package nl.politie.predev.notes.api.repository;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nl.politie.predev.notes.api.model.Multimedia;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Long> {
	
	@Query("SELECT m FROM Multimedia m WHERE noteID = ?1 AND isDeleted = false")
	List<Multimedia> findByNoteID(UUID noteID);
	
	@Transactional
	@Modifying
	@Query("UPDATE Multimedia SET is_deleted = true WHERE id = ?1")
	void deleteMultimedia(Long id);

}
