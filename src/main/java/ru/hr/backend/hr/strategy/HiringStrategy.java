
package ru.hr.backend.hr.strategy;

import org.springframework.stereotype.Component;
import ru.hr.backend.employee.dto.EmployeeRequest;
import ru.hr.backend.employee.dto.EmployeeResponse;
import ru.hr.backend.employee.service.EmployeeService;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestType;

import java.time.LocalDate;

@Component
public class HiringStrategy implements HrRequestStrategy {

    private final EmployeeService employeeService;

    public HiringStrategy(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public HrRequestType getType() {
        return HrRequestType.HIRING;
    }

    @Override
    public void approve(HrRequest request) {

        EmployeeRequest employee = new EmployeeRequest();

        employee.setLastName(request.getLastName());
        employee.setFirstName(request.getFirstName());
        employee.setMiddleName(request.getMiddleName());
        employee.setBirthDate(request.getBirthDate());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        employee.setAddress(request.getAddress());

        employee.setDepartmentId(request.getDepartmentId());
        employee.setPositionId(request.getPositionId());

        employee.setEmploymentDate(
                request.getStartDate() == null
                        ? LocalDate.now()
                        : request.getStartDate()
        );

        employee.setWorkSchedule(request.getWorkSchedule());
        employee.setIsActive(true);

        EmployeeResponse created =
                employeeService.createEmployee(employee);

        request.setEmployeeId(created.getEmployeeId());
    }
}
