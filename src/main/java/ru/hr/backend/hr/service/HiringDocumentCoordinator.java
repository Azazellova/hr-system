package ru.hr.backend.hr.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hr.backend.accounting.PayrollService;
import ru.hr.backend.department.dto.DepartmentResponse;
import ru.hr.backend.department.dto.PositionResponse;
import ru.hr.backend.department.service.OrganizationService;
import ru.hr.backend.document.dto.HiringDocumentRequest;
import ru.hr.backend.document.service.DocumentService;
import ru.hr.backend.hr.entity.HrRequest;
import java.time.LocalDate;

/** Связывает согласованную заявку с бухгалтерским адаптером и сервисом документов. */
@Service
public class HiringDocumentCoordinator {
    private final OrganizationService organization;
    private final PayrollService payroll;
    private final DocumentService documents;
    private final String employerName;

    public HiringDocumentCoordinator(OrganizationService organization, PayrollService payroll,
                                     DocumentService documents, @Value("${company.name:ООО «Вектор»}") String employerName) {
        this.organization = organization;
        this.payroll = payroll;
        this.documents = documents;
        this.employerName = employerName;
    }

    /** Создаёт договор и приказ в транзакции согласования заявки. */
    public void createDocuments(HrRequest request) {
        if (request.getEmployeeId() == null) throw new IllegalStateException("Сотрудник ещё не создан");
        DepartmentResponse department = organization.getDepartment(request.getDepartmentId());
        PositionResponse position = organization.getPosition(request.getPositionId());
        if (!position.departmentId().equals(department.id())) throw new IllegalArgumentException("Должность не относится к подразделению");
        String fullName = String.join(" ", request.getLastName(), request.getFirstName(),
                request.getMiddleName() == null ? "" : request.getMiddleName()).trim();
        documents.createHiringDocuments(new HiringDocumentRequest(
                request.getEmployeeId(), request.getId(), fullName, department.name(), position.name(),
                request.getStartDate() == null ? LocalDate.now() : request.getStartDate(),
                payroll.getContractSalary(request.getPositionId()), employerName, "ТД-" + request.getId()));
    }
}
