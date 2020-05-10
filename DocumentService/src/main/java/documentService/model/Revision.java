package documentService.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="revisions")
public class Revision implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String downloadLink;
	
	private String notes;
	
	private Date timestampForRevision;
	
	@JsonIgnore
	@Lob
	private byte[] data;
	
	public Revision() {
		
	}
	
	public Revision(byte[] data) {
		this.data = data;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getTimestampForRevision() {
		return timestampForRevision;
	}

	public void setTimestampForRevision(Date timestampForRevision) {
		this.timestampForRevision = timestampForRevision;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	

}
