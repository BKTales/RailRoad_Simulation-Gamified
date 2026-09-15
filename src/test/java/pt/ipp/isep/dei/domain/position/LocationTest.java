package pt.ipp.isep.dei.domain.position;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void getPosition() {
        // Arrange
        Simulation r = Simulation.getInstance();
        // Act
        Position testPosition = new Position(1, 2);
        Location testLocation = new Location(testPosition);

        // Assert
        assertEquals(testPosition, testLocation.getPosition());
    }

    @Test
    void testEqualsTrue() {
        // Arrange
        Simulation r = Simulation.getInstance();

        Position testPosition = new Position(1, 2);
        Location testLocation = new Location(testPosition);
        Location otherLocation = new Location(testPosition);

        // Act

        boolean equal = testLocation.equals(otherLocation);
        // Assert
        assertTrue(equal);
    }

    @Test
    void testEqualsFalse() {
        // Arrange
        Simulation r = Simulation.getInstance();
        Position testPosition = new Position(1, 2);
        Position otherPosition = new Position(2, 4);
        Location testLocation = new Location(testPosition);
        Location otherLocation = new Location(otherPosition);

        // Act
        boolean equals = testLocation.equals(otherLocation);

        // Assert
        assertFalse(equals);
    }

    @BeforeAll
    static void setup(){
        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        try{
            simulator.createMap("TestMap", 100, 100, 10);
        } catch(Exception e){

        }finally {
            simulator.createScenario("TestScenario", simulator.getMapRepository().getMap("TestMap"), timeRestrictions, null, null, null, null);
            Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
            Simulation simulation = Simulation.getInstance(scenario);
        }
    }
}