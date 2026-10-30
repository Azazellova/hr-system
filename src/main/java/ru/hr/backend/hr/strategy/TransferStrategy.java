
package ru.hr.backend.hr.strategy;

import org.springframework.stereotype.Component;
import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestType;
import ru.hr.backend.hr.service.PersonnelService;

@Component
public class TransferStrategy implements HrRequestStrategy {

    private final PersonnelService personnelService;

    public TransferStrategy(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @Override
    public HrRequestType getType() {
        return HrRequestType.TRANSFER;
    }

    @Override
    public void approve(HrRequest request) {

        if (request.getEmployeeId() == null) {
            throw new IllegalArgumentException(
                    "Для перевода необходимо указать ID сотрудника"
            );
        }

        if (request.getDepartmentId() == null
                || request.getPositionId() == null) {
            throw new IllegalArgumentException(
                    "Для перевода необходимо указать новое подразделение и должность"
            );
        }

        personnelService.transfer(
                request.getEmployeeId(),
                request.getDepartmentId(),
                request.getPositionId(),
                request.getReason()
        );
    }
}
