package ru.hr.backend.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hr.backend.department.entity.Position;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByDepartmentId(Long departmentId);
    boolean existsByNameIgnoreCaseAndDepartmentId(String name, Long departmentId);
    boolean existsByDepartmentId(Long departmentId);
    boolean existsByEmployeeId(Long employeeId);
}
