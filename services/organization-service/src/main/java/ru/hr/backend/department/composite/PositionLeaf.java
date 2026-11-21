package ru.hr.backend.department.composite;

import java.util.List;

/** Лист дерева: должность не содержит дочерних элементов. */
public class PositionLeaf implements OrganizationComponent {
    private final Long id;
    private final String name;
    public PositionLeaf(Long id, String name) { this.id = id; this.name = name; }
    public Long getId() { return id; }
    @Override public String getName() { return name; }
    @Override public List<OrganizationComponent> getChildren() { return List.of(); }
}
