package ru.hr.backend.hr.state;

public class DismissedState implements EmploymentState {
    @Override public String status() { return "DISMISSED"; }
    @Override public void activate() { throw new IllegalStateException("Нельзя активировать уволенного сотрудника"); }
    @Override public void suspend() { throw new IllegalStateException("Нельзя приостановить уволенного сотрудника"); }
    @Override public void dismiss() { throw new IllegalStateException("Сотрудник уже уволен"); }
}
