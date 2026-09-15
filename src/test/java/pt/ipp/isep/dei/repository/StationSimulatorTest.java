package pt.ipp.isep.dei.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.domain.station.StationType;

import static org.junit.jupiter.api.Assertions.*;

public class StationSimulatorTest {

    private StationRepository stationRepository;
    private static final int X = 100;
    private static final int Y = 100;
    private static Location LOC1;
    private static Location LOC2;
    private static Location LOC3;
    private  Scenario scenario;
    private Simulation simulation;
    private static int index;

    @BeforeEach
    public void setup() {
        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        simulator.createScenario("TestScenario", simulator.createMap("TestMap" + index, 100, 100, 10), timeRestrictions, null, null, null, null);
        scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        simulation = Simulation.getInstance(scenario);
        stationRepository = simulation.getStationRepository();
        stationRepository.clean();
        LOC1 = new Location(new Position(5, 5));
        LOC2 = new Location(new Position(6, 5));
        LOC3 = new Location(new Position(5, 6));
        index++;
    }

    @Test
    public void testCreateStationAddsStation() {
        StationType station1 = scenario.createStation("Lisboa", "STATION", LOC1, "NE", X, Y);
        assertNotNull(station1);
        assertEquals("Lisboa", station1.getName());
        assertEquals(1, stationRepository.getStations().size());

        StationType station2 = scenario.createStation("Lisboa", "DEPOT", LOC2, null, X,Y);
        assertNotNull(station2);
        assertEquals("Lisboa", station2.getName());
        assertEquals(2, stationRepository.getStations().size());

        StationType station3 = scenario.createStation("Lisboa", "TERMINAL", LOC3, null, X,Y);
        assertNotNull(station3);
        assertEquals("Lisboa", station3.getName());
        assertEquals(3, stationRepository.getStations().size());
    }

    @Test
    public void testDeleteStationByPosition() {
        StationType station1 = scenario.createStation("Porto", "DEPOT", LOC1, null, X, Y);
        StationType station2 = scenario.createStation("Porto", "DEPOT", LOC2, null, X, Y);

        assertEquals(2, stationRepository.getStations().size());

        stationRepository.deleteStation(LOC1.getPosition());
        assertEquals(1, stationRepository.getStations().size());

        stationRepository.deleteStation(station2);
        assertEquals(0, stationRepository.getStations().size());
    }

    @Test
    public void testGetStationByName() {
        scenario.createStation("Coimbra", "TERMINAL", LOC2, null, X, Y);

        StationType found = stationRepository.getStation("Coimbra");
        assertNotNull(found);
        assertEquals("Coimbra", found.getName());
    }

    @Test
    public void testGetAvailableUpgradeStationDepot() {
        StationType depot = scenario.createStation("Depot1", "DEPOT", LOC1, null, X, Y);

        String upgrades = stationRepository.getAvailableUpgradeStation(depot);
        String upgrades2 =  " [0] - DEPOTTOSTATION, price:50000,00\n" +
                " [1] - DEPOTTOTERMINAL, price:150000,00\n";
        assertEquals(upgrades2, upgrades);
    }

    @Test
    public void testGetAvailableUpgradeStationStation() {
        StationType station = scenario.createStation("Station1", "STATION", LOC2, "NE", X, Y);

        String upgrades = stationRepository.getAvailableUpgradeStation(station);
        String upgrades2 = " [0] - STATIONTOTERMINAL, price:100000,00\n";
        assertEquals(upgrades2,upgrades);
    }

    @Test
    public void testGetAvailableUpgradeStationTerminal() {
        StationType terminal = scenario.createStation("Terminal1", "TERMINAL", LOC3, null, X, Y);

        String upgrades = stationRepository.getAvailableUpgradeStation(terminal);
        assertEquals("", upgrades);
    }

    @Test
    public void testGetListOfStationTypes() {
        String expected =
                " [0] - DEPOT, price:50000,00\n" +
                        " [1] - STATION, price:100000,00\n" +
                        " [2] - TERMINAL, price:200000,00\n";

        String actual = stationRepository.getListOfStationTypes();
        assertEquals(expected, actual);
    }


    @Test
    public void testListAllStations() {
        //Arrange
        StationType station1 = scenario.createStation("North Station","DEPOT", new Location(new Position(0, 0)),null,X,Y);
        StationType station2 = scenario.createStation("South Station","STATION", new Location(new Position(5, 10)),"NE",X,Y);


        String expected =
                "[0] Depot North Station at (0,0)\n" +
                        "[1] Station South Station at (5,10)\n";
        //Act
        String actual = stationRepository.listAllStations();

        //Assert
        assertEquals(expected, actual);
    }

}
