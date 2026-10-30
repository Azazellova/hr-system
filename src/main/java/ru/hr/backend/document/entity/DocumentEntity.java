package ru.hr.backend.document.entity;

import jakarta.persistence.*;
import ru.hr.backend.document.model.DocumentStatus;
import ru.hr.backend.document.model.DocumentType;
import java.time.LocalDateTime;

@Entity
@Table(name = "document")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 50)
    private DocumentType documentType;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DocumentStatus status;

    protected DocumentEntity() {}

    public DocumentEntity(Long employeeId, Long eventId, DocumentType documentType,
                          String title, LocalDateTime creationDate, String content,
                          DocumentStatus status) {
        this.employeeId = employeeId;
        this.eventId = eventId;
        this.documentType = documentType;
        this.title = title;
        this.creationDate = creationDate;
        this.content = content;
        this.status = status;
    }

    public Long getDocumentId() { return documentId; }
    public Long getEmployeeId() { return employeeId; }
    public Long getEventId() { return eventId; }
    public DocumentType getDocumentType() { return documentType; }
    public String getTitle() { return title; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public String getContent() { return content; }
    public DocumentStatus getStatus() { return status; }
}
