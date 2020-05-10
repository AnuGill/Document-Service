package documentService.model.dao;

import java.util.List;

import documentService.model.Document;

public interface DocumentDao {
	
	Document getDocument(String name);
	
	List<Document> getAllDocuments();
	
	String saveDocument(Document document);

}
