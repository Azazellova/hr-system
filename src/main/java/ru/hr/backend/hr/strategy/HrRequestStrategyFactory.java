package ru.hr.backend.hr.strategy;

import org.springframework.stereotype.Component;
import ru.hr.backend.hr.entity.HrRequestType;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class HrRequestStrategyFactory {

    private final Map<HrRequestType, HrRequestStrategy> strategies =
            new EnumMap<>(HrRequestType.class);

    public HrRequestStrategyFactory(List<HrRequestStrategy> strategyList) {

        for (HrRequestStrategy strategy : strategyList) {

            HrRequestStrategy previous = strategies.put(
                    strategy.getType(),
                    strategy
            );

            if (previous != null) {
                throw new IllegalStateException(
                        "Для типа заявления " + strategy.getType()
                                + " зарегистрировано несколько стратегий"
                );
            }
        }
    }

    public HrRequestStrategy getStrategy(HrRequestType type) {

        HrRequestStrategy strategy = strategies.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Стратегия для типа заявления " + type + " не найдена"
            );
        }

        return strategy;
    }
}
