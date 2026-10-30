package ru.hr.backend.department.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hr.backend.department.dto.*;
import ru.hr.backend.department.entity.Department;
import ru.hr.backend.department.entity.Position;
import ru.hr.backend.department.exception.ResourceNotFoundException;
import ru.hr.backend.department.repository.DepartmentRepository;
import ru.hr.backend.department.repository.PositionRepository;

import java.util.List;

@Service
@Transactional
public class OrganizationService {
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public OrganizationService(
        DepartmentRepository departmentRepository,
        PositionRepository positionRepository
    ) {
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    // -------------------- DEPARTMENTS: CRUD --------------------

    public DepartmentResponse createDepartment(DepartmentRequest request) {
        String name = request.name().trim();
        if (departmentRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Подразделение с таким названием уже существует");
        }
        return toDepartmentResponse(departmentRepository.save(new Department(name)));
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartments() {
        return departmentRepository.findAll().stream()
            .map(this::toDepartmentResponse).toList();
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(Long id) {
        return toDepartmentResponse(requireDepartment(id));
    }

    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = requireDepartment(id);
        String name = request.name().trim();

        departmentRepository.findByNameIgnoreCase(name)
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new IllegalArgumentException("Подразделение с таким названием уже существует");
            });

        department.setName(name);
        return toDepartmentResponse(departmentRepository.save(department));
    }

    public void deleteDepartment(Long id) {
        Department department = requireDepartment(id);
        if (positionRepository.existsByDepartmentId(id)) {
            throw new IllegalStateException(
                "Нельзя удалить подразделение, пока в нем существуют должности"
            );
        }
        departmentRepository.delete(department);
    }

    // Назначение/замена руководителя. Проверка существования сотрудника
    // в Employee Service требует межсервисного HTTP-вызова; здесь хранится его ID.
    public DepartmentResponse assignManager(Long departmentId, ManagerRequest request) {
        Department department = requireDepartment(departmentId);
        department.setManagerId(request.managerId());
        return toDepartmentResponse(departmentRepository.save(department));
    }

    public DepartmentResponse removeManager(Long departmentId) {
        Department department = requireDepartment(departmentId);
        department.setManagerId(null);
        return toDepartmentResponse(departmentRepository.save(department));
    }

    // -------------------- POSITIONS: CRUD --------------------

    public PositionResponse createPosition(PositionRequest request) {
        requireDepartment(request.departmentId());
        String name = request.name().trim();

        if (positionRepository.existsByNameIgnoreCaseAndDepartmentId(
            name, request.departmentId()
        )) {
            throw new IllegalArgumentException(
                "Такая должность уже существует в этом подразделении"
            );
        }

        Position position = new Position(
            name, request.departmentId(), request.employeeId()
        );
        return toPositionResponse(positionRepository.save(position));
    }

    @Transactional(readOnly = true)
    public List<PositionResponse> getPositions(Long departmentId) {
        if (departmentId != null) {
            requireDepartment(departmentId);
            return positionRepository.findByDepartmentId(departmentId).stream()
                .map(this::toPositionResponse).toList();
        }
        return positionRepository.findAll().stream()
            .map(this::toPositionResponse).toList();
    }

    @Transactional(readOnly = true)
    public PositionResponse getPosition(Long id) {
        return toPositionResponse(requirePosition(id));
    }

    public PositionResponse updatePosition(Long id, PositionRequest request) {
        Position position = requirePosition(id);
        requireDepartment(request.departmentId());
        String name = request.name().trim();

        if (positionRepository.existsByNameIgnoreCaseAndDepartmentId(
            name, request.departmentId()
        ) && !(position.getName().equalsIgnoreCase(name)
            && position.getDepartmentId().equals(request.departmentId()))) {
            throw new IllegalArgumentException(
                "Такая должность уже существует в этом подразделении"
            );
        }

        position.setName(name);
        position.setDepartmentId(request.departmentId());
        position.setEmployeeId(request.employeeId());
        return toPositionResponse(positionRepository.save(position));
    }

    public void deletePosition(Long id) {
        Position position = requirePosition(id);
        positionRepository.delete(position);
    }

    private Department requireDepartment(Long id) {
        return departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Подразделение с ID " + id + " не найдено"
            ));
    }

    private Position requirePosition(Long id) {
        return positionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Должность с ID " + id + " не найдена"
            ));
    }

    private DepartmentResponse toDepartmentResponse(Department d) {
        return new DepartmentResponse(d.getId(), d.getName(), d.getManagerId());
    }

    private PositionResponse toPositionResponse(Position p) {
        return new PositionResponse(
            p.getId(), p.getName(), p.getDepartmentId(), p.getEmployeeId()
        );
    }
}
