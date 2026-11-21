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


    public Position() {}

    public Position(String name, Long departmentId) {
        this.name = name;
        this.departmentId = departmentId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getDepartmentId() { return departmentId; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
}
