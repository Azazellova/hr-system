package ru.hr.backend.document.factory;

import org.springframework.stereotype.Component;
import ru.hr.backend.document.builder.DocumentBuilder;
import ru.hr.backend.document.builder.EmploymentContractBuilder;
import ru.hr.backend.document.dto.HiringDocumentRequest;
import ru.hr.backend.document.model.DocumentType;

@Component
public class EmploymentContractGenerator implements DocumentGenerator {
    @Override public DocumentType getType() { return DocumentType.EMPLOYMENT_CONTRACT; }
    @Override public String getTitle() { return "Трудовой договор"; }

    @Override
    public DocumentBuilder createBuilder(HiringDocumentRequest r) {
        return new EmploymentContractBuilder()
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
