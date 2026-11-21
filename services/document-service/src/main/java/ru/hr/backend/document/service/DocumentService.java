package ru.hr.backend.document.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hr.backend.document.dto.DocumentResponse;
import ru.hr.backend.document.dto.HiringDocumentRequest;
import ru.hr.backend.document.entity.DocumentEntity;
import ru.hr.backend.document.factory.DocumentFactory;
import ru.hr.backend.document.factory.DocumentGenerator;
import ru.hr.backend.document.model.DocumentStatus;
import ru.hr.backend.document.model.DocumentType;
import ru.hr.backend.document.repository.DocumentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository repository;
    private final DocumentFactory factory;

    public DocumentService(DocumentRepository repository, DocumentFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    @Transactional
    public List<DocumentResponse> createHiringDocuments(HiringDocumentRequest request) {
        DocumentType[] types = {
                DocumentType.EMPLOYMENT_CONTRACT,
                DocumentType.HIRING_ORDER
        };

        return java.util.Arrays.stream(types)
                .map(type -> createOne(request, type))
                .toList();
    }

    private DocumentResponse createOne(HiringDocumentRequest request, DocumentType type) {
        if (repository.existsByEventIdAndDocumentType(request.eventId(), type)) {
            throw new IllegalArgumentException(
                    "Документ типа " + type + " для события " + request.eventId() + " уже существует"
            );
        }

        DocumentGenerator generator = factory.getGenerator(type);
        String content = generator.createBuilder(request).build();

        DocumentEntity entity = new DocumentEntity(
                request.employeeId(),
                request.eventId(),
                type,
                generator.getTitle(),
                LocalDateTime.now(),
                content,
                DocumentStatus.CREATED
        );

        return toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> getEmployeeDocuments(Long employeeId) {
        return repository.findAllByEmployeeIdOrderByCreationDateDesc(employeeId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DocumentResponse getDocument(Long documentId) {
        DocumentEntity entity = repository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));
        return toResponse(entity);
    }

    private DocumentResponse toResponse(DocumentEntity e) {
        return new DocumentResponse(e.getDocumentId(), e.getEmployeeId(), e.getEventId(),
                e.getDocumentType(), e.getTitle(), e.getCreationDate(), e.getStatus(), e.getContent());
    }
}
