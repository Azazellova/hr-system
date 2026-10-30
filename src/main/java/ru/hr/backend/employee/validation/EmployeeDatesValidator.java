package ru.hr.backend.employee.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.hr.backend.employee.dto.EmployeeRequest;

public class EmployeeDatesValidator
        implements ConstraintValidator<ValidEmployeeDates, EmployeeRequest> {

    @Override
    public boolean isValid(EmployeeRequest request, ConstraintValidatorContext context) {

        if (request == null) {
            return true;
        }

        if (request.getBirthDate() != null
                && request.getEmploymentDate() != null
                && !request.getBirthDate().isBefore(request.getEmploymentDate())) {

            return violation(
                    context,
                    "Дата рождения должна быть раньше даты приема"
            );
        }

        if (request.getDismissalDate() != null
                && request.getEmploymentDate() == null) {

            return violation(
                    context,
                    "Для даты увольнения необходимо указать дату приема"
            );
        }

        if (request.getEmploymentDate() != null
                && request.getDismissalDate() != null
                && request.getDismissalDate().isBefore(request.getEmploymentDate())) {

            return violation(
                    context,
                    "Дата увольнения не может быть раньше даты приема"
            );
        }

        return true;
    }

    private boolean violation(
            ConstraintValidatorContext context,
            String message
    ) {
        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();

        return false;
    }
}