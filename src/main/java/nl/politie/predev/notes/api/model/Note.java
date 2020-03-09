package nl.politie.predev.notes.api.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "NOTES")
public class Note implements java.io.Serializable {

	private static final long serialVersionUID = -2756761901963218687L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, insertable = false, nullable = false)
	private Long id;
	

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, insertable = false, nullable = false)
	private UUID noteID;
	
	@Column(updatable = false, insertable = true, nullable = false)
	private Integer version;
	
	@Column
	private String title;
	@Column
	private String note_text;
	@Column
	private String owner;
	@Column
	private String created_by;	
	@Column(insertable = false, updatable = false)
	private Timestamp generated_at;	
	@Column
	private boolean is_public=true;
	@Column
	private boolean is_deleted=false;
	@Column
	private double grondslag = 8.0;
	@Column
	private Integer autorisatieniveau = 1;
	@Column
	private Integer afhandelcode = 11;
	
//	@OneToMany(mappedBy = "note", fetch = FetchType.EAGER)
	
	@JsonInclude()
	@Transient
	private List<NoteTranscript> transcripts;
	
	@JsonInclude()
	@Transient
	private List<Multimedia> multimedia;
	
	@JsonInclude()
	@Transient
	private List<SharedNote> shareDetails;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id= id;
	}
	public List<Multimedia> getMultimedia() {
		return multimedia;
	}
	public void setMultimedia(List<Multimedia> multimedia) {
		this.multimedia = multimedia;
	}
	public List<NoteTranscript> getTranscripts() {
		return transcripts;
	}
	public void setTranscripts(List<NoteTranscript> transcript) {
		this.transcripts = transcript;
	}
	public List<SharedNote> getShareDetails() {
		return shareDetails;
	}
	public void setShareDetails(List<SharedNote> shareDetails) {
		this.shareDetails = shareDetails;
	}
	public UUID getNoteID() {
		return noteID;
	}
	public void setNoteID(UUID noteID) {
		this.noteID = noteID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNote_text() {
		return note_text;
	}
	public void setNote_text(String note_text) {
		this.note_text = note_text;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public Timestamp getGenerated_at() {
		return generated_at;
	}
	public void setGenerated_at(Timestamp generated_at) {
		this.generated_at = generated_at;
	}
	public boolean isIs_public() {
		return is_public;
	}
	public void setIs_public(boolean is_public) {
		this.is_public = is_public;
	}
	public boolean isIs_deleted() {
		return is_deleted;
	}
	public void setIs_deleted(boolean is_deleted) {
		this.is_deleted = is_deleted;
	}
	public double getGrondslag() {
		return grondslag;
	}
	public void setGrondslag(double grondslag) {
		this.grondslag = grondslag;
	}
	public Integer getAutorisatieniveau() {
		return autorisatieniveau;
	}
	public void setAutorisatieniveau(Integer autorisatieniveau) {
		this.autorisatieniveau = autorisatieniveau;
	}
	public Integer getAfhandelcode() {
		return afhandelcode;
	}
	public void setAfhandelcode(Integer afhandelcode) {
		this.afhandelcode = afhandelcode;
	}
	
}
