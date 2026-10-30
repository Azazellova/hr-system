package ru.hr.backend.document.builder;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class AbstractHiringDocumentBuilder implements DocumentBuilder {
    protected String title;
    protected Long employeeId;
    protected String fullName;
    protected Long eventId;
    protected String departmentName;
    protected String positionName;
    protected LocalDate employmentDate;
    protected BigDecimal salary;
    protected String employerName;
    protected String contractNumber;

    @Override public DocumentBuilder title(String value) { title = value; return this; }
    @Override public DocumentBuilder employee(Long id, String name) { employeeId = id; fullName = name; return this; }
    @Override public DocumentBuilder eventId(Long value) { eventId = value; return this; }
    @Override public DocumentBuilder department(String value) { departmentName = value; return this; }
    @Override public DocumentBuilder position(String value) { positionName = value; return this; }
    @Override public DocumentBuilder employmentDate(LocalDate value) { employmentDate = value; return this; }
    @Override public DocumentBuilder salary(BigDecimal value) { salary = value; return this; }
    @Override public DocumentBuilder employer(String value) { employerName = value; return this; }
    @Override public DocumentBuilder contractNumber(String value) { contractNumber = value; return this; }

    protected void validateCommon() {
        if (employeeId == null || fullName == null || fullName.isBlank()
                || eventId == null || departmentName == null || positionName == null
                || employmentDate == null || salary == null || employerName == null) {
            throw new IllegalStateException("Не заполнены обязательные данные документа");
        }
    }
}
