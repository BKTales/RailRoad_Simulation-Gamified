package pt.ipp.isep.dei.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.domain.station.Depot;

import static org.junit.jupiter.api.Assertions.*;

public class RailwayLineRepositoryTest {

    private static final int X = 100;
    private static final int Y = 100;
    private static Depot ALFA;
    private static Depot BRAVO;
    private static Depot CHARLIE;
    private static Location LOC1;
    private static Location LOC2;
    private static Location LOC3;
    private static RailwaylineRepository railwaylineRepository;
    private static int index = 0;
    private Scenario scenario;
    private Simulation simulation;
    private Map map;
    private int scale;

    @BeforeEach
     void setup() {

        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 2000);
        simulator.createScenario("TestScenario", simulator.createMap("TestMapp" + index, 100, 100, 10), timeRestrictions, null, null, null, null);
        scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        map = scenario.getAttachedMap();
        scale = map.getScale();
        simulation = Simulation.getInstance(scenario);
        railwaylineRepository = simulation.getRailwaylineRepository();
        LOC1 = new Location(new Position(5, 5));
        LOC2 = new Location(new Position(6, 5));
        LOC3 = new Location(new Position(5, 6));
        index++;
        ALFA = new Depot("Alfa", LOC1, X, Y);
        BRAVO = new Depot("Bravo", LOC2, X, Y);
        CHARLIE = new Depot("Charlie", LOC3, X, Y);
        railwaylineRepository = scenario.getRailwaylineRepository();
        railwaylineRepository.getRails().clear();
    }

    @Test
    void testCreateRailwayLine() {
        RailwayLine line = scenario.createRailwayLine(ALFA, BRAVO, "SINGLE_RAIL", "NON_ELECTRIFIED", 22.0);
        assertEquals(1, railwaylineRepository.getRails().size());
        assertEquals(ALFA, line.getStation1());
        assertEquals(BRAVO, line.getStation2());
    }

    @Test
    void testAlreadyExists_trueAndFalseCases() {
        scenario.createRailwayLine(ALFA, CHARLIE, "DOUBLE_RAIL", "NON_ELECTRIFIED", 15.0);

        assertTrue(railwaylineRepository.alreadyExists(ALFA, CHARLIE));
        assertTrue(railwaylineRepository.alreadyExists(CHARLIE, ALFA));
        assertFalse(railwaylineRepository.alreadyExists(BRAVO, CHARLIE));
    }

    @Test
    void testCalculateDistanceBetweenStations() {

        double dist = RailwayLine.calculateDistBetweenStations(ALFA, BRAVO,scale);

        assertEquals(10, dist); // (|6 - 5| + |5 - 5|) * 10 = 10
    }

    @Test
    void testDeleteByIndexAndByObject() {
        RailwayLine line = scenario.createRailwayLine(BRAVO, CHARLIE,"DOUBLE_RAIL", "ELECTRIFIED", 1.0);

        int index = railwaylineRepository.getRails().indexOf(line);
        railwaylineRepository.deleteRailwayLine(index);

        assertFalse(railwaylineRepository.getRails().contains(line));

        // Add again and delete by object
        RailwayLine line2 = scenario.createRailwayLine(BRAVO, CHARLIE, "DOUBLE_RAIL", "ELECTRIFIED", 1.0);
        railwaylineRepository.deleteRailwayLine(line2);

        assertFalse(railwaylineRepository.getRails().contains(line2));
    }

    @Test
    void testGetRailwayLineBetween() {
        RailwayLine line = scenario.createRailwayLine(ALFA, BRAVO, "SINGLE_RAIL", "ELECTRIFIED", 22.0);
        RailwayLine found = railwaylineRepository.getRailwayLineBetween(BRAVO, ALFA);

        assertEquals(line, found);
    }

    @Test
    void testGetListOfAvailableRailwayLineTypes() {
        String rails = railwaylineRepository.getListOfAvailableRailwayLineTypes(scenario.getTimeRestrictions().getStartYear());
        assertFalse(rails.isEmpty());
        assertTrue(rails.contains("NON_ELECTRIFIED"));
        assertTrue(rails.contains("ELECTRIFIED"));
    }

    @Test
    void testGetListOfRailwayTrackTypes() {
        String result = railwaylineRepository.getListOfRailwayTrackTypes();
        assertTrue(result.contains("SINGLE_RAIL"));
        assertTrue(result.contains("DOUBLE_RAIL"));
    }

    @Test
    void testListRailwayLinesFormatting() {
        scenario.createRailwayLine(ALFA, CHARLIE, "SINGLE_RAIL", "NON_ELECTRIFIED", 21.0);
        String output = railwaylineRepository.listRailwayLines();

        assertTrue(output.contains("RailwayLine"));
        assertTrue(output.contains("Alfa"));
        assertTrue(output.contains("Charlie"));
    }
}
