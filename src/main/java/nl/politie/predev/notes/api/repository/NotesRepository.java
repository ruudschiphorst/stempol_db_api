package nl.politie.predev.notes.api.repository;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nl.politie.predev.notes.api.model.Note;


@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {

	@Query("SELECT n FROM Note n WHERE noteID = ?1 AND version = ?2 AND is_deleted = false")
	Note findNoteByIdAndVersion(UUID noteID, Integer version);
	
	@Query("SELECT n FROM Note n WHERE noteID = ?1 AND version=(SELECT max(version) FROM Note n1 WHERE noteID = ?1) AND is_deleted = false")
	Note findMostRecentNoteByID(UUID noteID);
	
	@Query("SELECT n FROM Note n WHERE noteID = ?1 AND version=(SELECT max(version) FROM Note n1 WHERE noteID = ?1)")
	Note findMostRecentNoteByIdIncludingDeleted(UUID noteID);
	
	@Query("SELECT n FROM Note n WHERE noteID = ?1 AND is_deleted = false")
	List<Note> findAllNoteVersionsByID(UUID noteID);
	
	@Transactional
	@Modifying
	@Query("UPDATE Note n SET is_deleted = true WHERE noteID = ?1")
	void deleteById(UUID noteID);
	
	@Transactional
	@Modifying
	@Query("UPDATE Note n SET is_deleted = true WHERE noteID = ?1 AND version = ?2")
	void deleteByIdAndVersion(UUID noteID, Integer version);
}