package ru.hr.backend.hr.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import ru.hr.backend.department.dto.DepartmentResponse;
import ru.hr.backend.department.dto.PositionResponse;
import ru.hr.backend.department.service.OrganizationService;
import ru.hr.backend.employee.dto.EmployeeRequest;
import ru.hr.backend.employee.dto.EmployeeResponse;
import ru.hr.backend.employee.service.EmployeeService;
import ru.hr.backend.hr.event.PersonnelEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PersonnelServiceTest {
    private EmployeeService employeeService;
    private ApplicationEventPublisher eventPublisher;
    private OrganizationService organizationService;
    private PersonnelService service;

    @BeforeEach
    void setUp() {
        employeeService = mock(EmployeeService.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        organizationService = mock(OrganizationService.class);
        service = new PersonnelService(employeeService, eventPublisher, organizationService);
    }

    @Test
    void dismissUpdatesEmployeeAndPublishesEvent() {
        EmployeeResponse current = employee(5L, "ACTIVE", true);
        EmployeeResponse updated = employee(5L, "DISMISSED", false);
        when(employeeService.getEmployeeById(5L)).thenReturn(current);
        when(employeeService.updateEmployee(eq(5L), any(EmployeeRequest.class))).thenReturn(updated);

        EmployeeResponse result = service.dismiss(5L, "Увольнение по заявлению");

        assertEquals("DISMISSED", result.getEmploymentStatus());
        verify(employeeService).updateEmployee(eq(5L), argThat(request ->
                "DISMISSED".equals(request.getEmploymentStatus())
                        && Boolean.FALSE.equals(request.getIsActive())
                        && request.getDismissalDate() != null));
        verify(eventPublisher).publishEvent(
                argThat((Object event) -> {
                    if (!(event instanceof PersonnelEvent)) {
                        return false;
                    }

                    PersonnelEvent personnelEvent = (PersonnelEvent) event;

                    return personnelEvent.getEmployeeId().equals(5L)
                            && "DISMISS".equals(personnelEvent.getAction())
                            && "Увольнение по заявлению".equals(personnelEvent.getReason());
                })
        );
    }

    @Test
    void dismissedEmployeeCannotBeActivated() {
        when(employeeService.getEmployeeById(5L)).thenReturn(employee(5L, "DISMISSED", false));

        assertThrows(IllegalStateException.class, () -> service.activate(5L, "Повторный прием"));
        verify(employeeService, never()).updateEmployee(any(), any());
        verify(eventPublisher, never()).publishEvent(any(Object.class));
    }

    @Test
    void transferRejectsPositionFromAnotherDepartment() {
        when(employeeService.getEmployeeById(5L)).thenReturn(employee(5L, "ACTIVE", true));
        when(organizationService.getDepartment(2L)).thenReturn(new DepartmentResponse(2L, "IT", null));
        when(organizationService.getPosition(7L)).thenReturn(new PositionResponse(7L, "Manager", 3L, null));

        assertThrows(IllegalArgumentException.class,
                () -> service.transfer(5L, 2L, 7L, "Перевод"));
        verify(employeeService, never()).updateEmployee(any(), any());
        verify(eventPublisher, never()).publishEvent(any(Object.class));
    }

    private EmployeeResponse employee(Long id, String status, Boolean active) {
        EmployeeResponse employee = new EmployeeResponse();
        employee.setEmployeeId(id);
        employee.setLastName("Иванов");
        employee.setFirstName("Иван");
        employee.setEmploymentStatus(status);
        employee.setIsActive(active);
        employee.setDepartmentId(2L);
        employee.setPositionId(7L);
        return employee;
    }
}
