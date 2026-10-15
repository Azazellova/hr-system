package ru.hr.backend.department.dto;

public record PositionResponse(
    Long id,
    String name,
    Long departmentId,
    Long employeeId
) {}
