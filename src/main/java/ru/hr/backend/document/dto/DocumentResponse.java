package ru.hr.backend.document.dto;

import ru.hr.backend.document.model.DocumentStatus;
import ru.hr.backend.document.model.DocumentType;
import java.time.LocalDateTime;

public record DocumentResponse(
        Long documentId,
        Long employeeId,
        Long eventId,
        DocumentType documentType,
        String title,
        LocalDateTime creationDate,
        DocumentStatus status,
        String content
) {}
