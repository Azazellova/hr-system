package ru.hr.backend.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PositionRequest(
    @NotBlank(message = "Название должности обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    String name,

    @NotNull(message = "departmentId обязателен")
    @Positive(message = "departmentId должен быть положительным числом")
    Long departmentId,

    @Positive(message = "employeeId должен быть положительным числом")
    Long employeeId
) {}
