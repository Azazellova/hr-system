
package ru.hr.backend.hr.strategy;

import org.springframework.stereotype.Component;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestType;
import ru.hr.backend.hr.service.PersonnelService;

@Component
public class DismissalStrategy implements HrRequestStrategy {

    private final PersonnelService personnelService;

    public DismissalStrategy(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @Override
    public HrRequestType getType() {
        return HrRequestType.DISMISSAL;
    }

    @Override
    public void approve(HrRequest request) {

        if (request.getEmployeeId() == null) {
            throw new IllegalArgumentException(
                    "Для увольнения необходимо указать ID сотрудника"
            );
        }

        if (request.getReason() == null
                || request.getReason().isBlank()) {
            throw new IllegalArgumentException(
                    "Необходимо указать причину увольнения"
            );
        }

        personnelService.dismiss(
                request.getEmployeeId(),
                request.getReason()
        );
    }
}
