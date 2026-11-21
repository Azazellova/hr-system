
package ru.hr.backend.hr.strategy;

import ru.hr.backend.hr.entity.HrRequest;
import ru.hr.backend.hr.entity.HrRequestType;

public interface HrRequestStrategy {
    HrRequestType getType();

    void approve(HrRequest request);
}
