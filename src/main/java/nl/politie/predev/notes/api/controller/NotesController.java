package nl.politie.predev.notes.api.controller;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nl.politie.predev.notes.api.model.Multimedia;
import nl.politie.predev.notes.api.model.MultimediaID;
import nl.politie.predev.notes.api.model.Note;
import nl.politie.predev.notes.api.model.NoteIdentifier;
import nl.politie.predev.notes.api.model.SharedNote;
import nl.politie.predev.notes.api.repository.MultimediaRepository;
import nl.politie.predev.notes.api.repository.NoteTranscriptRepository;
import nl.politie.predev.notes.api.repository.NotesRepository;
import nl.politie.predev.notes.api.repository.SharedNotesRepository;


@RestController
public class NotesController {

	private static final String THUMB_FILE_PREFIX = "thumb_";
	
	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private MultimediaRepository multimediaRepository;

	@Autowired
	private NoteTranscriptRepository noteTranscriptRepository;

	@Autowired
	private SharedNotesRepository sharedNotesRepository;

	private String jwtSecret="JWTSuperSecretKey";
	
	@GetMapping("/get")
	public ResponseEntity<?> getEverythingTest(){
		return ResponseEntity.ok(notesRepository.findAll());
	}
	
	@GetMapping("/getall")
	public ResponseEntity<?> getAll(){
		List<Note> notes = notesRepository.getAll("Ruud", "mwdp");
		Map<String, Note> filteredNotes = new HashMap<String, Note>();
		
		//Alleen meest recente versies
		for(Note note: notes) {
			Note existing = filteredNotes.get(note.getNoteID().toString());
			if(existing == null || note.getVersion() > existing.getVersion()) {
				filteredNotes.put(note.getNoteID().toString(), note);
			} 
		}
		
		notes =  new ArrayList<Note>();
		
		for(Map.Entry<String, Note> entry: filteredNotes.entrySet()) {
			Note note  = entry.getValue();
//			note.setMultimedia(multimediaRepository.findByNoteID(note.getNoteID()));
			notes.add(note);
		}
		
		return ResponseEntity.ok(notes);
	}
	
	@PostMapping("/getmultimedia")
	public ResponseEntity<?> getMultimedia(@Valid @RequestBody MultimediaID multimediaID) {
		Multimedia retval = multimediaRepository.getMultimediaByUUID(multimediaID.getMultimediaID());
		
		if(retval.getFilepath() != null && Files.exists(Paths.get(retval.getFilepath()))) {
			byte[] data=null;
			try {
				data = Files.readAllBytes(Paths.get(retval.getFilepath()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			retval.setContent(Base64.getEncoder().encodeToString(data));
		}
		
		
		return ResponseEntity.ok(retval);
	}
	
	@PostMapping("/addnote")
	public ResponseEntity<?> addNote(@Valid @RequestBody Note note, HttpServletRequest req) {
		return updateNote(note, req);
	}

	@PostMapping("/updatenote")
	public ResponseEntity<?> updateNote(@Valid @RequestBody Note note, HttpServletRequest req) {
		System.err.println("updating note... " + note.getNoteID().toString());
		Integer versionNumber;
		try {
			//Note ophalen met grootste versienummer, nummer ophogen en dit wordt het nieuwe nummer
			Note mostRecentNoteInDatabase = notesRepository.findMostRecentNoteByIdIncludingDeleted(note.getNoteID());
			if (mostRecentNoteInDatabase == null || mostRecentNoteInDatabase.getVersion() == null) {
				versionNumber = 1;
			} else {
				versionNumber = mostRecentNoteInDatabase.getVersion() + 1;
			}
			note.setVersion(versionNumber);
			note.setCreated_by(getUsernameFromJWT(req.getHeader("Authorization").replace("Bearer ", "")));
			
			if(note.getOwner() == null || note.getOwner().equals(null)) {
				note.setOwner(getUsernameFromJWT(req.getHeader("Authorization").replace("Bearer ", "")));
			}
			if(note.getNoteID() == null || note.getNoteID().equals(null)) {
				note.setNoteID(UUID.randomUUID());
			}
			
			
			Note n = notesRepository.save(note);
			notesRepository.refresh(n);
			
			if(n.getMultimedia() != null) {
				System.err.println("adding multimedia no#: " + n.getMultimedia().size() );
				for(Multimedia multimedia : n.getMultimedia()) {
					handleMultimediaUpload(multimedia, n);
				}
			}
			
			return ResponseEntity.ok(notesRepository.findById(n.getId()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		}
	}

	private void handleMultimediaUpload(Multimedia multimedia, Note note) {
		
		System.err.println("handling upload");
		String fileUUID = UUID.randomUUID().toString();
		
		String path = "/tmp/fotos/" + fileUUID + ".jpg";
		multimedia.setNoteID(note.getNoteID());
		multimedia.setDeleted(false);
		multimedia.setFiletype("jpg");
		multimedia.setNoteVersion(note.getVersion());
		multimedia.setTitle("Nieuwe titel als placeholder");
		multimedia.setFilepath(path);
		if(multimedia.getMultimediaID() == null || multimedia.getMultimediaID().equals(null)) {
			multimedia.setMultimediaID(UUID.randomUUID());
		}
		
		try {
			
			byte[] decodedContent = Base64.getDecoder().decode(multimedia.getContent());
			Path filepath = Paths.get(path);
			Files.createFile(filepath);
            Files.write(filepath, decodedContent);
//            System.err.println("created regular file");
//            if(multimedia.getThumbnailContent() !=null) {
//	            decodedContent = Base64.getDecoder().decode(multimedia.getThumbnailContent());
//	            filepath = Paths.get(thumbPath);
//	            Files.createFile(filepath);
//	            Files.write(filepath, decodedContent);
//	            System.err.println("created thumbnail from thumbnal content");
//            }else{
//            	createThumbnail(path, thumbPath);
//            }
            
            multimediaRepository.save(multimedia);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	}
	
	@PostMapping("/getnotebyidandversion")
	public ResponseEntity<?> getNoteByIdAndVersion(@Valid @RequestBody NoteIdentifier id, HttpServletRequest req) {
		return ResponseEntity.ok(notesRepository.findNoteByIdAndVersion(id.getNoteID(), id.getVersion()));
	}

	@PostMapping("/getnote")
	public ResponseEntity<?> getMostRecentNoteByID(@RequestBody NoteIdentifier id, HttpServletRequest req) {
		try {
			List<Multimedia> fetchedMultimedia = multimediaRepository.findByNoteID(id.getNoteID());
			List<Multimedia> transformedMultimedia = new ArrayList<Multimedia>();
	
			//Omzetten naar base64 string, zodat ik het in JSON kan knallen
			for(Multimedia multimedia : fetchedMultimedia) {
				System.err.println("fetching multimedia");
				byte[] data=null;
				if(multimedia.getFilepath() != null && Files.exists(Paths.get(THUMB_FILE_PREFIX +multimedia.getFilepath()))) {
					data = Files.readAllBytes(Paths.get(THUMB_FILE_PREFIX + multimedia.getFilepath()));
				}else if(multimedia.getFilepath() != null && Files.exists(Paths.get(multimedia.getFilepath()))) {
					data = Files.readAllBytes(Paths.get(multimedia.getFilepath()));
				}
				multimedia.setThumbnailContent(Base64.getEncoder().encodeToString(data));
				transformedMultimedia.add(multimedia);
			}
			
			Note note = notesRepository.findMostRecentNoteByID(id.getNoteID());
			note.setMultimedia(transformedMultimedia);
			note.setTranscripts(noteTranscriptRepository.findByNoteID(id.getNoteID()));
			note.setShareDetails(sharedNotesRepository.findByNoteID(id.getNoteID()));
			return ResponseEntity.ok(note);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		}
	}

	@PostMapping("/getallnotesbyid")
	public ResponseEntity<?> getAllNotesByID(@RequestBody NoteIdentifier id, HttpServletRequest req) {
		return ResponseEntity.ok(notesRepository.findAllNoteVersionsByID(id.getNoteID()));
	}

	@PostMapping("/deletenote")
	public ResponseEntity<?> deleteNoteByIdAndVersion(@RequestBody NoteIdentifier id, HttpServletRequest req) {
		notesRepository.deleteByIdAndVersion(id.getNoteID(), id.getVersion());
		return ResponseEntity.ok("{\"acknowledged\": true}");
	}

	@PostMapping("/deletenotebyid")
	public ResponseEntity<?> deleteNoteByID(@RequestBody NoteIdentifier id, HttpServletRequest req) {
		notesRepository.deleteById(id.getNoteID());
		return ResponseEntity.ok("{\"acknowledged\": true}");
	}
	
	private List<Note> getFilteredNotes(List<Note> notes, HttpServletRequest req) {
		
		List<Note> retval = new ArrayList<Note>();

		for(Note note : notes) {
			Note filteredNote = getFilteredNote(note, req);
			if(filteredNote !=null) {
				retval.add(filteredNote);
			}
		}
		
		return retval;
	}
	
//	private void createThumbnail(String pathOfOriginal, String thumbPath) {
//		System.err.println("creating thumb...");
//		try {
//			BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//			img.createGraphics().drawImage(ImageIO.read(new File(pathOfOriginal)).getScaledInstance(100, 100, Image.SCALE_SMOOTH),0,0,null);
//			ImageIO.write(img, "jpg", new File(thumbPath));
//			System.err.println("created thumb");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

	private Note getFilteredNote(Note note, HttpServletRequest req) {
		
		//TODO
		//TODO
		//TODO 
		//RBAC filters
		
		//User en dr groups waar hij in zit ophalen
		String username = getUsernameFromJWT(req.getHeader("Authorization").replace("Bearer ", ""));
		List<String> groups =  getGroupsFromJWT(req.getHeader("Authorization").replace("Bearer ", ""));
		
		//Als ik de owner ben mag ik hem zien
		if(note.getOwner().equalsIgnoreCase(username)) {
			return note;
		}else{
			for(SharedNote sharedNote: note.getShareDetails()){
				//Als de note met mij of met de groep waar ik in zit gedeeld wordt, dan mag ik hem zien
				if(sharedNote.getSharedWithUsername().equalsIgnoreCase(username)|| groups.contains(sharedNote.getSharedWithGroupname())) {
					return note;
				}
			}
		}
		return null;
	}
	
	
	public String getUsernameFromJWT(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
        
    }
	
	public List<String> getGroupsFromJWT(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        String rollen = (String) claims.get("rol");
        return Arrays.asList(rollen.split(";"));
        
    }
}
