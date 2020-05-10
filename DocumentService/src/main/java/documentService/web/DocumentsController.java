package documentService.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import documentService.model.Document;
import documentService.model.Revision;
import documentService.model.dao.DocumentDao;

@RestController
@RequestMapping("/documents")
public class DocumentsController {
	
	
	@Autowired
	private DocumentDao documentDao;
	
	// 1).Get All Documents.
	@GetMapping
	public List<Document> getDocuments(ModelMap map) {
		return documentDao.getAllDocuments();
	}
	
	
	// 2). Get a document.
	@GetMapping("/{name}")
	public Document get(@PathVariable String name) {
		Document document = documentDao.getDocument(name);
		if(document == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found");
		return document;
	}
	
	// 3). Download the file of revision.
	@GetMapping("/{name}/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String name, @PathVariable String id){
		Document document = documentDao.getDocument(name);
		String[] tokens = id.split("_");
		Revision lastUpdated = document.getRevisions().get(Integer.parseInt(tokens[1])-1);
		
		return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .body(new ByteArrayResource(lastUpdated.getData()));
	}
	
	// 4) Add a document. 5) Add a new revision to a document.
	@RequestMapping(path= "/{name}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public String uploadDocument(@PathVariable String name, @RequestParam(value = "notes", required = false) String notes,  @RequestParam("file") MultipartFile file) {
		Document document;
		if(documentDao.getDocument(name) == null) {
			document = new Document();
			document.setName(name);
			document.setTimestamp(new Date());
		}
		else {
			document = documentDao.getDocument(name);
		}
		
		Revision newFile = null;
		try {
			newFile = new Revision(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(notes == null) notes = "";
		newFile.setNotes(notes);
		newFile.setTimestampForRevision(new Date());
		List<Revision> revisions;
		if(document.getRevisions() == null) {
			 revisions = new ArrayList<>();
		}
		else {
			revisions = document.getRevisions();
		}
		revisions.add(newFile);
		document.setRevisions(revisions);
		int id = document.getRevisions().size();
		newFile.setId(document.getName()+"_"+id); // Revision Id = DocumentId_totalRevisionsTillNow.
		document.setTotalRevisions(document.getRevisions().size());
		document.setTimestamp(document.getRevisions().get(document.getRevisions().size()-1).getTimestampForRevision());
		newFile.setDownloadLink("http://localhost:8080/documents/"+document.getName()+"/"+newFile.getId());
		documentDao.saveDocument(document);
		return document.getName();
	}
	
	// 6). Edit a revision -- only the revision notes can be edited, not the file.
	@RequestMapping(path= "/{name}/{id}", method = RequestMethod.PUT)
	public void editRevision(@PathVariable String name, @PathVariable String id, @RequestParam(value = "notes") String notes) {
		Document doc = documentDao.getDocument(name);
		String[] tokens = id.split("_");
		Revision revision = doc.getRevisions().get(Integer.parseInt(tokens[1])-1);
		revision.setNotes(notes);
		documentDao.saveDocument(doc);
	}
	
}
