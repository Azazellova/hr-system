package ru.hr.backend.employee.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.hr.backend.common.exception.ResourceNotFoundException;
import ru.hr.backend.employee.dto.EmployeeRequest;
import ru.hr.backend.employee.dto.EmployeeResponse;
import ru.hr.backend.employee.entity.Employee;
import ru.hr.backend.employee.repository.EmployeeRepository;

import java.util.List;
import java.util.Locale;

@Service
@Validated
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EmployeeResponse getEmployeeById(
            @NotNull(message = "ID сотрудника обязателен")
            @Positive(message = "ID сотрудника должен быть положительным")
            Long id
    ) {

        return toResponse(findEmployee(id));
    }

    @Transactional
    public EmployeeResponse createEmployee(
            @Valid EmployeeRequest request
    ) {

        normalize(request);

        Employee employee = new Employee();

        updateEntity(employee, request);

        if (employee.getIsActive() == null) {
            employee.setIsActive(true);
        }

        return toResponse(
                employeeRepository.save(employee)
        );
    }

    @Transactional
    public EmployeeResponse updateEmployee(
            @NotNull(message = "ID сотрудника обязателен")
            @Positive(message = "ID сотрудника должен быть положительным")
            Long id,

            @Valid EmployeeRequest request
    ) {

        normalize(request);

        Employee employee = findEmployee(id);

        updateEntity(employee, request);

        return toResponse(
                employeeRepository.save(employee)
        );
    }

    @Transactional
    public void deleteEmployee(
            @NotNull(message = "ID сотрудника обязателен")
            @Positive(message = "ID сотрудника должен быть положительным")
            Long id
    ) {

        Employee employee = findEmployee(id);

        employeeRepository.delete(employee);
    }

    public List<EmployeeResponse> searchByLastName(
            String lastName
    ) {

        String value = normalizeSearch(lastName);

        return employeeRepository
                .findByLastNameContainingIgnoreCase(value)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<EmployeeResponse> getByDepartment(
            @NotNull(message = "departmentId обязателен")
            @Positive(message = "departmentId должен быть положительным")
            Long departmentId
    ) {

        return employeeRepository
                .findByDepartmentId(departmentId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public EmployeeResponse changeStatus(
            @NotNull(message = "ID сотрудника обязателен")
            @Positive(message = "ID сотрудника должен быть положительным")
            Long id,

            @NotNull(message = "isActive обязателен")
            Boolean isActive
    ) {

        Employee employee = findEmployee(id);

        employee.setIsActive(isActive);

        return toResponse(
                employeeRepository.save(employee)
        );
    }

    private Employee findEmployee(Long id) {

        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Сотрудник с ID " + id + " не найден"
                        )
                );
    }

    private void updateEntity(
            Employee employee,
            EmployeeRequest request
    ) {

        employee.setLastName(request.getLastName());
        employee.setFirstName(request.getFirstName());
        employee.setMiddleName(request.getMiddleName());
        employee.setBirthDate(request.getBirthDate());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setAddress(request.getAddress());

        employee.setDepartmentId(
                request.getDepartmentId()
        );

        employee.setPositionId(
                request.getPositionId()
        );

        employee.setEmploymentDate(
                request.getEmploymentDate()
        );

        employee.setDismissalDate(
                request.getDismissalDate()
        );

        employee.setEmploymentStatus(
                request.getEmploymentStatus()
        );

        employee.setWorkSchedule(
                request.getWorkSchedule()
        );

        if (request.getIsActive() != null) {
            employee.setIsActive(
                    request.getIsActive()
            );
        }
    }

    private void normalize(EmployeeRequest request) {

        request.setLastName(
                normalizeRequired(request.getLastName())
        );

        request.setFirstName(
                normalizeRequired(request.getFirstName())
        );

        request.setMiddleName(
                normalizeOptional(request.getMiddleName())
        );

        request.setAddress(
                normalizeOptional(request.getAddress())
        );

        request.setEmploymentStatus(
                normalizeOptional(request.getEmploymentStatus())
        );

        request.setWorkSchedule(
                normalizeOptional(request.getWorkSchedule())
        );

        request.setEmail(
                normalizeEmail(request.getEmail())
        );

        request.setPhone(
                normalizePhone(request.getPhone())
        );
    }

    private String normalizeRequired(String value) {

        if (value == null) {
            return null;
        }

        return value
                .trim()
                .replaceAll("\\s+", " ");
    }

    private String normalizeOptional(String value) {

        if (value == null) {
            return null;
        }

        String normalized = value
                .trim()
                .replaceAll("\\s+", " ");

        return normalized.isBlank()
                ? null
                : normalized;
    }

    private String normalizeEmail(String value) {

        if (value == null) {
            return null;
        }

        String normalized = value
                .trim()
                .toLowerCase(Locale.ROOT);

        return normalized.isBlank()
                ? null
                : normalized;
    }

    private String normalizePhone(String value) {

        if (value == null) {
            return null;
        }

        String normalized = value
                .trim()
                .replaceAll("\\s+", " ");

        return normalized.isBlank()
                ? null
                : normalized;
    }

    private String normalizeSearch(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Поисковая строка не может быть пустой"
            );
        }

        return value
                .trim()
                .replaceAll("\\s+", " ");
    }

    private EmployeeResponse toResponse(
            Employee employee
    ) {

        EmployeeResponse response =
                new EmployeeResponse();

        response.setEmployeeId(
                employee.getEmployeeId()
        );

        response.setLastName(
                employee.getLastName()
        );

        response.setFirstName(
                employee.getFirstName()
        );

        response.setMiddleName(
                employee.getMiddleName()
        );

        response.setBirthDate(
                employee.getBirthDate()
        );

        response.setPhone(
                employee.getPhone()
        );

        response.setEmail(
                employee.getEmail()
        );

        response.setAddress(
                employee.getAddress()
        );

        response.setDepartmentId(
                employee.getDepartmentId()
        );

        response.setPositionId(
                employee.getPositionId()
        );

        response.setEmploymentDate(
                employee.getEmploymentDate()
        );

        response.setDismissalDate(
                employee.getDismissalDate()
        );

        response.setEmploymentStatus(
                employee.getEmploymentStatus()
        );

        response.setWorkSchedule(
                employee.getWorkSchedule()
        );

        response.setIsActive(
                employee.getIsActive()
        );

        return response;
    }
}