package ru.hr.backend.clients.organization.dto;

public record DepartmentResponse(Long id, String name, Long managerId, Long parentDepartmentId) {
    public DepartmentResponse(Long id, String name, Long managerId) {
        this(id, name, managerId, null);
    }
}
