package ru.hr.backend.department.composite;

import java.util.List;

/** Общий компонент дерева организационной структуры (Composite). */
public interface OrganizationComponent {
    String getName();
    List<OrganizationComponent> getChildren();
    default void add(OrganizationComponent component) {
        throw new UnsupportedOperationException("Элемент не поддерживает добавление дочерних узлов");
    }
    default void remove(OrganizationComponent component) {
        throw new UnsupportedOperationException("Элемент не поддерживает удаление дочерних узлов");
    }
}
