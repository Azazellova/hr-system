package ru.hr.backend.hr.state;

public class ActiveState implements EmploymentState {
    @Override public String status() { return "ACTIVE"; }
    @Override public void activate() { throw new IllegalStateException("Сотрудник уже активен"); }
    @Override public void suspend() { }
    @Override public void dismiss() { }
}
