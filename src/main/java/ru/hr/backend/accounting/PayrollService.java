package ru.hr.backend.accounting;

import org.springframework.stereotype.Service;
import ru.hr.backend.employee.entity.Worktime;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** Сервис оплаты труда: отделяет расчёт от кадрового процесса. */
@Service
public class PayrollService {
    private final AccountingAdapter accounting;

    public PayrollService(AccountingAdapter accounting) { this.accounting = accounting; }

    /** Оклад для трудового договора; фактическая выработка на него не влияет. */
    public BigDecimal getContractSalary(Long positionId) {
        return accounting.getMonthlySalary(positionId);
    }

    /** Учебный расчёт по Worktime с двойной оплатой сверхурочных (без отпускных и больничных). */
    public BigDecimal calculate(Long positionId, Worktime time) {
        if (time == null || time.getPlannedHours() == null || time.getWorkedHours() == null || time.getPlannedHours() <= 0 || time.getWorkedHours() < 0 || (time.getOvertimeHours() != null && time.getOvertimeHours() < 0)) {
            throw new IllegalArgumentException("Некорректные часы Worktime");
        }
        BigDecimal hourly = getContractSalary(positionId).divide(BigDecimal.valueOf(time.getPlannedHours()), 8, RoundingMode.HALF_UP);
        BigDecimal regular = BigDecimal.valueOf(time.getWorkedHours());
        BigDecimal overtime = BigDecimal.valueOf(time.getOvertimeHours() == null ? 0 : time.getOvertimeHours());
        return hourly.multiply(regular.add(overtime.multiply(BigDecimal.valueOf(2)))).setScale(2, RoundingMode.HALF_UP);
    }
}
