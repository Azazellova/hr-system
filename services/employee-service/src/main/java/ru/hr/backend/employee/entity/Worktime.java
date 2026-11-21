package ru.hr.backend.employee.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "worktime")
public class Worktime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_time_id")
    private Long workTimeId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "period", nullable = false, length = 50)
    private String period;

    @Column(name = "planned_hours")
    private Double plannedHours;

    @Column(name = "worked_hours")
    private Double workedHours;

    @Column(name = "overtime_hours")
    private Double overtimeHours;

    public Worktime() {
    }

    public Long getWorkTimeId() {
        return workTimeId;
    }

    public void setWorkTimeId(Long workTimeId) {
        this.workTimeId = workTimeId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Double getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(Double plannedHours) {
        this.plannedHours = plannedHours;
    }

    public Double getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(Double workedHours) {
        this.workedHours = workedHours;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
}