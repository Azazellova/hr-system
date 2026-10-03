package ru.hr.backend.employee.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hr.backend.employee.dto.EmployeeRequest;
import ru.hr.backend.employee.dto.EmployeeResponse;
import ru.hr.backend.employee.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        return ResponseEntity.ok(
                employeeService.getAllEmployees()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchByLastName(

            @RequestParam

            @Size(
                    min = 2,
                    max = 100,
                    message = "Фамилия для поиска должна содержать от 2 до 100 символов"
            )

            @Pattern(
                    regexp = "^[\\p{L}\\p{M}][\\p{L}\\p{M}'’\\- ]*[\\p{L}\\p{M}]$",
                    message = "Поисковая строка содержит недопустимые символы"
            )

            String lastName
    ) {

        return ResponseEntity.ok(
                employeeService.searchByLastName(lastName)
        );
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> getByDepartment(

            @PathVariable

            @Positive(
                    message = "departmentId должен быть положительным"
            )

            Long departmentId
    ) {

        return ResponseEntity.ok(
                employeeService.getByDepartment(departmentId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(

            @PathVariable

            @Positive(
                    message = "ID сотрудника должен быть положительным"
            )

            Long id
    ) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id)
        );
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(

            @Valid
            @RequestBody
            EmployeeRequest request
    ) {

        return ResponseEntity.ok(
                employeeService.createEmployee(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(

            @PathVariable

            @Positive(
                    message = "ID сотрудника должен быть положительным"
            )

            Long id,

            @Valid
            @RequestBody
            EmployeeRequest request
    ) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(

            @PathVariable

            @Positive(
                    message = "ID сотрудника должен быть положительным"
            )

            Long id
    ) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EmployeeResponse> changeStatus(

            @PathVariable

            @Positive(
                    message = "ID сотрудника должен быть положительным"
            )

            Long id,

            @RequestParam
            Boolean isActive
    ) {

        return ResponseEntity.ok(
                employeeService.changeStatus(
                        id,
                        isActive
                )
        );
    }
}