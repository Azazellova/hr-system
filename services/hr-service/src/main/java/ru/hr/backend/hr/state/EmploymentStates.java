package ru.hr.backend.hr.state;

import java.util.Locale;

public final class EmploymentStates {
    private EmploymentStates() { }

    public static EmploymentState from(String status, Boolean active) {
        String normalized = status == null ? "" : status.trim().toUpperCase(Locale.ROOT);
        if ("DISMISSED".equals(normalized) || "УВОЛЕН".equals(normalized)) return new DismissedState();
        if ("SUSPENDED".equals(normalized) || "ПРИОСТАНОВЛЕН".equals(normalized)) return new SuspendedState();
        if ("ACTIVE".equals(normalized) || "АКТИВЕН".equals(normalized) || Boolean.TRUE.equals(active)) return new ActiveState();
        if (Boolean.FALSE.equals(active)) return new SuspendedState();
        // Для старых записей без статуса считаем сотрудника активным, если он не помечен неактивным.
        return new ActiveState();
    }
}
