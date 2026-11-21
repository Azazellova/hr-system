package ru.hr.backend.hr.state;

public class SuspendedState implements EmploymentState {
    @Override public String status() { return "SUSPENDED"; }
    @Override public void activate() { }
    @Override public void suspend() { throw new IllegalStateException("Сотрудник уже приостановлен"); }
    @Override public void dismiss() { }
}
