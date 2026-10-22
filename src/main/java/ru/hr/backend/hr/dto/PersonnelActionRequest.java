package ru.hr.backend.hr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PersonnelActionRequest {
    @NotBlank(message = "Основание кадровой операции обязательно")
    @Size(max = 500, message = "Основание не должно превышать 500 символов")
    private String reason;

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
