package ru.hr.backend.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentRequest(
    @NotBlank(message = "Название подразделения обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    String name
) {}
