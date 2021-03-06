package nl.politie.predev.notes.api.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nl.politie.predev.notes.api.model.Note;

@Repository
public interface NotesRepository extends RefreshableRepository<Note, Long> {

	@Query("SELECT n FROM Note n WHERE noteID = ?1 AND version = ?2 AND is_deleted = false")
	Note findNoteByIdAndVersion(UUID noteID, Integer version);
	
	@Query("SELECT n FROM Note n WHERE noteID = ?1 AND version=(SELECT max(version) FROM Note n1 WHERE noteID = ?1) AND is_deleted = false")
	Note findMostRecentNoteByID(UUID noteID);
	
	@Query("SELECT n FROM Note n WHERE noteID = ?1 AND version=(SELECT max(version) FROM Note n1 WHERE noteID = ?1)")
	Note findMostRecentNoteByIdIncludingDeleted(UUID noteID);
	
	@Query("SELECT n FROM Note n WHERE n.noteID = ?1 AND n.is_deleted = false ORDER BY n.generated_at DESC")
	List<Note> findAllNoteVersionsByID(UUID noteID);
	
	@Query("SELECT n FROM Note n WHERE n.noteID = ?1 AND n.is_deleted = false AND n.version <> ?2 ORDER BY n.generated_at DESC")
	List<Note> findAllOtherNoteVersionsByID(UUID noteID, Integer version);
	
	@Transactional
	@Modifying
	@Query("UPDATE Note n SET is_deleted = true WHERE noteID = ?1")
	void deleteById(UUID noteID);
	
	@Transactional
	@Modifying
	@Query("UPDATE Note n SET is_deleted = true WHERE noteID = ?1 AND version = ?2")
	void deleteByIdAndVersion(UUID noteID, Integer version);
	
	//Werkt, maar tijdelijk shared er uit halen
	//@Query("SELECT n FROM Note n LEFT JOIN SharedNote s on n.noteID=s.noteID WHERE ((LOWER(n.owner) =LOWER(?1) OR LOWER(n.created_by) = LOWER(?1) OR (LOWER(s.sharedWithUsername)= LOWER(?1) OR LOWER(s.sharedWithGroupname) IN (LOWER(?2) OR n.is_public = true) AND s.isDeleted = false)) AND n.is_deleted = false) ORDER BY n.generated_at DESC")
	@Query("SELECT n FROM Note n WHERE (LOWER(n.owner) =LOWER(?1) OR LOWER(n.created_by) = LOWER(?1) OR n.is_public = true) AND n.is_deleted = false ORDER BY n.generated_at DESC")
	List<Note> getAll(String username, String roles);
	
	@Query("SELECT n FROM Note n WHERE (LOWER(n.owner) =LOWER(?1) OR LOWER(n.created_by) = LOWER(?1)) AND n.is_deleted = false ORDER BY n.generated_at DESC")
	List<Note> getMyNotes(String username, String roles);

	@Query("SELECT n FROM Note n WHERE n.owner = ?1")
	List<Note> getMySqlInjectionNotesByOwner(String title);
	
	EntityManager getEM();
	
//	@Query("SELECT n FROM Note n WHERE n.is_public = true AND n.is_deleted = false ORDER BY n.generated_at DESC")
//	@Query("SELECT n FROM Note n WHERE LOWER(n.owner) <> LOWER(?1) AND LOWER(n.created_by) <> LOWER(?1) AND n.is_deleted = false AND n.is_public = true ORDER BY n.generated_at DESC")
//	List<Note> getPublicNotes(String username);
//	
//	@Query("SELECT n FROM Note n WHERE (LOWER(n.owner) = LOWER(?1) OR LOWER(n.created_by) = LOWER(?1)) AND n.is_deleted = false AND n.is_public = true ORDER BY n.generated_at DESC")
//	List<Note> getMyPublicNotes(String username);
	
}
