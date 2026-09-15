//package pt.ipp.isep.dei.domain.CityTest;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//import pt.ipp.isep.dei.repository.Simulator;
//import pt.ipp.isep.dei.domain.City;
//import pt.ipp.isep.dei.domain.ProductType;
//import pt.ipp.isep.dei.domain.position.HouseBlock;
//import pt.ipp.isep.dei.domain.position.Location;
//import pt.ipp.isep.dei.domain.position.Position;
//import pt.ipp.isep.dei.domain.position.PositionRandomizer;
//import pt.ipp.isep.dei.domain.scenario.Scenario;
//import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
//import pt.ipp.isep.dei.domain.simulation.Simulation;
//import pt.ipp.isep.dei.us003.CityEditorController;
//import pt.ipp.isep.dei.us003.CityEditorUI;
//
//import java.util.List;
//
//class CityTest {
//
//    // AC1 ------------------------------------------------------------------------------------------------------------------------
//
//    @Test
//    void validCityNameShouldReturnTrue() {
//        // Arrange
//        CityEditorUI cityEditorUI = new CityEditorUI();
//        String validName = "Lisbon";
//
//        // Act
//        boolean isValid = cityEditorUI.isValidCityName(validName);
//
//        // Assert
//        assertTrue(isValid);
//    }
//
//    @Test
//    void invalidCityNameWithDigitsShouldReturnFalse() {
//        // Arrange
//        CityEditorUI cityEditorUI = new CityEditorUI();
//        String invalidName = "Lisbon123";
//
//        // Act
//        boolean isValid = cityEditorUI.isValidCityName(invalidName);
//
//        // Assert
//        assertFalse(isValid);
//    }
//
//    @Test
//    void invalidCityNameWithSpecialCharactersShouldReturnFalse() {
//        // Arrange
//        CityEditorUI cityEditorUI = new CityEditorUI();
//        String invalidName = "Lisbon!@#";
//
//        // Act
//        boolean isValid = cityEditorUI.isValidCityName(invalidName);
//
//        // Assert
//        assertFalse(isValid);
//    }
//
//    // AC2 ------------------------------------------------------------------------------------------------------------------------
//
//    @Test
//    void createCityBlockShouldAddBlockToCityManual() {
//        // Arrange
//        CityEditorController controller = new CityEditorController();
//        City city = new City("TestCity", new Location(new Position(5, 5)), 0, 100, ProductType.GRAINS);
//        city.createHouseBlockList();
//
//        // Act
//        controller.createCityBlock(city, 1, 2);
//
//        // Assert
//        List<HouseBlock> houseBlocks = city.getHouseBlocks();
//        assertFalse(houseBlocks.isEmpty());
//        assertEquals(new Position(1, 2), houseBlocks.get(0).getPosition());
//    }
//
//    @Test
//    void createCityBlockShouldAddBlockToCityRandom() {
//        // Arrange
//        CityEditorController controller = new CityEditorController();
//        City city = new City("TestCity", new Location(new Position(5, 5)), 0, 100, ProductType.GRAINS);
//        city.createHouseBlockList();
//
//        // Act
//        controller.createCityBlock(city, PositionRandomizer.getRandomPositionAround(city.getPosition()).getX(), PositionRandomizer.getRandomPositionAround(city.getPosition()).getY());
//
//        // Assert
//        List<HouseBlock> houseBlocks = city.getHouseBlocks();
//        assertFalse(houseBlocks.isEmpty());
//    }
//
//    // AC3 ------------------------------------------------------------------------------------------------------------------------
//
//    @Test
//    void creatingCityWithValidPositionShouldSucceed() {
//        // Arrange
//        Position validPosition = new Position(5, 5); // assume within bounds
//        Location location = new Location(validPosition);
//
//        // Act & Assert
//        assertDoesNotThrow(() -> new City("ValidCity", location, 100, 50, ProductType.GRAINS));
//    }
//
//    @Test
//    void creatingCityWithInvalidPositionShouldThrowException() {
//
//        assertThrows(IllegalArgumentException.class, () ->
//        {
//            new Position(-1, -1);
//        });
//    }
//
//    // AC5 ------------------------------------------------------------------------------------------------------------------------
//
//    @Test
//    void createCityShouldAddCityToMap() {
//        // Arrange
//        CityEditorController controller = new CityEditorController();
//        String cityName = "TestCity";
//        Position position = new Position(10, 10);
//        int passengers = 100;
//        int mail = 50;
//        ProductType product = ProductType.GRAINS;
//
//        // Act
//        City city = controller.createCity(cityName, position, passengers, mail, product);
//
//        // Assert
//        assertNotNull(city);
//        assertEquals(cityName, city.getName());
//        assertEquals(position, city.getPosition());
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