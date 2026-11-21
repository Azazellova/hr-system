package ru.hr.backend.document.factory;

import ru.hr.backend.document.builder.DocumentBuilder;
import ru.hr.backend.document.dto.HiringDocumentRequest;
import ru.hr.backend.document.model.DocumentType;

public interface DocumentGenerator {
    DocumentType getType();
    DocumentBuilder createBuilder(HiringDocumentRequest request);
    String getTitle();
}
