package ru.hr.backend.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hr.backend.employee.entity.Worktime;

import java.util.List;

public interface WorktimeRepository extends JpaRepository<Worktime, Long> {

    List<Worktime> findByEmployeeId(Long employeeId);

    List<Worktime> findByPeriod(String period);
}