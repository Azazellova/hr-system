package ru.hr.backend.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hr.backend.hr.entity.HrRequest;

import java.util.List;

public interface HrRequestRepository extends JpaRepository<HrRequest, Long> {
    List<HrRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
}
