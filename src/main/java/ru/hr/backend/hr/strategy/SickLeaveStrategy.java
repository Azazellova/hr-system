
package ru.hr.backend.hr.strategy;

import org.springframework.stereotype.Component;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestType;

@Component
public class SickLeaveStrategy implements HrRequestStrategy {

    @Override
    public HrRequestType getType() {
        return HrRequestType.SICK_LEAVE;
    }

    @Override
    public void approve(HrRequest request) {

        if (request.getEmployeeId() == null) {
            throw new IllegalArgumentException(
                    "Для оформления больничного необходимо указать сотрудника"
            );
        }

        if (request.getStartDate() == null
                || request.getEndDate() == null) {
            throw new IllegalArgumentException(
                    "Необходимо указать начало и окончание больничного"
            );
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException(
                    "Дата окончания больничного не может быть раньше даты начала"
            );
        }
    }
}
