package ru.hr.backend.hr.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import ru.hr.backend.hr.dto.HrRequestCreate;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.service.HrRequestService;

import java.util.List;

@RestController
@RequestMapping("/hr/requests")
@Validated
public class HrRequestController {
    private final HrRequestService service;

    public HrRequestController(HrRequestService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HrRequest create(@Valid @RequestBody HrRequestCreate request) {
        return service.create(request);
    }

    @GetMapping
    public List<HrRequest> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public HrRequest getById(@PathVariable @Positive Long id) {
        return service.getById(id);
    }

    @GetMapping("/employee/{employeeId}")
    public List<HrRequest> getByEmployee(@PathVariable @Positive Long employeeId) {
        return service.getByEmployeeId(employeeId);
    }

    @PostMapping("/{id}/approve")
    public HrRequest approve(@PathVariable @Positive Long id) {
        return service.approve(id);
    }

    @PostMapping("/{id}/reject")
    public HrRequest reject(@PathVariable @Positive Long id) {
        return service.reject(id);
    }

}
