package nl.politie.predev.notes.api.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NOTE_TRANSCRIPTS")
public class NoteTranscript {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(insertable = false, updatable = false, nullable = false)
	private Long id;
	
	@Column
	private UUID noteID;
	
	@Column(name = "noteversion")
	private Integer noteVersion;
	
	@Column
	private String transcriptID;
	
	@Column
	private String transcript_text;

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

	public String getTranscriptID() {
		return transcriptID;
	}

	public void setTranscriptID(String transcriptID) {
		this.transcriptID = transcriptID;
	}

	public String getTranscript_text() {
		return transcript_text;
	}

	public void setTranscript_text(String transcript_text) {
		this.transcript_text = transcript_text;
	}
	
	
	
}
