package ru.hr.backend.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hr.backend.document.entity.DocumentEntity;
import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findAllByEmployeeIdOrderByCreationDateDesc(Long employeeId);
    boolean existsByEventIdAndDocumentType(Long eventId, ru.hr.backend.document.model.DocumentType documentType);
}
