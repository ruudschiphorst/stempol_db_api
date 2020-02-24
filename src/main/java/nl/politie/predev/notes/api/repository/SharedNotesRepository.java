package nl.politie.predev.notes.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import nl.politie.predev.notes.api.model.SharedNote;

public interface SharedNotesRepository extends JpaRepository<SharedNote, Long>{

//	@Query("SELECT DISTINCT NEW nl.politie.predev.notes.api.model.SharedNotes(sn.sharedWithUsername, sn.sharedWithGroupname) FROM SharedNotes sn WHERE noteID = ?1")
	@Query("SELECT sn FROM SharedNote sn WHERE noteID = ?1 AND isDeleted = false")
	List<SharedNote> findByNoteID(UUID noteID);
	
}
