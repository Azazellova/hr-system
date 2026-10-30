package ru.hr.backend.hr.event;

import java.time.LocalDateTime;

public class PersonnelEvent {
    private final Long employeeId;
    private final String action;
    private final String status;
    private final String reason;
    private final LocalDateTime occurredAt = LocalDateTime.now();

    public PersonnelEvent(Long employeeId, String action, String status, String reason) {
        this.employeeId = employeeId;
        this.action = action;
        this.status = status;
        this.reason = reason;
    }
    public Long getEmployeeId() { return employeeId; }
    public String getAction() { return action; }
    public String getStatus() { return status; }
    public String getReason() { return reason; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
}
