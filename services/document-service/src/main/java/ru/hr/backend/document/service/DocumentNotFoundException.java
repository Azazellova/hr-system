package ru.hr.backend.document.service;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(Long id) {
        super("Документ с ID " + id + " не найден");
    }
}
