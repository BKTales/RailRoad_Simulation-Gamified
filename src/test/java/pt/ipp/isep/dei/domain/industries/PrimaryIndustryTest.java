package pt.ipp.isep.dei.domain.industries;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import static org.junit.jupiter.api.Assertions.*;

class PrimaryIndustryTest {
    @Test
    void testPrimaryIndustry() {
        // Arrange

        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        Simulator simulator = Simulator.getInstance();
        simulator.createScenario("TestScenario", simulator.createMap("Mp", 100, 100, 10), timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        Simulation repo = Simulation.getInstance(scenario);

        // Act

        Industry i = repo.getCurrentScenario().createIndustry(ProductType.WOOL, repo.getCurrentMap().createLocation(new Position(13, 15)));
        // Assert

        assertInstanceOf(PrimaryIndustry.class, i);
    }

    @Test
    void testPrimaryIndustryWrongProductType() {
        // Arrange
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        Simulator simulator = Simulator.getInstance();
        simulator.createScenario("TestScenario", simulator.createMap("Mp3", 100, 100, 10), timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        Simulation repo = Simulation.getInstance(scenario);

        // Act
        Industry i = repo.getCurrentScenario().createIndustry(ProductType.CAR, repo.getCurrentMap().createLocation(new Position(14, 15)));

        // Assert
        assertInstanceOf(TransformingIndustry.class, i);
    }
}

