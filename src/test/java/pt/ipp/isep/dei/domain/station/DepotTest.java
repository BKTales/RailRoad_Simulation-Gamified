package pt.ipp.isep.dei.domain.station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.position.*;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import static org.junit.jupiter.api.Assertions.*;

public class DepotTest {
    private static final int X = 100;
    private static final int Y = 100;
    private static Location LOC1;
    private static Location LOC2;
    private static Location LOC3;

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
        LOC1 = new Location(new Position(10, 10));
        LOC2 = new Location(new Position(0, 0));
        LOC3 = new Location(new Position(0, 2));
        ALFA = new Depot("Alfa", LOC1, X, Y);
        BRAVO = new Depot("Bravo", LOC2, X, Y);
        CHARLIE = new Depot("Charlie", LOC3, X, Y);
    }

    @Test
    public void testCreateArea_withinBounds() {

        // When
        InfluenceArea area = ALFA.getArea();

        assertEquals(8, area.getArea().size());
        assertFalse(area.getArea().contains(new Position(10, 10)));
        // Todas as posições esperadas
        assertTrue(area.getArea().contains(new Position(9, 9)));
        assertTrue(area.getArea().contains(new Position(10, 9)));
        assertTrue(area.getArea().contains(new Position(11, 9)));
        assertTrue(area.getArea().contains(new Position(9, 10)));
        assertTrue(area.getArea().contains(new Position(11, 10)));
        assertTrue(area.getArea().contains(new Position(9, 11)));
        assertTrue(area.getArea().contains(new Position(10, 11)));
        assertTrue(area.getArea().contains(new Position(11, 11)));
    }

    @Test
    public void testCreateArea_nearEdges_doesNotExceedBounds() {

        // When
        InfluenceArea area = BRAVO.getArea();

        // Then
        // (0,0) center — should only include (0,1), (1,0), (1,1)
        assertEquals(3, area.getArea().size());
        assertTrue(area.getArea().contains(new Position(1, 1)));
        assertFalse(area.getArea().contains(new Position(0, 0))); // center
        assertTrue(area.getArea().contains(new Position(0, 1)));
    }

    @Test
    public void testToString_returnsExpectedFormat() {

        // When
        String result = CHARLIE.toString();

        assertEquals("Depot Charlie at (0,2)", result);
    }

    @Test
    public void testCompareTo_sameDepot_returnsZero() {
        Depot d1 = new Depot("DepotA", new Location(new Position(2, 2)), 10, 10);
        Depot d2 = new Depot("DepotB", new Location(new Position(2, 2)), 10, 10);

        assertEquals(0, d1.compareTo(d2));
    }

    @Test
    public void testCompareTo_differentLocation_returnsOne() {
        Depot d1 = new Depot("DepotA", new Location(new Position(2, 2)), X, Y);
        Depot d2 = new Depot("DepotB", new Location(new Position(3, 3)), X, Y);

        assertEquals(1, d1.compareTo(d2));
    }


}
