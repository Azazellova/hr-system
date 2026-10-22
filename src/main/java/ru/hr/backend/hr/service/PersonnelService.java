package ru.hr.backend.hr.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hr.backend.employee.dto.EmployeeRequest;
import ru.hr.backend.employee.dto.EmployeeResponse;
import ru.hr.backend.employee.service.EmployeeService;
import ru.hr.backend.hr.event.PersonnelEvent;
import ru.hr.backend.hr.state.EmploymentState;
import ru.hr.backend.hr.state.EmploymentStates;

import java.time.LocalDate;

/** Facade: координирует кадровые операции через существующий сервис сотрудников. */
@Service
public class PersonnelService {
    private final EmployeeService employeeService;
    private final ApplicationEventPublisher eventPublisher;

    public PersonnelService(EmployeeService employeeService, ApplicationEventPublisher eventPublisher) {
        this.employeeService = employeeService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public EmployeeResponse activate(Long employeeId, String reason) {
        EmployeeResponse current = employeeService.getEmployeeById(employeeId);
        EmploymentState state = EmploymentStates.from(current.getEmploymentStatus(), current.getIsActive());
        state.activate();

        EmployeeRequest request = toRequest(current);
        request.setEmploymentStatus("ACTIVE");
        request.setIsActive(true);
        if (request.getEmploymentDate() == null) request.setEmploymentDate(LocalDate.now());
        request.setDismissalDate(null);
        EmployeeResponse updated = employeeService.updateEmployee(employeeId, request);
        publish(employeeId, "ACTIVATE", "ACTIVE", reason);
        return updated;
    }

    @Transactional
    public EmployeeResponse suspend(Long employeeId, String reason) {
        EmployeeResponse current = employeeService.getEmployeeById(employeeId);
        EmploymentState state = EmploymentStates.from(current.getEmploymentStatus(), current.getIsActive());
        state.suspend();

        EmployeeRequest request = toRequest(current);
        request.setEmploymentStatus("SUSPENDED");
        request.setIsActive(false);
        EmployeeResponse updated = employeeService.updateEmployee(employeeId, request);
        publish(employeeId, "SUSPEND", "SUSPENDED", reason);
        return updated;
    }

    @Transactional
    public EmployeeResponse dismiss(Long employeeId, String reason) {
        EmployeeResponse current = employeeService.getEmployeeById(employeeId);
        EmploymentState state = EmploymentStates.from(current.getEmploymentStatus(), current.getIsActive());
        state.dismiss();

        EmployeeRequest request = toRequest(current);
        request.setEmploymentStatus("DISMISSED");
        request.setIsActive(false);
        request.setDismissalDate(LocalDate.now());
        EmployeeResponse updated = employeeService.updateEmployee(employeeId, request);
        publish(employeeId, "DISMISS", "DISMISSED", reason);
        return updated;
    }

    private void publish(Long id, String action, String status, String reason) {
        eventPublisher.publishEvent(new PersonnelEvent(id, action, status, reason));
    }

    private EmployeeRequest toRequest(EmployeeResponse source) {
        EmployeeRequest request = new EmployeeRequest();
        request.setLastName(source.getLastName());
        request.setFirstName(source.getFirstName());
        request.setMiddleName(source.getMiddleName());
        request.setBirthDate(source.getBirthDate());
        request.setPhone(source.getPhone());
        request.setEmail(source.getEmail());
        request.setAddress(source.getAddress());
        request.setDepartmentId(source.getDepartmentId());
        request.setPositionId(source.getPositionId());
        request.setEmploymentDate(source.getEmploymentDate());
        request.setDismissalDate(source.getDismissalDate());
        request.setEmploymentStatus(source.getEmploymentStatus());
        request.setWorkSchedule(source.getWorkSchedule());
        request.setIsActive(source.getIsActive());
        return request;
    }
}
