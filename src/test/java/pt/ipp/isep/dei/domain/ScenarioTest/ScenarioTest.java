package pt.ipp.isep.dei.domain.ScenarioTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.domain.train.LocomotiveModel;
import pt.ipp.isep.dei.domain.train.LocomotiveType;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.industries.Industry;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.rails.RailType;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.rails.TrackType;
import pt.ipp.isep.dei.domain.scenario.*;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.station.Depot;
import pt.ipp.isep.dei.domain.station.Station;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.domain.station.Terminal;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.repository.StationRepository;
import pt.ipp.isep.dei.us004.ScenarioCreationAssembler;
import pt.ipp.isep.dei.us004.ScenarioCreationController;
import pt.ipp.isep.dei.us004.ScenarioCreationDTO;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioTest {

    private static ScenarioCreationController controller;
    private static ScenarioCreationAssembler assembler;
    private static ScenarioCreationDTO dto;

    @BeforeAll
    static void setup(){

        controller = new ScenarioCreationController();
        assembler = new ScenarioCreationAssembler(controller);
        dto = new ScenarioCreationDTO();

        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        simulator.createScenario("TestScenario", simulator.createMap("Test1Map", 100, 100, 10), timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        Simulation simulation = Simulation.getInstance(scenario);
    }

    @Test
    void testCreateScenario_WithoutMap_ThrowsException() {
        // Arrange
        dto.setScenarioName("Invalid Scenario");
        dto.setAttachedMapName(null);
        dto.setStartYear(1900);
        dto.setEndYear(1950);
        dto.setInitialBudget(100000.0);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                assembler.createScenarioFromDTO(dto));
    }

    @Test
    void testValidateDTO_MissingRequiredFields_ThrowsException() {
        // Missing scenario name
        ScenarioCreationDTO invalidDto = new ScenarioCreationDTO();
        invalidDto.setAttachedMapName("Europe");
        invalidDto.setStartYear(1900);
        invalidDto.setEndYear(1950);
        invalidDto.setInitialBudget(100000.0);

        assertThrows(IllegalArgumentException.class, () ->
                assembler.validateDTO(invalidDto));

        // Missing map name
        invalidDto.setScenarioName("Test");
        invalidDto.setAttachedMapName(null);
        assertThrows(IllegalArgumentException.class, () ->
                assembler.validateDTO(invalidDto));

        // Missing years
        invalidDto.setAttachedMapName("Europe");
        invalidDto.setStartYear(null);
        assertThrows(IllegalArgumentException.class, () ->
                assembler.validateDTO(invalidDto));

        // Invalid budget
        invalidDto.setStartYear(1900);
        invalidDto.setInitialBudget(-100.0);
        assertThrows(IllegalArgumentException.class, () ->
                assembler.validateDTO(invalidDto));
    }

//    @Test
//    void createIndustry() {
//        // Arrange
//        Scenario scenario = new Scenario("Test", new Map("Test", 100, 100, 10),
//                new TimeRestrictions(1900, 1950), null, null, null, new Budget(1000));
//        Location location = new Location(new Position(10, 10));
//
//        // Act
//        Industry industry = scenario.createIndustry(ProductType.COFFEE, location);
//
//        // Assert
//        assertNotNull(industry);
//        assertEquals(ProductType.COFFEE, industry.getProduct());
//    }

    @Test
    void createRailwayLine() {
        // Arrange
        Scenario scenario = new Scenario("Test", new Map("Test", 100, 100, 10),
                new TimeRestrictions(1900, 1950), null, null, null, new Budget(1000));

        Location location1 = new Location(new Position(10, 10));
        Location location2 = new Location(new Position(20, 10));
        RailType railType = RailType.NON_ELECTRIFIED;
        TrackType trackType = TrackType.DOUBLE_RAIL;

        // Act
        Depot depot1 = new Depot("Depot1",  location1, 10, 20);
        Depot depot2 = new Depot("Depot1",  location2, 10, 20);
        assertNotNull(depot1);
        assertNotNull(depot2);

        RailwayLine railwayLine = new RailwayLine(depot1,depot2, railType, trackType,10);
        assertNotNull(railwayLine);
        assertEquals(trackType, railwayLine.getTrackType());
        assertEquals(railType, railwayLine.getRailType());
    }

    @Test
    void createStation(){
        Scenario scenario = new Scenario("Test", new Map("Test", 100, 100, 10),
                new TimeRestrictions(1900, 1950), null, null, null, new Budget(1000));

        Location location1 = new Location(new Position(10, 10));
        Location location2 = new Location(new Position(20, 10));
        Location location3 = new Location(new Position(11, 10));

        Industry industry = scenario.createIndustry(ProductType.GRAINS, location3);
        StationType station1 = scenario.createStation("Porto", "DEPOT", location1, null, 100, 100);
        StationType station2 = scenario.createStation("Porto", "DEPOT", location2, null, 100, 100);

        String expected = " [0] - GRAINS (Available: 0)\n";
        assertEquals(expected,station1.listOfAvailableProducts());
        assertNotNull(station1);
        assertNotNull(station2);
    }

    @Test
    void testDepotToStationUpgrade() {
        // Arrange
        Scenario scenario = new Scenario("Test", new Map("Test", 100, 100, 10),
                new TimeRestrictions(1900, 1950), null, null, null, new Budget(1000));

        Location location = new Location(new Position(10,20));

        StationRepository stationRepository = scenario.getStationRepository();
        Depot depot = new Depot("Depot1",  location, 10, 20);
        stationRepository.addStation(depot);

        // Act
        scenario.passDataFromStation(depot, 15, 25, "DEPOTTOSTATION", "NE");

        StationType result = stationRepository.getStation("Depot1");

        // Assert
        assertInstanceOf(Station.class, result);
        Station newStation = (Station) result;
        assertEquals("Depot1", newStation.getName());
        assertEquals(location, newStation.getLocation());
        assertTrue(stationRepository.getStations().contains(newStation));
        stationRepository.deleteStation(depot);
        assertFalse(stationRepository.getStations().contains(depot));
    }

    @Test
    void testDepotToTerminalUpgrade() {
        // Arrange
        Scenario scenario = new Scenario("Test", new Map("Test", 100, 100, 10),
                new TimeRestrictions(1900, 1950), null, null, null, new Budget(1000));

        Location location = new Location(new Position(10,20));

        StationRepository stationRepository = scenario.getStationRepository();
        Depot depot = new Depot("Depot1",  location, 10, 20);
        stationRepository.addStation(depot);

        // Act
        scenario.passDataFromStation(depot, 15, 25, "DEPOTTOTERMINAL", null);

        StationType result = stationRepository.getStation("Depot1");
        // Assert
        assertInstanceOf(Terminal.class, result);
        Terminal newStation = (Terminal) result;
        assertEquals("Depot1", newStation.getName());
        assertEquals(location, newStation.getLocation());
        assertTrue(stationRepository.getStations().contains(newStation));
        stationRepository.deleteStation(depot);
        assertFalse(stationRepository.getStations().contains(depot));
    }


    @Test
    void testStationToTerminalUpgrade() {
        // Arrange
        Scenario scenario = new Scenario("Test", new Map("Test", 100, 100, 10),
                new TimeRestrictions(1900, 1950), null, null, null, new Budget(1000));

        Location location = new Location(new Position(10,20));

        StationRepository stationRepository = scenario.getStationRepository();
        Station depot = new Station("Station1", "NE" ,location, 10, 20);
        stationRepository.addStation(depot);

        // Act
        scenario.passDataFromStation(depot, 15, 25, "STATIONTOTERMINAL", null);
        StationType result = stationRepository.getStation("Station1");
        // Assert
        assertInstanceOf(Terminal.class, result);
        Terminal newStation = (Terminal) result;
        assertEquals("Station1", newStation.getName());
        assertEquals(location, newStation.getLocation());
        assertTrue(stationRepository.getStations().contains(newStation));
        stationRepository.deleteStation(depot);
        assertFalse(stationRepository.getStations().contains(depot));
    }



}