package pt.ipp.isep.dei.domain.industries;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.scenario.PortBehaviour;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortTest {
//    @Test
//    void testPortIndustry() {
//        // Arrange
//        Simulation simulator = Simulation.getInstance();
//        // Act
//        Port i = simulator.getCurrentScenario().createPort(simulator.getCurrentMap().createLocation(new Position(25, 15)));
//        // Assert
//
//        assertInstanceOf(Port.class, i);
//    }

    @Test
    void testPortIndustryNullBehaviour() {
        // Arrange
        Simulation repo = Simulation.getInstance();

        try {
            // Act
            Industry i = repo.getCurrentScenario().createPort(repo.getCurrentMap().createLocation(new Position(13, 18)));
        }catch (Exception e) {
            assert true;
            return;
        }
        assert false;
    }

        @BeforeAll
        static void setup(){
            TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
            Simulator simulator = Simulator.getInstance();
            List<ProductType> productTypes = new ArrayList<>();
            productTypes.add(ProductType.CAR);
            ProductType exportedProduct = ProductType.STEEL;
            PortBehaviour portBehaviour = new PortBehaviour(productTypes, exportedProduct);
            try {
                simulator.createMap("Mp2", 100, 100, 10);
            } catch(Exception e){
            }finally {
                simulator.createScenario("TestScenario", simulator.getMapRepository().getMap("Mp2"), timeRestrictions, null, null, portBehaviour, null);
                Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
                Simulation repo = Simulation.getInstance(scenario);
            }
        }
}