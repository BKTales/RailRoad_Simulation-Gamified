package pt.ipp.isep.dei.domain.station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.position.*;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalTest {
    private static final int X = 100;
    private static final int Y = 100;
    private static Location LOC1;
    private static Location LOC2;
    private static Location LOC3;

    private static Terminal ALFA;
    private static Terminal BRAVO;
    private static Terminal CHARLIE;
    @BeforeAll
    static void setup() {
        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        simulator.createScenario("TestScenario", simulator.getMapRepository().getMap("TestMap"), timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        LOC1 = new Location(new Position(10, 10));
        LOC2 = new Location(new Position(0, 0));
        LOC3 = new Location(new Position(0, 2));
        ALFA = new Terminal("Alfa", LOC1, X, Y);
        BRAVO = new Terminal("Bravo", LOC2, X, Y);
        CHARLIE = new Terminal("Charlie", LOC3, X, Y);
    }

    @Test
    public void testCreateArea_withinBounds() {

        // When
        InfluenceArea area = ALFA.getArea();

        assertEquals(24, area.getArea().size());

        assertTrue(area.getArea().contains(new Position(8, 8)));
        assertTrue(area.getArea().contains(new Position(12, 12)));
        assertTrue(area.getArea().contains(new Position(9, 9)));
        assertTrue(area.getArea().contains(new Position(11, 11)));


        assertTrue(area.getArea().contains(new Position(10, 8)));
        assertTrue(area.getArea().contains(new Position(10, 12)));
        assertTrue(area.getArea().contains(new Position(8, 10)));
        assertTrue(area.getArea().contains(new Position(12, 10)));
    }

    @Test
    public void testCreateArea_nearEdges_doesNotExceedBounds() {

        // When
        InfluenceArea area = BRAVO.getArea();

        // Then
        // (0,0) center — should only include (0,1), (1,0), (1,1)
        assertEquals(8, area.getArea().size());
        assertTrue(area.getArea().contains(new Position(0, 1)));
        assertTrue(area.getArea().contains(new Position(0, 2)));
        assertTrue(area.getArea().contains(new Position(1, 0)));
        assertTrue(area.getArea().contains(new Position(1, 1)));
        assertTrue(area.getArea().contains(new Position(1, 2)));
        assertTrue(area.getArea().contains(new Position(2, 0)));
        assertTrue(area.getArea().contains(new Position(2, 1)));
        assertTrue(area.getArea().contains(new Position(2, 2)));

        // Não pode conter o centro
        assertFalse(area.getArea().contains(new Position(0, 0)));
    }

    @Test
    public void testToString_returnsExpectedFormat() {

        // When
        String result = CHARLIE.toString();

        assertEquals("Terminal Charlie at (0,2)", result);
    }

    @Test
    public void testCompareTo_sameDepot_returnsZero() {
        Terminal d1 = new Terminal("TerminalA", new Location(new Position(2, 2)), X, Y);
        Terminal d2 = new Terminal("TerminalB", new Location(new Position(2, 2)), X, Y);

        assertEquals(0, d1.compareTo(d2));
    }

    @Test
    public void testCompareTo_differentLocation_returnsOne() {
        Terminal d1 = new Terminal("TerminalA", new Location(new Position(2, 2)), X, Y);
        Terminal d2 = new Terminal("TerminalB", new Location(new Position(3, 3)), X, Y);

        assertEquals(1, d1.compareTo(d2));
    }


}
