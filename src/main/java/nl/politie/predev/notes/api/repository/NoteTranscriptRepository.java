package nl.politie.predev.notes.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import nl.politie.predev.notes.api.model.NoteTranscript;

public interface NoteTranscriptRepository extends JpaRepository<NoteTranscript, Long> {

	
	@Query("SELECT nt FROM NoteTranscript nt WHERE noteID = ?1")
	List<NoteTranscript> findByNoteID(UUID noteID);
	
	
}
