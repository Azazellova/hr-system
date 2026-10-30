package ru.hr.backend.document.factory;

import org.springframework.stereotype.Component;
import ru.hr.backend.document.builder.DocumentBuilder;
import ru.hr.backend.document.builder.HiringOrderBuilder;
import ru.hr.backend.document.dto.HiringDocumentRequest;
import ru.hr.backend.document.model.DocumentType;

@Component
public class HiringOrderGenerator implements DocumentGenerator {
    @Override public DocumentType getType() { return DocumentType.HIRING_ORDER; }
    @Override public String getTitle() { return "Приказ о приёме на работу"; }

    @Override
    public DocumentBuilder createBuilder(HiringDocumentRequest r) {
        return new HiringOrderBuilder()
                .title(getTitle())
                .employee(r.employeeId(), r.employeeFullName())
                .eventId(r.eventId())
                .department(r.departmentName())
                .position(r.positionName())
                .employmentDate(r.employmentDate())
                .salary(r.salary())
                .employer(r.employerName())
                .contractNumber(r.contractNumber());
    }
}
