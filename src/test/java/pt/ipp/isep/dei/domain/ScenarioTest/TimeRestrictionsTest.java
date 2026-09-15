package pt.ipp.isep.dei.domain.ScenarioTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;

class TimeRestrictionsTest {

    @Test
    void createValidTimeRestriction() {
        // Arrange
        int startYear = 1900;
        int endYear = 1950;

        // Act
        TimeRestrictions restriction = new TimeRestrictions(startYear, endYear);

        // Assert
        assertEquals(startYear, restriction.getStartYear());
        assertEquals(endYear, restriction.getEndYear());
    }

    @Test
    void createInvalidTimeRestrictionThrowsException() {
        // Arrange
        int startYear = 1950;
        int endYear = 1900;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new TimeRestrictions(startYear, endYear));
    }

    @Test
    void negativeYearsThrowException() {
        // Arrange
        int startYear = -1;
        int endYear = 1900;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new TimeRestrictions(startYear, endYear));
    }

    @Test
    void isValidYearReturnsTrueForValidRange() {
        // Arrange
        int startYear = 1900;
        int endYear = 1950;

        // Act & Assert
        assertTrue(new TimeRestrictions(0,0).isValidYear(startYear, endYear));
    }


}