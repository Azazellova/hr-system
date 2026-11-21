package ru.hr.backend.document.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public record HiringDocumentRequest(
        @NotNull Long employeeId,
        @NotNull Long eventId,
        @NotBlank String employeeFullName,
        @NotBlank String departmentName,
        @NotBlank String positionName,
        @NotNull LocalDate employmentDate,
        @NotNull @PositiveOrZero BigDecimal salary,
        @NotBlank String employerName,
        @NotBlank String contractNumber
) {}
