package ru.hr.backend.document.builder;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DocumentBuilder {
    DocumentBuilder title(String title);
    DocumentBuilder employee(Long employeeId, String fullName);
    DocumentBuilder eventId(Long eventId);
    DocumentBuilder department(String departmentName);
    DocumentBuilder position(String positionName);
    DocumentBuilder employmentDate(LocalDate employmentDate);
    DocumentBuilder salary(BigDecimal salary);
    DocumentBuilder employer(String employerName);
    DocumentBuilder contractNumber(String contractNumber);
    String build();
}
