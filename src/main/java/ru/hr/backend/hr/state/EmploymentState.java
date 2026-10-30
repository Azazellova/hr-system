package ru.hr.backend.hr.state;

/** State pattern: определяет, какие кадровые переходы разрешены. */
public interface EmploymentState {
    String status();
    void activate();
    void suspend();
    void dismiss();
}
