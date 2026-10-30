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
import java.util.HashSet;
import java.util.Set;
import ru.hr.backend.department.composite.OrganizationTreeResponse;
import ru.hr.backend.department.composite.OrganizationComponent;
import ru.hr.backend.department.composite.DepartmentComposite;
import ru.hr.backend.department.composite.PositionLeaf;

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
        Department department = new Department(name);
        if (request.parentDepartmentId() != null) {
            department.setParentDepartment(requireDepartment(request.parentDepartmentId()));
        }
        return toDepartmentResponse(departmentRepository.save(department));
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
        if (request.parentDepartmentId() != null) {
            if (request.parentDepartmentId().equals(id)) {
                throw new IllegalArgumentException("Подразделение не может быть родителем самого себя");
            }
            Department newParent = requireDepartment(request.parentDepartmentId());
            if (isDescendant(newParent, id)) {
                throw new IllegalArgumentException("Нельзя переместить подразделение внутрь собственного потомка");
            }
            department.setParentDepartment(newParent);
        } else {
            department.setParentDepartment(null);
        }
        return toDepartmentResponse(departmentRepository.save(department));
    }

    public void deleteDepartment(Long id) {
        Department department = requireDepartment(id);
        if (positionRepository.existsByDepartmentId(id) || departmentRepository.existsByParentDepartment_Id(id)) {
            throw new IllegalStateException(
                "Нельзя удалить подразделение: сначала переместите или удалите его должности и дочерние подразделения"
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

        Position position = new Position(name, request.departmentId());
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
        return toPositionResponse(positionRepository.save(position));
    }

    public void deletePosition(Long id) {
        Position position = requirePosition(id);
        positionRepository.delete(position);
    }

    @Transactional(readOnly = true)
    public List<OrganizationTreeResponse> getOrganizationTree() {
        List<Department> roots = departmentRepository.findByParentDepartmentIsNullOrderByNameAsc();
        return roots.stream()
                .map(root -> buildComposite(root, new HashSet<>()))
                .map(this::toTreeResponse)
                .toList();
    }

    private DepartmentComposite buildComposite(Department department, Set<Long> path) {
        if (!path.add(department.getId())) {
            throw new IllegalStateException("Обнаружен цикл в организационной структуре: " + department.getId());
        }
        DepartmentComposite composite = new DepartmentComposite(department.getId(), department.getName());
        departmentRepository.findByParentDepartment_IdOrderByNameAsc(department.getId()).forEach(child ->
                composite.add(buildComposite(child, new HashSet<>(path))));
        positionRepository.findByDepartmentId(department.getId()).stream()
                .sorted(java.util.Comparator.comparing(Position::getName, String.CASE_INSENSITIVE_ORDER))
                .forEach(position -> composite.add(new PositionLeaf(position.getId(), position.getName())));
        return composite;
    }

    private OrganizationTreeResponse toTreeResponse(OrganizationComponent component) {
        Long id;
        String type;
        if (component instanceof DepartmentComposite department) {
            id = department.getId();
            type = "DEPARTMENT";
        } else if (component instanceof PositionLeaf position) {
            id = position.getId();
            type = "POSITION";
        } else {
            throw new IllegalStateException("Неизвестный элемент организационной структуры");
        }
        return new OrganizationTreeResponse(type, id, component.getName(),
                component.getChildren().stream().map(this::toTreeResponse).toList());
    }

    /** Проверяет, находится ли предполагаемый родитель внутри поддерева перемещаемого отдела. */
    private boolean isDescendant(Department candidate, Long departmentId) {
        Department current = candidate;
        Set<Long> visited = new HashSet<>();
        while (current != null) {
            if (!visited.add(current.getId())) {
                throw new IllegalStateException("В существующей иерархии обнаружен цикл");
            }
            if (current.getId().equals(departmentId)) return true;
            current = current.getParentDepartment();
        }
        return false;
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
        return new DepartmentResponse(d.getId(), d.getName(), d.getManagerId(), d.getParentDepartmentId());
    }

    private PositionResponse toPositionResponse(Position p) {
        return new PositionResponse(
            p.getId(), p.getName(), p.getDepartmentId()
        );
    }
}
