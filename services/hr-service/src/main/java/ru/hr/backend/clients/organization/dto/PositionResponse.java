package ru.hr.backend.clients.organization.dto;

public record PositionResponse(
    Long id,
    String name,
    Long departmentId
) {}
