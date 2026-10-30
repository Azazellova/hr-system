package ru.hr.backend.accounting;

import java.math.BigDecimal;

/** Адаптер бухгалтерии: предоставляет базовый месячный оклад по должности. */
public interface AccountingAdapter {
    /** @return оклад в рублях; ошибка, если тариф не настроен. */
    BigDecimal getMonthlySalary(Long positionId);
}
