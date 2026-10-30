package ru.hr.backend.hr.dto;

import jakarta.validation.constraints.*;
import ru.hr.backend.hr.entity.HrRequestType;

import java.time.LocalDate;
import java.math.BigDecimal;

public class HrRequestCreate {
    @NotNull(message = "Тип кадровой заявки обязателен")
    private HrRequestType type;

    @Positive(message = "ID сотрудника должен быть положительным")
    private Long employeeId;

    @Positive(message = "ID подразделения должен быть положительным")
    private Long departmentId;

    @Positive(message = "ID должности должен быть положительным")
    private Long positionId;

    @NotBlank(message = "Основание заявки обязательно")
    @Size(max = 500, message = "Основание не должно превышать 500 символов")
    private String reason;

    private LocalDate startDate;
    private LocalDate endDate;

    @Size(min = 2, max = 100, message = "Фамилия должна содержать от 2 до 100 символов")
    private String lastName;

    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    private String firstName;

    @Size(max = 100, message = "Отчество не должно превышать 100 символов")
    private String middleName;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @Pattern(regexp = "^(?:\\+7|8)[\\s\\-]?\\(?\\d{3}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$", message = "Некорректный формат телефона")
    private String phone;

    @Email(message = "Некорректный формат email")
    @Size(max = 150, message = "Email не должен превышать 150 символов")
    private String email;

    @Size(max = 255, message = "Адрес не должен превышать 255 символов")
    private String address;

    @Size(max = 100, message = "График работы не должен превышать 100 символов")
    private String workSchedule;

    private BigDecimal salary;

    public HrRequestType getType() { return type; }
    public void setType(HrRequestType type) { this.type = type; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Long getPositionId() { return positionId; }
    public void setPositionId(Long positionId) { this.positionId = positionId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public String getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(String workSchedule) { this.workSchedule = workSchedule; }
}
