
package ru.hr.backend.hr.strategy;

import org.springframework.stereotype.Component;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestType;

@Component
public class VacationStrategy implements HrRequestStrategy {

    @Override
    public HrRequestType getType() {
        return HrRequestType.VACATION;
    }

    @Override
    public void approve(HrRequest request) {

        if (request.getEmployeeId() == null) {
            throw new IllegalArgumentException(
                    "Для оформления отпуска необходимо указать сотрудника"
            );
        }

        if (request.getStartDate() == null
                || request.getEndDate() == null) {
            throw new IllegalArgumentException(
                    "Необходимо указать начало и окончание отпуска"
            );
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException(
                    "Дата окончания отпуска не может быть раньше даты начала"
            );
        }
    }
}
