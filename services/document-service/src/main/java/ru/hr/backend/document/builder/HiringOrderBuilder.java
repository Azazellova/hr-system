package ru.hr.backend.document.builder;

import java.time.format.DateTimeFormatter;

public class HiringOrderBuilder extends AbstractHiringDocumentBuilder {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public String build() {
        validateCommon();
        return """
                ПРИКАЗ О ПРИЁМЕ НА РАБОТУ

                Работодатель: %s
                Принимаемый сотрудник: %s (ID сотрудника: %d)

                ПРИКАЗЫВАЮ:
                1. Принять сотрудника на должность «%s».
                2. Определить подразделение: «%s».
                3. Установить дату начала работы: %s.
                4. Установить оклад: %s руб. в месяц.

                Основание: кадровое событие № %d.
                Приказ сформирован информационной системой учёта кадров.
                """.formatted(employerName, fullName, employeeId, positionName,
                departmentName, employmentDate.format(DATE), salary.toPlainString(), eventId);
    }
}
