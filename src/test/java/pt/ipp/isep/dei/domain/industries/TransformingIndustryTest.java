package pt.ipp.isep.dei.domain.industries;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import static org.junit.jupiter.api.Assertions.*;

class TransformingIndustryTest {

    @Test
    void testTransformIndustry() {
        // Arrange

        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        Simulator simulator = Simulator.getInstance();
        simulator.createScenario("TestScenario", simulator.getMapRepository().getMap("TestMap"), timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        Simulation repo = Simulation.getInstance(scenario);

        // Act

        Industry i = repo.getCurrentScenario().createIndustry(ProductType.CAR, repo.getCurrentMap().createLocation(new Position(23, 15)));
        // Assert

        assertInstanceOf(TransformingIndustry.class, i);
    }

    @Test
    void testTransformIndustryWrongProductType() {
        // Arrange
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        Simulator simulator = Simulator.getInstance();
        simulator.createScenario("TestScenario", simulator.getMapRepository().getMap("TestMap"), timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        Simulation repo = Simulation.getInstance(scenario);

        // Act
        Industry i = repo.getCurrentScenario().createIndustry(ProductType.WOOL, repo.getCurrentMap().createLocation(new Position(24, 15)));

        // Assert
        assertInstanceOf(PrimaryIndustry.class, i);
    }
}