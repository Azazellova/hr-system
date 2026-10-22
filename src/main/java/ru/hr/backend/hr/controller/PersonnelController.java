package ru.hr.backend.hr.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.hr.backend.employee.dto.EmployeeResponse;
import ru.hr.backend.hr.dto.PersonnelActionRequest;
import ru.hr.backend.hr.service.PersonnelService;

@RestController
@RequestMapping("/hr/employees")
@Validated
public class PersonnelController {
    private final PersonnelService personnelService;

    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<EmployeeResponse> activate(
            @PathVariable @Positive(message = "ID сотрудника должен быть положительным") Long id,
            @Valid @RequestBody PersonnelActionRequest request) {
        return ResponseEntity.ok(personnelService.activate(id, request.getReason().trim()));
    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<EmployeeResponse> suspend(
            @PathVariable @Positive(message = "ID сотрудника должен быть положительным") Long id,
            @Valid @RequestBody PersonnelActionRequest request) {
        return ResponseEntity.ok(personnelService.suspend(id, request.getReason().trim()));
    }

    @PostMapping("/{id}/dismiss")
    public ResponseEntity<EmployeeResponse> dismiss(
            @PathVariable @Positive(message = "ID сотрудника должен быть положительным") Long id,
            @Valid @RequestBody PersonnelActionRequest request) {
        return ResponseEntity.ok(personnelService.dismiss(id, request.getReason().trim()));
    }
}
