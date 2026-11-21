package ru.hr.backend.hr.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class TransferRequestCreate {

    @NotNull
    @Positive
    private Long employeeId;

    @NotNull
    @Positive
    private Long departmentId;

    @NotNull
    @Positive
    private Long positionId;

    @NotNull
    private LocalDate startDate;

    @NotBlank
    @Size(max = 500)
    private String reason;

    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public Long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public Long getPositionId() {
        return positionId;
    }
    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

}