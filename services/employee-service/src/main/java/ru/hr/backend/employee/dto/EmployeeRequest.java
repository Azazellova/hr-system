package ru.hr.backend.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.hr.backend.employee.validation.ValidEmployeeDates;

import java.time.LocalDate;

@ValidEmployeeDates
public class EmployeeRequest {

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, max = 100, message = "Фамилия должна содержать от 2 до 100 символов")
    @Pattern(
            regexp = "^[\\p{L}\\p{M}][\\p{L}\\p{M}'’\\- ]*[\\p{L}\\p{M}]$",
            message = "Фамилия содержит недопустимые символы"
    )
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    @Pattern(
            regexp = "^[\\p{L}\\p{M}][\\p{L}\\p{M}'’\\- ]*[\\p{L}\\p{M}]$",
            message = "Имя содержит недопустимые символы"
    )
    private String firstName;

    @Size(max = 100, message = "Отчество не должно превышать 100 символов")
    @Pattern(
            regexp = "^$|^[\\p{L}\\p{M}][\\p{L}\\p{M}'’\\- ]*[\\p{L}\\p{M}]$",
            message = "Отчество содержит недопустимые символы"
    )
    private String middleName;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @Pattern(
            regexp = "^(?:\\+7|8)[\\s\\-]?\\(?\\d{3}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$",
            message = "Телефон должен соответствовать формату +7 (999) 123-45-67"
    )
    private String phone;

    @Email(message = "Некорректный формат email")
    @Size(max = 150, message = "Email не должен превышать 150 символов")
    @Pattern(
            regexp = "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$",
            message = "Email имеет недопустимый формат"
    )
    private String email;

    @Size(max = 255, message = "Адрес не должен превышать 255 символов")
    @Pattern(
            regexp = "^[^\\p{Cc}]*$",
            message = "Адрес содержит недопустимые управляющие символы"
    )
    private String address;

    @Positive(message = "departmentId должен быть положительным")
    private Long departmentId;

    @Positive(message = "positionId должен быть положительным")
    private Long positionId;

    @PastOrPresent(message = "Дата приема не может быть в будущем")
    private LocalDate employmentDate;

    @PastOrPresent(message = "Дата увольнения не может быть в будущем")
    private LocalDate dismissalDate;

    @Size(max = 50, message = "Статус не должен превышать 50 символов")
    @Pattern(
            regexp = "^$|^[^\\p{Cc}]+$",
            message = "Статус содержит недопустимые управляющие символы"
    )
    private String employmentStatus;

    @Size(max = 100, message = "График работы не должен превышать 100 символов")
    @Pattern(
            regexp = "^$|^[^\\p{Cc}]+$",
            message = "График работы содержит недопустимые управляющие символы"
    )
    private String workSchedule;

    private Boolean isActive;

    public EmployeeRequest() {
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public LocalDate getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(LocalDate dismissalDate) {
        this.dismissalDate = dismissalDate;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(String workSchedule) {
        this.workSchedule = workSchedule;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}