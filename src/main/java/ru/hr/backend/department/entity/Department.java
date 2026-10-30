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

    /** NULL означает корневое подразделение организации. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_department_id", foreignKey = @ForeignKey(name = "fk_department_parent"))
    private Department parentDepartment;

    // ID сотрудника из Employee Service; отдельную JPA-связь не создаём.
    @Column(name = "manager_id")
    private Long managerId;

    public Department() {}

    public Department(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getManagerId() { return managerId; }
    public Department getParentDepartment() { return parentDepartment; }
    public Long getParentDepartmentId() {
        return parentDepartment == null ? null : parentDepartment.getId();
    }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }
    public void setParentDepartment(Department parentDepartment) { this.parentDepartment = parentDepartment; }
}
