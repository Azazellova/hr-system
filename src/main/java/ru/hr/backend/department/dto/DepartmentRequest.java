package ru.hr.backend.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

public record DepartmentRequest(
    @NotBlank(message = "Название подразделения обязательно")
    @Size(max = 255, message = "Название не должно превышать 255 символов")
    String name,
    @Positive(message = "parentDepartmentId должен быть положительным")
    Long parentDepartmentId
) {
    public DepartmentRequest(String name) { this(name, null); }
}
