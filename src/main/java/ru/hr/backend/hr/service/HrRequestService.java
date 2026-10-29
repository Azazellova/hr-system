package ru.hr.backend.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hr.backend.common.exception.ResourceNotFoundException;
import ru.hr.backend.department.dto.DepartmentResponse;
import ru.hr.backend.department.dto.PositionResponse;
import ru.hr.backend.department.service.OrganizationService;
import ru.hr.backend.employee.dto.EmployeeRequest;
import ru.hr.backend.employee.dto.EmployeeResponse;
import ru.hr.backend.employee.service.EmployeeService;
import ru.hr.backend.hr.dto.HrRequestCreate;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestStatus;
import ru.hr.backend.hr.entity.HrRequestType;
import ru.hr.backend.hr.repository.HrRequestRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class HrRequestService {

    private final HrRequestRepository repository;
    private final EmployeeService employeeService;
    private final OrganizationService organizationService;
    private final PersonnelService personnelService;

    public HrRequestService(
            HrRequestRepository repository,
            EmployeeService employeeService,
            OrganizationService organizationService,
            PersonnelService personnelService
    ) {
        this.repository = repository;
        this.employeeService = employeeService;
        this.organizationService = organizationService;
        this.personnelService = personnelService;
    }

    @Transactional
    public HrRequest create(HrRequestCreate input) {
        validate(input);

        if (input.getType() != HrRequestType.HIRING && input.getEmployeeId() != null) {
            employeeService.getEmployeeById(input.getEmployeeId());
        }

        if (input.getType() == HrRequestType.HIRING
                || input.getType() == HrRequestType.TRANSFER) {

            DepartmentResponse department =
                    organizationService.getDepartment(input.getDepartmentId());

            PositionResponse position =
                    organizationService.getPosition(input.getPositionId());

            if (!position.departmentId().equals(department.id())) {
                throw new IllegalArgumentException(
                        "Указанная должность не относится к выбранному подразделению"
                );
            }
        }

        HrRequest request = new HrRequest();
        request.setType(input.getType());
        request.setEmployeeId(input.getEmployeeId());
        request.setDepartmentId(input.getDepartmentId());
        request.setPositionId(input.getPositionId());
        if (input.getType() == HrRequestType.HIRING) {
            request.setLastName(input.getLastName().trim());
            request.setFirstName(input.getFirstName().trim());
            request.setMiddleName(cleanOptional(input.getMiddleName()));
            request.setBirthDate(input.getBirthDate());
            request.setPhone(input.getPhone().trim());
            request.setEmail(cleanOptional(input.getEmail()));
            request.setAddress(input.getAddress().trim());
            request.setWorkSchedule(cleanOptional(input.getWorkSchedule()));
            if (request.getStartDate() == null) {
                request.setStartDate(LocalDate.now());
            }
        }
        request.setReason(input.getReason().trim());
        request.setStartDate(input.getStartDate());
        request.setEndDate(input.getEndDate());
        request.setStatus(HrRequestStatus.PENDING);

        return repository.save(request);
    }

    @Transactional(readOnly = true)
    public List<HrRequest> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public HrRequest getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Кадровая заявка с ID " + id + " не найдена"
                ));
    }

    @Transactional(readOnly = true)
    public List<HrRequest> getByEmployeeId(Long employeeId) {
        employeeService.getEmployeeById(employeeId);
        return repository.findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    @Transactional
    public HrRequest approve(Long id) {
        HrRequest request = getById(id);
        checkPending(request);

        switch (request.getType()) {
            case HIRING:
                hireEmployee(request);
                request.setStatus(HrRequestStatus.COMPLETED);
                break;

            case TRANSFER:
                personnelService.transfer(request.getEmployeeId(), request.getDepartmentId(),
                        request.getPositionId(), request.getReason());
                request.setStatus(HrRequestStatus.COMPLETED);
                break;

            case DISMISSAL:
                personnelService.dismiss(request.getEmployeeId(), request.getReason());
                request.setStatus(HrRequestStatus.COMPLETED);
                break;

            case VACATION:
            case SICK_LEAVE:
                // Сама заявка с датами является записью об отсутствии в hr_requests.
                request.setStatus(HrRequestStatus.COMPLETED);
                break;

            case INCREASE:
                // В модели сотрудника нет поля оклада, поэтому размер повышения хранить негде.
                request.setStatus(HrRequestStatus.APPROVED);
                break;
        }

        return repository.save(request);
    }

    @Transactional
    public HrRequest reject(Long id) {
        HrRequest request = getById(id);
        checkPending(request);

        request.setStatus(HrRequestStatus.REJECTED);

        return repository.save(request);
    }

    private void hireEmployee(HrRequest request) {
        EmployeeRequest employee = new EmployeeRequest();
        employee.setLastName(request.getLastName());
        employee.setFirstName(request.getFirstName());
        employee.setMiddleName(request.getMiddleName());
        employee.setBirthDate(request.getBirthDate());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setAddress(request.getAddress());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setPositionId(request.getPositionId());
        employee.setEmploymentDate(request.getStartDate() == null
                ? LocalDate.now() : request.getStartDate());
        employee.setWorkSchedule(request.getWorkSchedule());
        employee.setIsActive(true);

        EmployeeResponse created = employeeService.createEmployee(employee);
        request.setEmployeeId(created.getEmployeeId());
    }

    private String cleanOptional(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private void checkPending(HrRequest request) {
        if (request.getStatus() != HrRequestStatus.PENDING) {
            throw new IllegalStateException(
                    "Обработать можно только заявку со статусом PENDING"
            );
        }
    }

    private void validate(HrRequestCreate input) {
        if (input.getType() == null) {
            throw new IllegalArgumentException("Тип кадровой заявки обязателен");
        }

        HrRequestType type = input.getType();
        boolean hiring = type == HrRequestType.HIRING;

        if (!hiring && input.getEmployeeId() == null) {
            throw new IllegalArgumentException(
                    "Для этой заявки необходимо указать employeeId"
            );
        }

        if ((type == HrRequestType.HIRING || type == HrRequestType.TRANSFER)
                && (input.getDepartmentId() == null
                || input.getPositionId() == null)) {
            throw new IllegalArgumentException(
                    "Для приема и перевода необходимо указать departmentId и positionId"
            );
        }

        if (input.getReason() == null || input.getReason().isBlank()) {
            throw new IllegalArgumentException("Основание заявки обязательно");
        }

        if (input.getReason().length() > 500) {
            throw new IllegalArgumentException(
                    "Основание не должно превышать 500 символов"
            );
        }

        if (input.getStartDate() != null && input.getEndDate() != null
                && input.getEndDate().isBefore(input.getStartDate())) {
            throw new IllegalArgumentException(
                    "endDate не может быть раньше startDate"
            );
        }

        if (hiring) {
            if (input.getLastName() == null || input.getLastName().isBlank()
                    || input.getFirstName() == null || input.getFirstName().isBlank()) {
                throw new IllegalArgumentException("Для приема необходимо указать фамилию и имя кандидата");
            }
            if (input.getBirthDate() == null || !input.getBirthDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Дата рождения кандидата должна быть в прошлом");
            }
            if (input.getPhone() == null || input.getPhone().isBlank()) {
                throw new IllegalArgumentException("Для приема необходимо указать телефон кандидата");
            }
            if (input.getAddress() == null || input.getAddress().isBlank()) {
                throw new IllegalArgumentException("Для приема необходимо указать адрес кандидата");
            }
            if (input.getStartDate() != null && input.getStartDate().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Дата приема не может быть в будущем");
            }
        }

        if ((type == HrRequestType.VACATION
                || type == HrRequestType.SICK_LEAVE)
                && (input.getStartDate() == null
                || input.getEndDate() == null)) {
            throw new IllegalArgumentException(
                    "Для отпуска и больничного необходимо указать startDate и endDate"
            );
        }
    }
}
