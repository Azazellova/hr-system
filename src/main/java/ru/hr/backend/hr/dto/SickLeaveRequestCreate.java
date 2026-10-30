
package ru.hr.backend.hr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class SickLeaveRequestCreate {

    @NotNull(message = "ID сотрудника обязателен")
    @Positive(message = "ID сотрудника должен быть положительным")
    private Long employeeId;

    @NotNull(message = "Дата начала больничного обязательна")
    private LocalDate startDate;

    @NotNull(message = "Дата окончания больничного обязательна")
    private LocalDate endDate;

    @NotBlank(message = "Основание больничного обязательно")
    @Size(max = 500, message = "Основание не должно превышать 500 символов")
    private String reason;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
