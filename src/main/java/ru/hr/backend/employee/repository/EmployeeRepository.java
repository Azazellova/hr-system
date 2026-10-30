package ru.hr.backend.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hr.backend.employee.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByLastNameContainingIgnoreCase(String lastName);

    List<Employee> findByFirstNameContainingIgnoreCase(String firstName);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByPositionId(Long positionId);

    List<Employee> findByIsActive(Boolean isActive);

    List<Employee> findByEmploymentStatusIgnoreCase(String employmentStatus);
}