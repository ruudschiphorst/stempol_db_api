package nl.politie.predev.notes.api.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
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
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

	
	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private MultimediaRepository multimediaRepository;

	@Autowired
	private NoteTranscriptRepository noteTranscriptRepository;

	@Autowired
	private SharedNotesRepository sharedNotesRepository;

	private String jwtSecret=System.getenv("JWT_SIGNING_KEY");
	
	@GetMapping("/privacy")
	public ResponseEntity<?> getPrivacy(){
			
		String retval ="<h2>Privacy Policy</h2>\n" + 
				"<p>Your privacy is important to us. It is Predev's policy to respect your privacy regarding any information we may collect from you through our app, Het Digitale Zakboekje.</p>\n" + 
				"<p>We only ask for personal information when we truly need it to provide a service to you. We collect it by fair and lawful means, with your knowledge and consent. We also let you know why we’re collecting it and how it will be used.</p>\n" + 
				"<p>We only retain collected information for as long as necessary to provide you with your requested service. What data we store, we’ll protect within acceptable means to prevent loss and theft, as well as unauthorized access, disclosure, copying, use or modification.</p>\n" + 
				"<p>We don’t share any information publicly or with third-parties, except when required to by law.</p>\n" + 
				"<p>Our app may link to external sites that are not operated by us. Please be aware that we have no control over the content and practices of these sites, and cannot accept responsibility or liability for their respective privacy policies.</p>\n" + 
				"<p>You are free to refuse our request for your personal information, with the understanding that we may be unable to provide you with some of your desired services.</p>\n" + 
				"<p>Your continued use of our app will be regarded as acceptance of our practices around privacy and personal information. If you have any questions about how we handle user data and personal information, feel free to contact us.</p>\n" + 
				"<p>This policy is effective as of 14 April 2020.</p>";
		
		return ResponseEntity.ok(retval);
	}
	
	@GetMapping("/health")
	public ResponseEntity<?> getHealth() {
		return ResponseEntity.ok("System up");
	}
	

	@GetMapping("/getall")
	public ResponseEntity<?> getAll(HttpServletRequest req){
		String username = getUsernameFromJWT(req.getHeader("Authorization").replace("Bearer ", ""));
		String groups = getGroupsFromJWTAsString(req.getHeader("Authorization").replace("Bearer ", ""));
		List<Note> notes = notesRepository.getAll(username, groups);
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
			notes.add(note);
		}
		
		return ResponseEntity.ok(notes);
	}
	
	
	@GetMapping("/getmypublicnotes")
	public ResponseEntity<?> getMyPublicNotes(HttpServletRequest req){
		String username = getUsernameFromJWT(req.getHeader("Authorization").replace("Bearer ", ""));
		String groups = getGroupsFromJWTAsString(req.getHeader("Authorization").replace("Bearer ", ""));
		List<Note> notes = notesRepository.getMyNotes(username, groups);
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
			if(note.isIs_public()){
				notes.add(note);
			}
		}
		
		return ResponseEntity.ok(notes);
	}
	
	@GetMapping("/getmynotes")
	public ResponseEntity<?> getMyNotes(HttpServletRequest req){
		String username = getUsernameFromJWT(req.getHeader("Authorization").replace("Bearer ", ""));
		String groups = getGroupsFromJWTAsString(req.getHeader("Authorization").replace("Bearer ", ""));
		List<Note> notes = notesRepository.getMyNotes(username, groups);
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
			notes.add(note);
		}
		
		return ResponseEntity.ok(notes);
	}
	
	@GetMapping("/getpublicnotes")
	public ResponseEntity<?> getPublicNotes(HttpServletRequest req){
		String username = getUsernameFromJWT(req.getHeader("Authorization").replace("Bearer ", ""));
		String groups = getGroupsFromJWTAsString(req.getHeader("Authorization").replace("Bearer ", ""));
		List<Note> notes = notesRepository.getAll(username, groups);
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
			if(note.isIs_public() && 
					(!note.getCreated_by().equalsIgnoreCase(username) || !note.getOwner().equalsIgnoreCase(username))){
				notes.add(note);
			}
		}
		
		return ResponseEntity.ok(notes);
	}
	
	@PostMapping("/getallversionsofnote")
	public ResponseEntity<?> getAllVersionsOfNote(@Valid @RequestBody NoteIdentifier id, HttpServletRequest req) {
		List<Note> fetched = notesRepository.findAllOtherNoteVersionsByID(id.getNoteID(), id.getVersion());
		List<Note> retval = new ArrayList<Note>();
		String username = getUsernameFromJWT(req.getHeader("Authorization").replace("Bearer ", ""));
		for(Note n: fetched){
			if(n.getOwner().equalsIgnoreCase(username) || n.getCreated_by().equalsIgnoreCase(username) || n.isIs_public()) {
				retval.add(n);
			}
		}
		
		return ResponseEntity.ok(retval);
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
			
			if(note.getMultimedia() != null) {
				for(Multimedia multimedia : note.getMultimedia()) {
					handleMultimediaUpload(multimedia, n);
				}
			}
			
			return ResponseEntity.ok(notesRepository.findById(n.getId()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		}
	}

	private void handleMultimediaUpload(Multimedia multimedia, Note note) {
		
		if(multimedia.getFilepath() !=null) {
			//bestaat al
			return;
		}
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
				String thumbnailAsBase64String ="";

				if(multimedia.getFilepath() != null && Files.exists(Paths.get(multimedia.getFilepath()))) {
					try{
						//Voorkeur om on the fly een thumbnail te maken
						thumbnailAsBase64String = createThumbnailAsBase64String(multimedia.getFilepath());
					}catch(IOException ioException) {
						//Maar als thumb maken niet lukt, dan maar het origineel teruggeven
						thumbnailAsBase64String = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(multimedia.getFilepath())));
					}
				}
				multimedia.setThumbnailContent(thumbnailAsBase64String);
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
	
	@PostMapping("/getmysqlinjectionnotesbyowner")
	public ResponseEntity<?> getMySqlInjectionNotesByOwner(@RequestParam String owner, HttpServletRequest req) {
		return ResponseEntity.ok(notesRepository.getMySqlInjectionNotesByOwner(owner));
	}
	@PostMapping("/getmysuperunsafenotes")
	public ResponseEntity<?> getMySuperUnsafeNotes(@RequestParam(name="owner", required=false) String owner, HttpServletRequest req) {
		
		EntityManager em = notesRepository.getEM();
		
		String jql = "SELECT * from notes where owner = '" + owner + "'";        
	    TypedQuery<Note> q = em.createQuery(jql, Note.class);        
	    return ResponseEntity.ok(q.getResultList());
		
	}

	
//	private List<Note> getFilteredNotes(List<Note> notes, HttpServletRequest req) {
//		
//		List<Note> retval = new ArrayList<Note>();
//
//		for(Note note : notes) {
//			Note filteredNote = getFilteredNote(note, req);
//			if(filteredNote !=null) {
//				retval.add(filteredNote);
//			}
//		}
//		
//		return retval;
//	}

	private String createThumbnailAsBase64String(String pathToOriginal) throws IOException {
		
		//100 x 100 pixels
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		img.createGraphics().drawImage(ImageIO.read(new File(pathToOriginal)).getScaledInstance(100, 100, Image.SCALE_SMOOTH),0,0,null);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( img, "jpg", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		
		return Base64.getEncoder().encodeToString(imageInByte);
		
	}
	
//	private Note getFilteredNote(Note note, HttpServletRequest req) {
//		
//		//TODO
//		//TODO
//		//TODO 
//		//RBAC filters
//		
//		//User en dr groups waar hij in zit ophalen
//		String username = getUsernameFromJWT(req.getHeader("Authorization").replace("Bearer ", ""));
//		List<String> groups =  getGroupsFromJWT(req.getHeader("Authorization").replace("Bearer ", ""));
//		
//		//Als ik de owner ben mag ik hem zien
//		if(note.getOwner().equalsIgnoreCase(username)) {
//			return note;
//		}else{
//			for(SharedNote sharedNote: note.getShareDetails()){
//				//Als de note met mij of met de groep waar ik in zit gedeeld wordt, dan mag ik hem zien
//				if(sharedNote.getSharedWithUsername().equalsIgnoreCase(username)|| groups.contains(sharedNote.getSharedWithGroupname())) {
//					return note;
//				}
//			}
//		}
//		return null;
//	}
	
	
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
	public String getGroupsFromJWTAsString(String token) {
		Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        String rollen = (String) claims.get("rol");
        return rollen.replace(";", ",");
	}
}
