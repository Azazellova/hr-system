package ru.hr.backend.department.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hr.backend.department.dto.PositionRequest;
import ru.hr.backend.department.dto.PositionResponse;
import ru.hr.backend.department.service.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {
    private final OrganizationService service;

    public PositionController(OrganizationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PositionResponse create(@Valid @RequestBody PositionRequest request) {
        return service.createPosition(request);
    }

    @GetMapping
    public List<PositionResponse> getAll(
        @RequestParam(required = false) Long departmentId
    ) {
        return service.getPositions(departmentId);
    }

    @GetMapping("/{id}")
    public PositionResponse getById(@PathVariable Long id) {
        return service.getPosition(id);
    }

    @PutMapping("/{id}")
    public PositionResponse update(
        @PathVariable Long id,
        @Valid @RequestBody PositionRequest request
    ) {
        return service.updatePosition(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deletePosition(id);
    }
}
