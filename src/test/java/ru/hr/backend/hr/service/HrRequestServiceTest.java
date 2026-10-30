package ru.hr.backend.hr.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hr.backend.department.service.OrganizationService;
import ru.hr.backend.employee.service.EmployeeService;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestStatus;
import ru.hr.backend.hr.entity.HrRequestType;
import ru.hr.backend.hr.repository.HrRequestRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HrRequestServiceTest {
    private HrRequestRepository repository;
    private EmployeeService employeeService;
    private OrganizationService organizationService;
    private PersonnelService personnelService;
    private HrRequestService service;

    @BeforeEach
    void setUp() {
        repository = mock(HrRequestRepository.class);
        employeeService = mock(EmployeeService.class);
        organizationService = mock(OrganizationService.class);
        personnelService = mock(PersonnelService.class);
        service = new HrRequestService(repository, employeeService, organizationService, personnelService);
    }

    @Test
    void approvingTransferUsesPersonnelFacadeAndCompletesRequest() {
        HrRequest request = request(HrRequestType.TRANSFER);
        request.setDepartmentId(20L);
        request.setPositionId(30L);
        when(repository.findById(1L)).thenReturn(Optional.of(request));
        when(repository.save(request)).thenReturn(request);

        HrRequest result = service.approve(1L);

        verify(personnelService).transfer(10L, 20L, 30L, "Перевод");
        assertEquals(HrRequestStatus.COMPLETED, result.getStatus());
    }

    @Test
    void approvingDismissalUsesPersonnelFacadeAndCompletesRequest() {
        HrRequest request = request(HrRequestType.DISMISSAL);
        when(repository.findById(1L)).thenReturn(Optional.of(request));
        when(repository.save(request)).thenReturn(request);

        HrRequest result = service.approve(1L);

        verify(personnelService).dismiss(10L, "Перевод");
        assertEquals(HrRequestStatus.COMPLETED, result.getStatus());
    }

    @Test
    void approvingVacationCompletesRequestAndKeepsDates() {
        HrRequest request = request(HrRequestType.VACATION);
        LocalDate start = LocalDate.of(2026, 11, 1);
        LocalDate end = LocalDate.of(2026, 11, 14);
        request.setStartDate(start);
        request.setEndDate(end);
        when(repository.findById(1L)).thenReturn(Optional.of(request));
        when(repository.save(request)).thenReturn(request);

        HrRequest result = service.approve(1L);

        assertEquals(HrRequestStatus.COMPLETED, result.getStatus());
        assertEquals(start, result.getStartDate());
        assertEquals(end, result.getEndDate());
        verifyNoInteractions(personnelService);
    }

    @Test
    void approvingIncreaseLeavesRequestApprovedUntilSalaryModelExists() {
        HrRequest request = request(HrRequestType.INCREASE);
        when(repository.findById(1L)).thenReturn(Optional.of(request));
        when(repository.save(request)).thenReturn(request);

        HrRequest result = service.approve(1L);

        assertEquals(HrRequestStatus.APPROVED, result.getStatus());
        verifyNoInteractions(personnelService);
    }

    @Test
    void cannotApproveRequestTwice() {
        HrRequest request = request(HrRequestType.VACATION);
        request.setStatus(HrRequestStatus.COMPLETED);
        when(repository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(IllegalStateException.class, () -> service.approve(1L));
        verify(repository, never()).save(any());
    }

    private HrRequest request(HrRequestType type) {
        HrRequest request = new HrRequest();
        request.setId(1L);
        request.setType(type);
        request.setEmployeeId(10L);
        request.setReason("Перевод");
        request.setStatus(HrRequestStatus.PENDING);
        return request;
    }
}
