package pt.ipp.isep.dei.domain.station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.position.*;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;

import static org.junit.jupiter.api.Assertions.*;

public class StationTest {
    private static final int X = 100;
    private static final int Y = 100;
    private static Location LOC1;
    private static Location LOC2;
    private static Location LOC3;
    private static String CARD_POS1;
    private static String CARD_POS2;
    private static String CARD_POS3;

    private static Station ALFA;
    private static Station BRAVO;
    private static Station CHARLIE;
    private static Scenario scenario;

    @BeforeAll
    static void setup() {
        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        simulator.createScenario("TestScenario", simulator.getMapRepository().getMap("TestMap"), timeRestrictions, null, null, null, null);
        scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        LOC1 = new Location(new Position(10, 10));
        LOC2 = new Location(new Position(0, 0));
        LOC3 = new Location(new Position(0, 2));
        CARD_POS1 = "NE";
        CARD_POS2 = "NW";
        CARD_POS3 = "SW";
        ALFA = new Station("Alfa",CARD_POS1, LOC1, X, Y);
        BRAVO = new Station("Bravo", CARD_POS2,LOC2, X, Y);
        CHARLIE = new Station("Charlie", CARD_POS3,LOC3, X, Y);
    }

    @Test
    public void testValidStationConstruction() {
        Station station = new Station("MyStation",CARD_POS1, LOC1,X,Y);

        assertEquals("MyStation", station.getName());
        assertEquals(LOC1, station.getLocation());
        assertNotNull(station.getAcquiredBuildings());
        assertNotNull(station.getRailwayLines());
    }

    @Test
    public void testNullCardinalPositionThrowsException() {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Station("MyStation",null, LOC1,X,Y);
        });

        assertEquals("Cardinal position cannot be null or empty!", ex.getMessage());
    }

    @Test
    public void testToString_returnsExpectedFormat() {

        // When
        String result = CHARLIE.toString();

        assertEquals("Station Charlie at (0,2)", result);
    }

    @Test
    public void testCreateArea_withinBounds() {

        // When
        InfluenceArea area = ALFA.getArea();

        assertEquals(15, area.getArea().size());
        assertFalse(area.getArea().contains(new Position(10, 10)));
        assertTrue(area.getArea().contains(new Position(11, 11)));
        assertTrue(area.getArea().contains(new Position(11, 9)));
        assertTrue(area.getArea().contains(new Position(11, 8)));
        assertTrue(area.getArea().contains(new Position(11, 10)));
        assertTrue(area.getArea().contains(new Position(10, 11)));
        assertTrue(area.getArea().contains(new Position(10, 9)));
        assertTrue(area.getArea().contains(new Position(10, 8)));
        assertTrue(area.getArea().contains(new Position(9, 9)));
        assertTrue(area.getArea().contains(new Position(9, 8)));
        assertTrue(area.getArea().contains(new Position(9, 10)));
        assertTrue(area.getArea().contains(new Position(9, 11)));
        assertTrue(area.getArea().contains(new Position(8, 9)));
        assertTrue(area.getArea().contains(new Position(8, 8)));
        assertTrue(area.getArea().contains(new Position(8, 11)));
        assertTrue(area.getArea().contains(new Position(8, 10)));

    }

    @Test
    public void testCreateArea_nearEdges_doesNotExceedBounds() {

        // When
        InfluenceArea area = BRAVO.getArea();

        // Then
        // (0,0) center — should only include (0,1), (1,0), (1,1)
        assertEquals(5, area.getArea().size());
        assertFalse(area.getArea().contains(new Position(0, 0))); // center
        assertTrue(area.getArea().contains(new Position(0, 1)));
        assertTrue(area.getArea().contains(new Position(0, 2)));
        assertTrue(area.getArea().contains(new Position(1, 0)));
        assertTrue(area.getArea().contains(new Position(1, 1)));
        assertTrue(area.getArea().contains(new Position(1, 2)));
    }



    @Test
    public void testCompareTo_sameDepot_returnsZero() {
        Station d1 = new Station("DepotA",CARD_POS1, new Location(new Position(2, 2)), 10, 10);
        Station d2 = new Station("DepotB",CARD_POS2, new Location(new Position(2, 2)), 10, 10);

        assertEquals(0, d1.compareTo(d2));
    }

    @Test
    public void testCompareTo_differentLocation_returnsOne() {
        Station d1 = new Station("DepotA", CARD_POS1, new Location(new Position(2, 2)), X, Y);
        Station d2 = new Station("DepotB",CARD_POS2, new Location(new Position(3, 3)), X, Y);

        assertEquals(1, d1.compareTo(d2));
    }

    @Test
    void testGetInfo() {

        String b1 = BuildingType.CAFFEE_LARGE.name;
        String b2 = BuildingType.TELEGRAPH.name;
        ALFA.getAcquiredBuildings().addBuilding(b1);
        ALFA.getAcquiredBuildings().addBuilding(b2);


        String info = ALFA.getInfo();


        assertTrue(info.contains("Name: Alfa"));
        assertTrue(info.contains("Position: (10,10)"));
        assertTrue(info.contains("Buildings: LARGE_CAFFEE, TELEGRAPH"));
        assertTrue(info.contains("InfluenceArea: (8,11), (9,11), (10,11), (11,11), (8,10), (9,10), (11,10), (8,9), \n" +
                "(9,9), (10,9), (11,9), (8,8), (9,8), (10,8), (11,8)"));
        assertTrue(info.contains("Objects in Area: "));
        assertTrue(info.contains("Cardinal Position of the Center: NE"));
    }
}
