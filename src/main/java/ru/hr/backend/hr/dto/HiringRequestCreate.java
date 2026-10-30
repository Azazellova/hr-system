
package ru.hr.backend.hr.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class HiringRequestCreate {

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, max = 100)
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 100)
    private String firstName;

    @Size(max = 100)
    private String middleName;

    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(
            regexp = "^(?:\\+7|8)[\\s\\-]?\\(?\\d{3}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$",
            message = "Некорректный формат телефона"
    )
    private String phone;

    @Email(message = "Некорректный формат email")
    @Size(max = 150)
    private String email;

    @Size(max = 255)
    private String address;

    @Size(max = 100)
    private String workSchedule;

    @NotNull(message = "Подразделение обязательно")
    @Positive(message = "ID подразделения должен быть положительным")
    private Long departmentId;

    @NotNull(message = "Должность обязательна")
    @Positive(message = "ID должности должен быть положительным")
    private Long positionId;

    @NotNull(message = "Дата приёма обязательна")
    private LocalDate startDate;

    @NotBlank(message = "Основание обязательно")
    @Size(max = 500)
    private String reason;

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

    public String getWorkSchedule() { return workSchedule; }
    public void setWorkSchedule(String workSchedule) { this.workSchedule = workSchedule; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Long getPositionId() { return positionId; }
    public void setPositionId(Long positionId) { this.positionId = positionId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
