package ru.hr.backend.employee.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmployeeDatesValidator.class)
@Documented
public @interface ValidEmployeeDates {

    String message() default "Некорректная последовательность кадровых дат";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}