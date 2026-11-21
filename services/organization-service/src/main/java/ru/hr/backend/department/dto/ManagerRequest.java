package ru.hr.backend.department.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ManagerRequest(
    @NotNull(message = "managerId обязателен")
    @Positive(message = "managerId должен быть положительным числом")
    Long managerId
) {}
