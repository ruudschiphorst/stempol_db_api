package nl.politie.predev.notes.api.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "MULTIMEDIA")
public class Multimedia implements java.io.Serializable {

	private static final long serialVersionUID = 951548968715616281L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(insertable = false, updatable = false, nullable = false)
	private Long id;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	private UUID multimediaID;
	@Column
	private String title;
	@Column
	private String filepath;
	
	@Column
	private String filetype;
	
	@Column
	private UUID noteID;
	@Column(name = "noteversion")
	private Integer noteVersion;
	
	@Column(name = "is_deleted")
	private boolean isDeleted;
	
	@Transient
	private String content;
	
	@Transient
	private String thumbnailContent;
	
	public boolean isDeleted(){
		return isDeleted;
	}
	
	public void setDeleted(boolean deleted){
		this.isDeleted = deleted;
	}
	
	
	public Integer getNoteVersion() {
		return noteVersion;
	}
	public void setNoteVersion(Integer noteVersion) {
		this.noteVersion = noteVersion;
	}
	public UUID getNoteID() {
		return noteID;
	}
	public void setNoteID(UUID noteUUID) {
		this.noteID = noteUUID;
	}
	public UUID getMultimediaID() {
		return multimediaID;
	}
	public void setMultimediaID(UUID multimediaID) {
		this.multimediaID = multimediaID;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getThumbnailContent() {
		return thumbnailContent;
	}

	public void setThumbnailContent(String thumbnailContent) {
		this.thumbnailContent = thumbnailContent;
	}
}
