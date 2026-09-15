package pt.ipp.isep.dei.domain.rails;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.station.Depot;

import pt.ipp.isep.dei.domain.simulation.Simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RailwayLineTest {
    private static final int X = 100;
    private static final int Y = 100;
    private static Location LOC1;
    private static Location LOC2;
    private static Location LOC3;

    private static final RailType RAIL_TYPE = RailType.NON_ELECTRIFIED;
    private static final TrackType TRACK_TYPE = TrackType.DOUBLE_RAIL;

    private static Depot ALFA;
    private static Depot BRAVO;
    private static Depot CHARLIE;

    @BeforeAll
    static void setup() {
        Simulation instance = Simulation.getInstance();
        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        simulator.createScenario("TestScenario", simulator.getMapRepository().getMap("TestMap"), timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        Simulation simulation = Simulation.getInstance(scenario);
        LOC1 = new Location(new Position(0, 0));
        LOC2 = new Location(new Position(0, 1));
        LOC3 = new Location(new Position(0, 2));
        ALFA = new Depot("Alfa", LOC1, X, Y);
        BRAVO = new Depot("Bravo", LOC2, X, Y);
        CHARLIE = new Depot("Charlie", LOC3, X, Y);
    }

    @Test
    void testConstructorAndGetters() {
        RailwayLine line = new RailwayLine(ALFA, BRAVO, RAIL_TYPE, TRACK_TYPE, 1);

        assertEquals(ALFA, line.getStation1());
        assertEquals(BRAVO, line.getStation2());
        assertEquals(RAIL_TYPE, line.getRailType());
        assertEquals(TRACK_TYPE, line.getTrackType());
        assertEquals(1, line.getDistance());
    }

    @Test
    void testCompareTo_DifferentFirstStationNames() {
        RailwayLine line1 = new RailwayLine(ALFA, BRAVO, RAIL_TYPE, TRACK_TYPE, 1);
        RailwayLine line2 = new RailwayLine(CHARLIE, BRAVO, RAIL_TYPE, TRACK_TYPE, 1);

        assertTrue(line1.compareTo(line2) < 0);
        assertTrue(line2.compareTo(line1) > 0);
    }

    @Test
    void testCompareTo_SameFirstStation_DifferentSecondStation() {
        RailwayLine line1 = new RailwayLine(ALFA, BRAVO, RAIL_TYPE, TRACK_TYPE, 1);
        RailwayLine line2 = new RailwayLine(ALFA, CHARLIE, RAIL_TYPE, TRACK_TYPE, 2);

        assertTrue(line1.compareTo(line2) < 0);
        assertTrue(line2.compareTo(line1) > 0);
    }

    @Test
    void testToStringShouldContainRelevantInfo() {
        RailwayLine line = new RailwayLine(ALFA, BRAVO, RAIL_TYPE, TRACK_TYPE, 1);
        String result = line.toString();

        assertTrue(result.contains("Alfa"));
        assertTrue(result.contains("Bravo"));
        assertTrue(result.contains("NON_ELECTRIFIED"));
        assertTrue(result.contains("DOUBLE_RAIL"));
        assertTrue(result.contains(String.valueOf(1)));
    }

}
