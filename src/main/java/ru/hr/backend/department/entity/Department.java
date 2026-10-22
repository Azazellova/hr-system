package ru.hr.backend.department.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    // ID сотрудника из Employee Service. Не является отдельной сущностью здесь.
    @Column(name = "manager_id")
    private Long managerId;

    public Department() {}

    public Department(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getManagerId() { return managerId; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }
}
