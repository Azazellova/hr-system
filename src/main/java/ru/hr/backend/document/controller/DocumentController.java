package ru.hr.backend.document.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hr.backend.document.dto.DocumentResponse;
import ru.hr.backend.document.dto.HiringDocumentRequest;
import ru.hr.backend.document.service.DocumentService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentController {
    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping("/documents/hiring")
    @ResponseStatus(HttpStatus.CREATED)
    public List<DocumentResponse> createHiringDocuments(
            @Valid @RequestBody HiringDocumentRequest request) {
        return service.createHiringDocuments(request);
    }

    @GetMapping("/documents/{documentId}")
    public DocumentResponse getDocument(@PathVariable Long documentId) {
        return service.getDocument(documentId);
    }

    @GetMapping("/employees/{employeeId}/documents")
    public List<DocumentResponse> getEmployeeDocuments(@PathVariable Long employeeId) {
        return service.getEmployeeDocuments(employeeId);
    }
}
