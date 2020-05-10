package documentService.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import documentService.model.Document;
import documentService.model.dao.DocumentDao;

@Repository
public class DocumentDaoImpl implements DocumentDao{
	
	@PersistenceContext
	private EntityManager entityManager; 

	@Override
	public Document getDocument(String name) {
		return entityManager.find(Document.class, name);
	}

	@Override
	public List<Document> getAllDocuments() {
		return entityManager.createQuery("from Document", Document.class).getResultList();
	}

	@Override
	@Transactional
	public String saveDocument(Document document) {
		entityManager.merge(document);
		return document.getName();
	}
	

}
