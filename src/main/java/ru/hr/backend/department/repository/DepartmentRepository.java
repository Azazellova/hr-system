package ru.hr.backend.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hr.backend.department.entity.Department;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Department> findByNameIgnoreCase(String name);
}
