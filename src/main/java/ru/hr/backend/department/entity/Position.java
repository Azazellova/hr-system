package ru.hr.backend.department.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "positions",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_position_name_department",
        columnNames = {"department_id", "name"}
    )
)
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    /*
     * Необязательная ссылка на занимающего должность сотрудника.
     * Поле сохранено согласно приложенной схеме. Если одна должность
     * должна назначаться нескольким сотрудникам, это поле лучше убрать:
     * employee.positionId уже задаёт связь сотрудника с должностью.
     */

    //TODO: УБРАТЬ EMPLOYEE_ID, у нескольких сотрудников может быть одна должность
    @Column(name = "employee_id")
    private Long employeeId;

    public Position() {}

    public Position(String name, Long departmentId, Long employeeId) {
        this.name = name;
        this.departmentId = departmentId;
        this.employeeId = employeeId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getDepartmentId() { return departmentId; }
    public Long getEmployeeId() { return employeeId; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
}
