package pt.ipp.isep.dei.domain.CityTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.position.PositionRandomizer;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;

class PositionRandomizerTest {

    @Test
    void getRandomPositionAroundReturnsValidPosition() {
        // Arrange
        Position center = new Position(10, 10);

        // Act
        Position randomPos = PositionRandomizer.getRandomPositionAround(center);

        // Assert
        assertNotNull(randomPos);
        assertTrue(Math.abs(randomPos.getX() - center.getX()) <= 2);
        assertTrue(Math.abs(randomPos.getY() - center.getY()) <= 2);
    }

    @Test
    void generatedPositionIsWithinMapBounds() {
        // Arrange
        Position center = new Position(10, 10);

        // Act
        Position randomPos = PositionRandomizer.getRandomPositionAround(center);

        // Assert
        assertTrue(randomPos.getX() >= 0);
        assertTrue(randomPos.getY() >= 0);
    }

    @BeforeAll
    static void setup(){
        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        simulator.createScenario("TestScenario", simulator.createMap("TestMap", 100, 100, 10), timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        Simulation simulation = Simulation.getInstance(scenario);
    }
}