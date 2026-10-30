
package ru.hr.backend.hr.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import ru.hr.backend.hr.dto.HrRequestCreate;
import ru.hr.backend.hr.dto.HiringRequestCreate;
import ru.hr.backend.hr.dto.TransferRequestCreate;
import ru.hr.backend.hr.dto.IncreaseRequestCreate;
import ru.hr.backend.hr.dto.DismissalRequestCreate;
import ru.hr.backend.hr.dto.VacationRequestCreate;
import ru.hr.backend.hr.dto.SickLeaveRequestCreate;

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

    // Старый endpoint оставляем для обратной совместимости
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HrRequest create(@Valid @RequestBody HrRequestCreate request) {
        return service.create(request);
    }

    // Приём сотрудника
    @PostMapping("/hiring")
    @ResponseStatus(HttpStatus.CREATED)
    public HrRequest createHiring(
            @Valid @RequestBody HiringRequestCreate request
    ) {
        return service.createHiring(request);
    }

    // Перевод сотрудника
    @PostMapping("/transfers")
    @ResponseStatus(HttpStatus.CREATED)
    public HrRequest createTransfer(
            @Valid @RequestBody TransferRequestCreate request
    ) {
        return service.createTransfer(request);
    }

    // Повышение сотрудника
    @PostMapping("/increases")
    @ResponseStatus(HttpStatus.CREATED)
    public HrRequest createIncrease(
            @Valid @RequestBody IncreaseRequestCreate request
    ) {
        return service.createIncrease(request);
    }

    // Увольнение сотрудника
    @PostMapping("/dismissals")
    @ResponseStatus(HttpStatus.CREATED)
    public HrRequest createDismissal(
            @Valid @RequestBody DismissalRequestCreate request
    ) {
        return service.createDismissal(request);
    }

    // Отпуск
    @PostMapping("/vacations")
    @ResponseStatus(HttpStatus.CREATED)
    public HrRequest createVacation(
            @Valid @RequestBody VacationRequestCreate request
    ) {
        return service.createVacation(request);
    }

    // Больничный
    @PostMapping("/sick-leaves")
    @ResponseStatus(HttpStatus.CREATED)
    public HrRequest createSickLeave(
            @Valid @RequestBody SickLeaveRequestCreate request
    ) {
        return service.createSickLeave(request);
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
    public List<HrRequest> getByEmployee(
            @PathVariable @Positive Long employeeId
    ) {
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
