package ru.hr.backend.department.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hr.backend.department.dto.*;
import ru.hr.backend.department.service.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final OrganizationService service;

    public DepartmentController(OrganizationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse create(@Valid @RequestBody DepartmentRequest request) {
        return service.createDepartment(request);
    }

    @GetMapping
    public List<DepartmentResponse> getAll() {
        return service.getDepartments();
    }

    @GetMapping("/{id}")
    public DepartmentResponse getById(@PathVariable Long id) {
        return service.getDepartment(id);
    }

    @PutMapping("/{id}")
    public DepartmentResponse update(
        @PathVariable Long id,
        @Valid @RequestBody DepartmentRequest request
    ) {
        return service.updateDepartment(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteDepartment(id);
    }

    @PutMapping("/{id}/manager")
    public DepartmentResponse assignManager(
        @PathVariable Long id,
        @Valid @RequestBody ManagerRequest request
    ) {
        return service.assignManager(id, request);
    }

    @DeleteMapping("/{id}/manager")
    public DepartmentResponse removeManager(@PathVariable Long id) {
        return service.removeManager(id);
    }
}
