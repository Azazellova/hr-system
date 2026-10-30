package ru.hr.backend.hr.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hr.backend.employee.dto.EmployeeRequest;
import ru.hr.backend.employee.dto.EmployeeResponse;
import ru.hr.backend.employee.service.EmployeeService;
import ru.hr.backend.hr.event.PersonnelEvent;
import ru.hr.backend.department.service.OrganizationService;
import ru.hr.backend.department.dto.DepartmentResponse;
import ru.hr.backend.department.dto.PositionResponse;
import ru.hr.backend.hr.state.EmploymentState;
import ru.hr.backend.hr.state.EmploymentStates;

import java.time.LocalDate;

/** Facade: координирует кадровые операции через существующий сервис сотрудников. */
@Service
public class PersonnelService {
    private final EmployeeService employeeService;
    private final ApplicationEventPublisher eventPublisher;
    private final OrganizationService organizationService;

    public PersonnelService(EmployeeService employeeService, ApplicationEventPublisher eventPublisher,
                            OrganizationService organizationService) {
        this.employeeService = employeeService;
        this.eventPublisher = eventPublisher;
        this.organizationService = organizationService;
    }

    @Transactional
    public EmployeeResponse transfer(Long employeeId, Long departmentId, Long positionId, String reason) {
        EmployeeResponse current = employeeService.getEmployeeById(employeeId);
        EmploymentState state = EmploymentStates.from(current.getEmploymentStatus(), current.getIsActive());
        if ("DISMISSED".equalsIgnoreCase(state.status())) {
            throw new IllegalStateException("Нельзя перевести уволенного сотрудника");
        }

        DepartmentResponse department = organizationService.getDepartment(departmentId);
        PositionResponse position = organizationService.getPosition(positionId);
        if (!position.departmentId().equals(department.id())) {
            throw new IllegalArgumentException("Указанная должность не относится к выбранному подразделению");
        }

        EmployeeRequest request = toRequest(current);
        request.setDepartmentId(departmentId);
        request.setPositionId(positionId);
        EmployeeResponse updated = employeeService.updateEmployee(employeeId, request);
        publish(employeeId, "TRANSFER", updated.getEmploymentStatus(), reason);
        return updated;
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


    @Transactional
    public EmployeeResponse increase(
            Long employeeId,
            Long positionId,
            String reason
    ) {
        // Получаем текущие данные сотрудника
        EmployeeResponse current =
                employeeService.getEmployeeById(employeeId);

        // Проверяем, что сотрудник не уволен
        EmploymentState state = EmploymentStates.from(
                current.getEmploymentStatus(),
                current.getIsActive()
        );

        if ("DISMISSED".equalsIgnoreCase(state.status())) {
            throw new IllegalStateException(
                    "Нельзя повысить уволенного сотрудника"
            );
        }

        // Получаем новую должность
        PositionResponse position =
                organizationService.getPosition(positionId);

        // Проверяем, что новая должность находится
        // в текущем подразделении сотрудника
        if (!position.departmentId().equals(current.getDepartmentId())) {
            throw new IllegalArgumentException(
                    "При повышении подразделение сотрудника не должно изменяться"
            );
        }

        // Сохраняем все прежние данные сотрудника
        EmployeeRequest request = toRequest(current);

        // Меняем только должность
        request.setPositionId(positionId);

        EmployeeResponse updated =
                employeeService.updateEmployee(employeeId, request);

        // Публикуем отдельное событие повышения
        publish(
                employeeId,
                "INCREASE",
                updated.getEmploymentStatus(),
                reason
        );

        return updated;
    }

}
