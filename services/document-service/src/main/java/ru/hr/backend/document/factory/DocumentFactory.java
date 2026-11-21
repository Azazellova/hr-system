package ru.hr.backend.document.factory;

import org.springframework.stereotype.Component;
import ru.hr.backend.document.model.DocumentType;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class DocumentFactory {
    private final Map<DocumentType, DocumentGenerator> generators =
            new EnumMap<>(DocumentType.class);

    public DocumentFactory(List<DocumentGenerator> generators) {
        for (DocumentGenerator generator : generators) {
            this.generators.put(generator.getType(), generator);
        }
    }

    public DocumentGenerator getGenerator(DocumentType type) {
        DocumentGenerator generator = generators.get(type);
        if (generator == null) {
            throw new IllegalArgumentException("Неизвестный тип документа: " + type);
        }
        return generator;
    }
}
