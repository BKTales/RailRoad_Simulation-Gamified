package pt.ipp.isep.dei.domain.station;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.industries.Industry;
import pt.ipp.isep.dei.domain.industries.IndustryType;
import pt.ipp.isep.dei.domain.industries.PrimaryIndustry;
import pt.ipp.isep.dei.domain.position.HouseBlock;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.rails.RailType;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.rails.TrackType;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class StationTypeTest {
    private static final int X = 100;
    private static final int Y = 100;
    private static Location LOC1;
    private static Location LOC2;
    private static Location LOC3;
    private static Depot ALFA;
    private static Depot BRAVO;
    private static Depot CHARLIE;
    private static final RailType RAIL_TYPE = RailType.NON_ELECTRIFIED;
    private static final TrackType TRACK_TYPE = TrackType.DOUBLE_RAIL;

    @BeforeAll
    public static void setup() {

        Simulator simulator = Simulator.getInstance();
        TimeRestrictions timeRestrictions = new TimeRestrictions(1920, 1925);
        simulator.createScenario("TestScenario", simulator.getMapRepository().getMap("TestMap"), timeRestrictions, null, null, null, null);
        Scenario scenario = simulator.getScenarioRepository().setActiveScenario("TestScenario");
        Simulation simulation = Simulation.getInstance(scenario);
        LOC1 = new Location(new Position(10, 13));
        LOC2 = new Location(new Position(10, 15));
        LOC3 = new Location(new Position(10, 17));
        ALFA = new Depot("ALFA",LOC1,X,Y);
        BRAVO = new Depot("BRAVO",LOC2,X,Y);
        CHARLIE = new Depot("CHARLIE",LOC3,X,Y);
    }

    @Test
    public void testValidTerminalConstruction() {
        Terminal terminal = new Terminal("MyTerminal", LOC1,X,Y);

        assertEquals("MyTerminal", terminal.getName());
        assertEquals(LOC1, terminal.getLocation());
        assertNotNull(terminal.getAcquiredBuildings());
        assertNotNull(terminal.getRailwayLines());
    }

    @Test
    public void testValidDepotConstruction() {
        Depot depot = new Depot("MyDepot", LOC1,X,Y);
        assertEquals("MyDepot", depot.getName());
        assertEquals(LOC1, depot.getLocation());
        assertNotNull(depot.getAcquiredBuildings());
        assertNotNull(depot.getRailwayLines());
    }

    @Test
    public void testNullNameThrowsException() {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Depot(null, LOC1,X,Y);
        });

        assertEquals("Name cannot be null or empty", ex.getMessage());
    }

    @Test
    public void testEmptyNameThrowsException() {

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Depot("", LOC1,X,Y);
        });

        assertEquals("Name cannot be null or empty", ex.getMessage());
    }

    @Test
    public void testNullLocationThrowsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Depot("TestDepot", null,X,Y);
        });

        assertEquals("Location cannot be null", ex.getMessage());
    }

    @Test
    void testGetNextStationFromStation1() {
        RailwayLine line = new RailwayLine(ALFA, BRAVO, RAIL_TYPE, TRACK_TYPE, 10.0);
        StationType next = ALFA.getNextStation(line);

        assertEquals(BRAVO, next, "Should return the second station when called from the first station");
    }

    @Test
    void testGetNextStationFromStation2() {
        RailwayLine line = new RailwayLine(ALFA, BRAVO, RAIL_TYPE, TRACK_TYPE, 10.0);
        StationType next = BRAVO.getNextStation(line);

        assertEquals(ALFA, next, "Should return the first station when called from the second station");
    }

    @Test
    void testGetNextStationThrowsWhenStationNotInLine() {
        RailwayLine line = new RailwayLine(ALFA, BRAVO, RAIL_TYPE, TRACK_TYPE, 10.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CHARLIE.getNextStation(line);
        });

        assertEquals("This station is not part of the given railway line.", exception.getMessage());
    }

    @Test
    void testGetInfo() {

        StationType depot = new Depot("Depot", LOC1, X,Y);


        String b1 = BuildingType.CAFFEE_LARGE.name;
        String b2 = BuildingType.TELEGRAPH.name;
        depot.getAcquiredBuildings().addBuilding(b1);
        depot.getAcquiredBuildings().addBuilding(b2);


        String info = depot.getInfo();

        assertTrue(info.contains("Name: Depot"));
        assertTrue(info.contains("Position: (10,13)"));
        assertTrue(info.contains("Buildings: LARGE_CAFFEE, TELEGRAPH"));
        assertTrue(info.contains("InfluenceArea: (9,12), (9,13), (9,14), (10,12), (10,14), (11,12), \n" +
                "(11,13), (11,14)"));
        assertTrue(info.contains("Objects in Area: "));
    }

    @Test
    void testListOfAvailableProductsWithIndustryAndHouseBlock() {
        Position position = new Position(10, 12);
        Location location1 = new Location(position);
        Position position1 = new Position(9, 14);
        Location location2 = new Location(position1);


        Industry industry = new PrimaryIndustry(new Product(ProductType.GRAINS), IndustryType.FARM,location2,10,10);
        HouseBlock houseBlock = new HouseBlock(location1);


        Set<Object> inArea = new HashSet<>();
        inArea.add(industry);
        inArea.add(houseBlock);

        ALFA.setObjectsInArea(inArea);
        String output = ALFA.listOfAvailableProducts();

        assertTrue(output.contains("[0] - GRAINS"));
        assertTrue(output.contains("[1] - MAIL"));
        assertTrue(output.contains("[2] - PEOPLE"));

    }

    @Test
    void testInitializeDemandValues() {

        Position position = new Position(11, 14);
        Location location1 = new Location(position);
        Position position1 = new Position(10, 14);
        Location location2 = new Location(position1);
        Product mailProduct = new Product(ProductType.MAIL);
        Product peopleProduct = new Product(ProductType.PEOPLE);

        Industry industry = new PrimaryIndustry(new Product(ProductType.GRAINS), IndustryType.FARM,location2,10,10);
        HouseBlock houseBlock = new HouseBlock(location1);


        Set<Object> inArea = new HashSet<>();
        inArea.add(industry);
        inArea.add(houseBlock);

        BRAVO.setObjectsInArea(inArea);

        BRAVO.initializeDemandValues();


        // Verificar produtos especiais
        assertTrue(BRAVO.getDemand().containsKey(mailProduct));
        assertEquals(5, BRAVO.getDemand().get(mailProduct));

        assertTrue(BRAVO.getDemand().containsKey(peopleProduct));
        assertEquals(5, BRAVO.getDemand().get(peopleProduct));
    }

    @Test
    void testListOfAvailableProductsWithIndustryAndCity() {
        Position position = new Position(10, 12);
        Location location1 = new Location(position);
        Position position1 = new Position(9, 14);
        Location location2 = new Location(position1);


        Industry industry = new PrimaryIndustry(new Product(ProductType.GRAINS), IndustryType.FARM,location2,10,10);
        City city = new City("city1",location1,100,100,ProductType.WOOL);


        Set<Object> inArea = new HashSet<>();
        inArea.add(industry);
        inArea.add(city);

        ALFA.setObjectsInArea(inArea);


        String output = ALFA.listOfAvailableProducts();


        assertTrue(output.contains("[0] - GRAINS"));
        assertTrue(output.contains("[1] - MAIL"));
        assertTrue(output.contains("[2] - PEOPLE"));

    }

    @Test
    void testRefreshAnualCargoeValues() {
        Product mailProduct = new Product(ProductType.MAIL);
        Product peopleProduct = new Product(ProductType.PEOPLE);
        BRAVO.getDemand().put(mailProduct, 8);
        BRAVO.getDemand().put(peopleProduct, 8);
        BRAVO.refreshAnualCargoeValues();

        assertEquals(7, BRAVO.getDemand().get(mailProduct));
        assertEquals(7, BRAVO.getDemand().get(peopleProduct));

    }

}
