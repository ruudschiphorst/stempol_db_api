package nl.politie.predev.notes.api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

	private String jwtSecret="JWTSuperSecretKey";
	
	@GetMapping("/getall")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(notesRepository.findAll());
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
			Note n = notesRepository.saveAndFlush(note);
			return ResponseEntity.ok(notesRepository.findById(n.getId()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		}
	}

	@GetMapping("/getnotebyidandversion")
	public ResponseEntity<?> getNoteByIdAndVersion(@Valid @RequestBody NoteIdentifier id, HttpServletRequest req) {
		return ResponseEntity.ok(notesRepository.findNoteByIdAndVersion(id.getNoteID(), id.getVersion()));
	}

	@GetMapping("/getnote")
	public ResponseEntity<?> getMostRecentNoteByID(@RequestBody NoteIdentifier id, HttpServletRequest req) {
		try {
			Note note = notesRepository.findMostRecentNoteByID(id.getNoteID());

			note.setMultimedia(multimediaRepository.findByNoteID(id.getNoteID()));
			note.setTranscripts(noteTranscriptRepository.findByNoteID(id.getNoteID()));
			note.setShareDetails(sharedNotesRepository.findByNoteID(id.getNoteID()));
			return ResponseEntity.ok(note);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		}
	}

	@GetMapping("/getallnotesbyid")
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
