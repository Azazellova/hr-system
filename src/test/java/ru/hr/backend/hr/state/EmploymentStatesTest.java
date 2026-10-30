package ru.hr.backend.hr.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmploymentStatesTest {
    @Test
    void dismissedEmployeeCannotBeActivated() {
        EmploymentState state = EmploymentStates.from("DISMISSED", false);
        assertThrows(IllegalStateException.class, state::activate);
    }

    @Test
    void activeEmployeeCannotBeActivatedTwice() {
        EmploymentState state = EmploymentStates.from("ACTIVE", true);
        assertThrows(IllegalStateException.class, state::activate);
    }

    @Test
    void suspendedEmployeeCanBeActivated() {
        EmploymentState state = EmploymentStates.from("SUSPENDED", false);
        assertDoesNotThrow(state::activate);
    }
}
