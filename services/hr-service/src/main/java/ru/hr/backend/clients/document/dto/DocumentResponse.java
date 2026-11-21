package ru.hr.backend.clients.document.dto;

import ru.hr.backend.clients.document.dto.DocumentStatus;
import ru.hr.backend.clients.document.dto.DocumentType;
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
