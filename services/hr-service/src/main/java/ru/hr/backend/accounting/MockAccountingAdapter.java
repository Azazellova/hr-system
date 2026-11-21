package ru.hr.backend.accounting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

/** Учебная реализация Adapter: заменяет внешнюю бухгалтерию конфигурацией. */
@Component
public class MockAccountingAdapter implements AccountingAdapter {
    private final Map<String, BigDecimal> salaries;

    public MockAccountingAdapter(@Value("#{${accounting.mock-salaries:{}}}") Map<String, BigDecimal> salaries) {
        this.salaries = salaries;
    }

    /** Возвращает тариф для существующего ID должности без изменения БД. */
    @Override
    public BigDecimal getMonthlySalary(Long positionId) {
        BigDecimal salary = salaries.get(String.valueOf(positionId));
        if (salary == null || salary.signum() <= 0) {
            throw new IllegalStateException("Оклад для должности ID " + positionId + " не настроен в accounting.mock-salaries");
        }
        return salary;
    }
}
