package ru.hr.backend.document.builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmploymentContractBuilder extends AbstractHiringDocumentBuilder {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public String build() {
        validateCommon();
        if (contractNumber == null || contractNumber.isBlank()) {
            throw new IllegalStateException("Не указан номер трудового договора");
        }
        return """
                ТРУДОВОЙ ДОГОВОР № %s

                Работодатель: %s
                Работник: %s (ID сотрудника: %d)

                1. Работник принимается на должность: %s.
                2. Подразделение: %s.
                3. Дата начала работы: %s.
                4. Размер оклада: %s руб. в месяц.

                Договор сформирован информационной системой учёта кадров.
                """.formatted(contractNumber, employerName, fullName, employeeId,
                positionName, departmentName, employmentDate.format(DATE), salary.toPlainString());
    }
}
