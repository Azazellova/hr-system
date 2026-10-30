package ru.hr.backend.hr.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hr.backend.common.exception.ResourceNotFoundException;
import ru.hr.backend.department.dto.DepartmentResponse;
import ru.hr.backend.department.dto.PositionResponse;
import ru.hr.backend.department.service.OrganizationService;
import ru.hr.backend.employee.service.EmployeeService;
import ru.hr.backend.hr.dto.HrRequestCreate;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestStatus;
import ru.hr.backend.hr.entity.HrRequestType;
import ru.hr.backend.hr.repository.HrRequestRepository;
import ru.hr.backend.hr.strategy.HrRequestStrategy;
import ru.hr.backend.hr.strategy.HrRequestStrategyFactory;
import java.time.LocalDate;
import java.util.List;
import ru.hr.backend.hr.dto.HiringRequestCreate;
import ru.hr.backend.hr.dto.TransferRequestCreate;
import ru.hr.backend.hr.dto.IncreaseRequestCreate;
import ru.hr.backend.hr.dto.DismissalRequestCreate;
import ru.hr.backend.hr.dto.VacationRequestCreate;
import ru.hr.backend.hr.dto.SickLeaveRequestCreate;
@Service
public class HrRequestService {

    private final HrRequestRepository repository;
    private final EmployeeService employeeService;
    private final OrganizationService organizationService;
    private final PersonnelService personnelService;
    private final HrRequestStrategyFactory strategyFactory;
    private final HiringDocumentCoordinator hiringDocuments;

    public HrRequestService(
            HrRequestRepository repository,
            EmployeeService employeeService,
            OrganizationService organizationService,
            PersonnelService personnelService,
            HrRequestStrategyFactory strategyFactory,
            HiringDocumentCoordinator hiringDocuments
    ) {
        this.repository = repository;
        this.employeeService = employeeService;
        this.organizationService = organizationService;
        this.personnelService = personnelService;
        this.strategyFactory = strategyFactory;
        this.hiringDocuments = hiringDocuments;
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
        request.setStartDate(input.getStartDate() == null && input.getType() == HrRequestType.HIRING ? LocalDate.now() : input.getStartDate());
        request.setEndDate(input.getEndDate());
        request.setStatus(HrRequestStatus.PENDING);

        return repository.save(request);
    }

    @Transactional
    public HrRequest createHiring(HiringRequestCreate input) {
        validateReason(input.getReason());

        DepartmentResponse department =
                organizationService.getDepartment(input.getDepartmentId());

        PositionResponse position =
                organizationService.getPosition(input.getPositionId());

        validatePositionDepartment(position, department);

        if (!input.getBirthDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Дата рождения кандидата должна быть в прошлом"
            );
        }

        if (input.getAddress() == null || input.getAddress().isBlank()) {
            throw new IllegalArgumentException(
                    "Для приема необходимо указать адрес кандидата"
            );
        }

        if (input.getStartDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Дата приема не может быть в будущем"
            );
        }

        HrRequest request = new HrRequest();
        request.setType(HrRequestType.HIRING);
        request.setDepartmentId(input.getDepartmentId());
        request.setPositionId(input.getPositionId());

        request.setLastName(input.getLastName().trim());
        request.setFirstName(input.getFirstName().trim());
        request.setMiddleName(cleanOptional(input.getMiddleName()));
        request.setBirthDate(input.getBirthDate());
        request.setPhone(input.getPhone().trim());
        request.setEmail(cleanOptional(input.getEmail()));
        request.setAddress(cleanOptional(input.getAddress()));
        request.setWorkSchedule(cleanOptional(input.getWorkSchedule()));

        request.setStartDate(input.getStartDate());
        request.setReason(input.getReason().trim());
        request.setStatus(HrRequestStatus.PENDING);

        return repository.save(request);
    }

    @Transactional
    public HrRequest createTransfer(TransferRequestCreate input) {
        validateReason(input.getReason());
        validateEmployee(input.getEmployeeId());

        DepartmentResponse department =
                organizationService.getDepartment(input.getDepartmentId());

        PositionResponse position =
                organizationService.getPosition(input.getPositionId());

        validatePositionDepartment(position, department);

        HrRequest request = new HrRequest();
        request.setType(HrRequestType.TRANSFER);
        request.setEmployeeId(input.getEmployeeId());
        request.setDepartmentId(input.getDepartmentId());
        request.setPositionId(input.getPositionId());
        request.setStartDate(input.getStartDate());
        request.setReason(input.getReason().trim());
        request.setStatus(HrRequestStatus.PENDING);

        return repository.save(request);
    }

    @Transactional
    public HrRequest createIncrease(IncreaseRequestCreate input) {
        validateReason(input.getReason());
        validateEmployee(input.getEmployeeId());
        organizationService.getPosition(input.getPositionId());

        HrRequest request = new HrRequest();
        request.setType(HrRequestType.INCREASE);
        request.setEmployeeId(input.getEmployeeId());
        request.setPositionId(input.getPositionId());
        request.setStartDate(input.getStartDate());
        request.setReason(input.getReason().trim());
        request.setStatus(HrRequestStatus.PENDING);

        return repository.save(request);
    }

    @Transactional
    public HrRequest createDismissal(DismissalRequestCreate input) {
        validateReason(input.getReason());
        validateEmployee(input.getEmployeeId());

        HrRequest request = new HrRequest();
        request.setType(HrRequestType.DISMISSAL);
        request.setEmployeeId(input.getEmployeeId());
        request.setStartDate(input.getStartDate());
        request.setReason(input.getReason().trim());
        request.setStatus(HrRequestStatus.PENDING);

        return repository.save(request);
    }

    @Transactional
    public HrRequest createVacation(VacationRequestCreate input) {
        validateReason(input.getReason());
        validateEmployee(input.getEmployeeId());
        validatePeriod(input.getStartDate(), input.getEndDate());

        HrRequest request = new HrRequest();
        request.setType(HrRequestType.VACATION);
        request.setEmployeeId(input.getEmployeeId());
        request.setStartDate(input.getStartDate());
        request.setEndDate(input.getEndDate());
        request.setReason(input.getReason().trim());
        request.setStatus(HrRequestStatus.PENDING);

        return repository.save(request);
    }

    @Transactional
    public HrRequest createSickLeave(SickLeaveRequestCreate input) {
        validateReason(input.getReason());
        validateEmployee(input.getEmployeeId());
        validatePeriod(input.getStartDate(), input.getEndDate());

        HrRequest request = new HrRequest();
        request.setType(HrRequestType.SICK_LEAVE);
        request.setEmployeeId(input.getEmployeeId());
        request.setStartDate(input.getStartDate());
        request.setEndDate(input.getEndDate());
        request.setReason(input.getReason().trim());
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

        HrRequestStrategy strategy =
                strategyFactory.getStrategy(request.getType());

        strategy.approve(request);

        if (request.getType() == HrRequestType.HIRING) {
            hiringDocuments.createDocuments(request);
        }

        request.setStatus(HrRequestStatus.COMPLETED);

        return repository.save(request);
    }

    @Transactional
    public HrRequest reject(Long id) {
        HrRequest request = getById(id);
        checkPending(request);

        request.setStatus(HrRequestStatus.REJECTED);

        return repository.save(request);
    }


    private String cleanOptional(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private void validateReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Основание заявки обязательно");
        }

        if (reason.length() > 500) {
            throw new IllegalArgumentException(
                    "Основание не должно превышать 500 символов"
            );
        }
    }

    private void validateEmployee(Long employeeId) {
        if (employeeId == null || employeeId <= 0) {
            throw new IllegalArgumentException(
                    "Необходимо указать корректный ID сотрудника"
            );
        }

        employeeService.getEmployeeById(employeeId);
    }

    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Необходимо указать дату начала и окончания периода"
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "Дата окончания не может быть раньше даты начала"
            );
        }
    }

    private void validatePositionDepartment(
            PositionResponse position,
            DepartmentResponse department
    ) {
        if (!position.departmentId().equals(department.id())) {
            throw new IllegalArgumentException(
                    "Указанная должность не относится к выбранному подразделению"
            );
        }
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
