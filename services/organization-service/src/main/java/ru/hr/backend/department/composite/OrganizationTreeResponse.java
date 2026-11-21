package ru.hr.backend.department.composite;

import java.util.List;

public record OrganizationTreeResponse(String type, Long id, String name, List<OrganizationTreeResponse> children) {}
