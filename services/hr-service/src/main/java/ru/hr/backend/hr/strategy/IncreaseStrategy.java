
package ru.hr.backend.hr.strategy;

import org.springframework.stereotype.Component;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestType;
import ru.hr.backend.hr.service.PersonnelService;

@Component
public class IncreaseStrategy implements HrRequestStrategy {

    private final PersonnelService personnelService;

    public IncreaseStrategy(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @Override
    public HrRequestType getType() {
        return HrRequestType.INCREASE;
    }

    @Override
    public void approve(HrRequest request) {

        if (request.getEmployeeId() == null) {
            throw new IllegalArgumentException(
                    "Для повышения необходимо указать ID сотрудника"
            );
        }

        if (request.getPositionId() == null) {
            throw new IllegalArgumentException(
                    "Для повышения необходимо указать новую должность"
            );
        }

        personnelService.increase(
                request.getEmployeeId(),
                request.getPositionId(),
                request.getReason()
        );
    }
}
