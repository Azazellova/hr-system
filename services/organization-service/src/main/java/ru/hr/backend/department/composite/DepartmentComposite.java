package ru.hr.backend.department.composite;

import java.util.ArrayList;
import java.util.List;

/** Компоновщик: подразделение может содержать другие подразделения и должности. */
public class DepartmentComposite implements OrganizationComponent {
    private final Long id;
    private final String name;
    private final List<OrganizationComponent> children = new ArrayList<>();
    public DepartmentComposite(Long id, String name) { this.id = id; this.name = name; }
    public Long getId() { return id; }
    @Override public String getName() { return name; }
    @Override public List<OrganizationComponent> getChildren() { return List.copyOf(children); }
    @Override public void add(OrganizationComponent component) { children.add(component); }
    @Override public void remove(OrganizationComponent component) { children.remove(component); }
}
