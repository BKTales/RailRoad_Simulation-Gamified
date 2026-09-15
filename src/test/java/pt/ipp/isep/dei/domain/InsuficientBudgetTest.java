package pt.ipp.isep.dei.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InsuficientBudgetTest {

    // US03 – test InsuficientBudget exception messages
    @Test
    void testInsufficientBudgetDefaultMessage() {
        InsuficientBudget ex = new InsuficientBudget();
        assertEquals("Insufficient Budget!", ex.getMessage());
    }

    @Test
    void testInsufficientBudgetCustomMessage() {
        InsuficientBudget ex = new InsuficientBudget("No funds");
        assertEquals("No funds", ex.getMessage());
    }
}
