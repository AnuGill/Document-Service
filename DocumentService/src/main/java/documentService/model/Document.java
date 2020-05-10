package documentService.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="documents")
public class Document implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	
	private Date timestamp;
	
	private Integer totalRevisions;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Revision> revisions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<Revision> getRevisions() {
		return revisions;
	}

	public void setRevisions(List<Revision> revisions) {
		this.revisions = revisions;
	}

	public Integer getTotalRevisions() {
		return totalRevisions;
	}

	public void setTotalRevisions(Integer totalRevisions) {
		this.totalRevisions = totalRevisions;
	}
}
