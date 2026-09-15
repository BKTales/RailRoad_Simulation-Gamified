package pt.ipp.isep.dei.domain.position;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    @Test
    void createPositionValidArguments() {
        // Arrange
        Simulation r = Simulation.getInstance();

        // Act
        Position testPosition = new Position(1, 2);

        // Assert
        assertNotNull(testPosition);
    }

    @Test
    void createPositionInvalidArgumentsLow() {
        // Arrange
        Simulation r = Simulation.getInstance();

        Position testPosition = null;

        // Act
        try {
            testPosition = new Position(-1, -2);
        } catch (Exception e){

            assertNull(testPosition);
        }

        // Assert
    }

    @Test
    void createPositionInvalidArgumentsHigh() {
        // Arrange
        Simulation r = Simulation.getInstance();

        Position testPosition = null;
        // Act
        try {
            testPosition = new Position(15, 15);

            // Assert
        } catch (Exception e){
            assert true;
        }

    }

    @Test
    void getX() {
        // Arrange
        Simulation r = Simulation.getInstance();
        Position testPosition = new Position(1, 2);

        // Act
        int x = testPosition.getX();
        // Assert
        assertEquals(testPosition.getX(), x);
    }

    @Test
    void getY() {
        // Arrange
        Simulation r = Simulation.getInstance();
        Position testPosition = new Position(1, 2);

        // Act
        int y = testPosition.getY();
        // Assert
        assertEquals(testPosition.getY(), y);
    }

    @Test
    void testEqualsTrue() {
        // Arrange
        Simulation r = Simulation.getInstance();
        Position testPosition = new Position(1, 2);
        Position testPosition2 = new Position(1, 2);

        // Act
        boolean equals = testPosition.equals(testPosition2);

        // Assert
        assertTrue(equals);
    }

    @Test
    void testEqualsFalse() {
        // Arrange
        Simulation r = Simulation.getInstance();
        Position testPosition = new Position(1, 2);
        Position testPosition2 = new Position(9, 9);

        // Act
        boolean equals = testPosition.equals(testPosition2);

        // Assert
        assertFalse(equals);
    }

    @BeforeAll
    static void setup(){
        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        simulator.createScenario("TestScenario",simulator.getMapRepository().getMap("TestMap") , timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        Simulation simulation = Simulation.getInstance(scenario);
    }
}