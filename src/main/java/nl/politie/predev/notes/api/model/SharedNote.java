package nl.politie.predev.notes.api.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SHARED_NOTES")
public class SharedNote {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(insertable = false, updatable = false, nullable = false)
	private Long id;
	
	@Column
	private UUID noteID;
	
	@Column(name = "noteversion")
	private Integer noteVersion;
	
	@Column(name="shared_with_username")
	private String sharedWithUsername;
	
	@Column(name="shared_with_groupname")
	private String sharedWithGroupname;

	
	@ManyToOne
	@JoinColumn(name="roleID", referencedColumnName = "id", insertable = false, updatable = false)
	private Role role;
	
	
	@Column(name = "is_deleted")
	private boolean isDeleted;
	
	public boolean isDeleted(){
		return isDeleted;
	}
	
	public void setDeleted(boolean deleted){
		this.isDeleted = deleted;
	}
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UUID getNoteID() {
		return noteID;
	}

	public void setNoteID(UUID noteID) {
		this.noteID = noteID;
	}

	public Integer getNoteVersion() {
		return noteVersion;
	}

	public void setNoteVersion(Integer noteVersion) {
		this.noteVersion = noteVersion;
	}

	public String getSharedWithUsername() {
		return sharedWithUsername;
	}

	public void setSharedWithUsername(String sharedWithUsername) {
		this.sharedWithUsername = sharedWithUsername;
	}

	public String getSharedWithGroupname() {
		return sharedWithGroupname;
	}

	public void setSharedWithGroupname(String sharedWithGroupname) {
		this.sharedWithGroupname = sharedWithGroupname;
	}
	
	
}
