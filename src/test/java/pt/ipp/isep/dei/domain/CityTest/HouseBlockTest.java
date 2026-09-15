//package pt.ipp.isep.dei.domain.CityTest;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//import pt.ipp.isep.dei.repository.Simulator;
//import pt.ipp.isep.dei.domain.position.HouseBlock;
//import pt.ipp.isep.dei.domain.position.Location;
//import pt.ipp.isep.dei.domain.position.Position;
//import pt.ipp.isep.dei.domain.scenario.Scenario;
//import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
//import pt.ipp.isep.dei.domain.simulation.Simulation;
//
//class HouseBlockTest {
//
//    @Test
//    void isValidReturnsTrueForValidCount() {
//        // Arrange
//        int validCount = 10; // Assuming map area is larger
//
//        // Act & Assert
//        assertTrue(HouseBlock.isValid(validCount));
//    }
//
//    @Test
//    void isValidReturnsFalseForInvalidCount() {
//        // Arrange
//        int invalidCount = -1;
//
//        // Act & Assert
//        assertFalse(HouseBlock.isValid(invalidCount));
//    }
//
//    @Test
//    void getPositionReturnsCorrectPosition() {
//        Position expectedPos = new Position(2, 3);
//        Location location = new Location(expectedPos);
//        HouseBlock houseBlock = new HouseBlock(location);
//
//        houseBlock.addPosition(2, 3);  // <== Add this line
//
//        Position actualPos = houseBlock.getPosition();
//
//        assertEquals(expectedPos, actualPos);
//    }
//
//
//    @Test
//    void toStringReturnsCorrectFormat() {
//        // Arrange
//        Position pos = new Position(4, 5);
//        Location location = new Location(pos);
//        HouseBlock houseBlock = new HouseBlock(location);
//
//        // Act & Assert
//        System.out.println(houseBlock);
//        assertTrue(houseBlock.toString().contains("HouseBlock"));
//    }
//
//    @BeforeAll
//    static void setup(){
//        Simulator simulator = Simulator.getInstance();
//        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
//        simulator.createScenario("TestScenario", simulator.getMapRepository().getMap("TestMap"), timeRestrictions, null, null, null, null);
//        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
//        Simulation simulation = Simulation.getInstance(scenario);
//    }
//}