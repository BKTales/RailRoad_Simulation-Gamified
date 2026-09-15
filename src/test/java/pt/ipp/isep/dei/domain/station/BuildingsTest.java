package pt.ipp.isep.dei.domain.station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.station.Building;
import pt.ipp.isep.dei.domain.station.BuildingType;
import pt.ipp.isep.dei.domain.station.Buildings;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BuildingsTest {

    private Buildings buildings;

    @BeforeEach
    void setUp() {
        buildings = new Buildings();
    }

    @Test
    void testAddBuilding_NewBuilding_AddsSuccessfully() {
        String building = BuildingType.TELEGRAPH.name;
        buildings.addBuilding(building);
        assertTrue(buildings.existsBuildingType(BuildingType.TELEGRAPH));
    }

    @Test
    void testAddBuilding_DuplicateBuilding_DoesNotAdd() {
        String building1 = BuildingType.TELEGRAPH.name;
        String building2 = BuildingType.TELEGRAPH.name;

        buildings.addBuilding(building1);
        buildings.addBuilding(building2);

        ArrayList<Building> list = buildings.getAcquiredBuildings();
        assertEquals(1, list.size());
    }

    @Test
    void testAddBuilding_TelephoneReplacesTelegraph() {
        buildings.addBuilding(BuildingType.TELEGRAPH.name);
        buildings.addBuilding(BuildingType.TELEPHONE.name);

        assertFalse(buildings.existsBuildingType(BuildingType.TELEGRAPH));
        assertTrue(buildings.existsBuildingType(BuildingType.TELEPHONE));
    }

    @Test
    void testAddBuilding_CaffeeLargeReplacesCaffeeSmall() {
        buildings.addBuilding(BuildingType.CAFFEE_SMALL.name);
        buildings.addBuilding(BuildingType.CAFFEE_LARGE.name);

        assertFalse(buildings.existsBuildingType(BuildingType.CAFFEE_SMALL));
        assertTrue(buildings.existsBuildingType(BuildingType.CAFFEE_LARGE));
    }

    @Test
    void testGetAvailableBuildings_SmallNotAvailableIfLargeExists() {
        buildings.addBuilding(BuildingType.CAFFEE_LARGE.name);
        buildings.addBuilding(BuildingType.HOTEL_LARGE.name);
        int year = BuildingType.TELEPHONE.getStartYear();
        String available = buildings.getAvailableBuildings(year);
        assertFalse(available.contains(BuildingType.HOTEL_SMALL.name()));
        assertFalse(available.contains(BuildingType.CAFFEE_SMALL.name()));
    }


    @Test
    void testRemoveBuilding_RemovesSuccessfully() {
        String building = BuildingType.TELEGRAPH.name;
        buildings.addBuilding(building);
        Building building1 = buildings.getAcquiredBuildings().get(0);
        buildings.removeBuilding(building1);
        assertFalse(buildings.existsBuildingType(BuildingType.TELEGRAPH));
    }

    @Test
    void testExistsSmallVersion_WhenSmallExists_ReturnsTrue() {
        buildings.addBuilding(BuildingType.HOTEL_SMALL.name);
        Building required = new Building(BuildingType.HOTEL_LARGE);
        assertTrue(buildings.existsSmallVersion(required));
    }

    @Test
    void testExistsSmallVersion_WhenSmallNotExists_ReturnsFalse() {
        Building required = new Building(BuildingType.CAFFEE_LARGE);
        assertFalse(buildings.existsSmallVersion(required));
    }

    @Test
    void testGetAvailableBuildings_BasedOnCurrentYear() {
        int year = BuildingType.TELEGRAPH.getStartYear();
        String available = buildings.getAvailableBuildings(year);


        assertTrue(available.contains(BuildingType.TELEGRAPH.name));
        assertFalse(available.contains(BuildingType.TELEPHONE.name));
    }

    @Test
    void testGetAvailableBuildings_AfterTelephoneYear_TelegraphUnavailable() {
        int year = BuildingType.TELEPHONE.getStartYear();
        String available = buildings.getAvailableBuildings(year);

        assertFalse(available.contains(BuildingType.TELEGRAPH.name));
    }

    @Test
    void testGetAcquiredBuildings_ReturnsCopyNotOriginal() {
        buildings.addBuilding(BuildingType.HOTEL_SMALL.name);
        ArrayList<Building> copy = buildings.getAcquiredBuildings();
        copy.clear();

        assertEquals(1, buildings.getAcquiredBuildings().size()); // internal list remains unchanged
    }
}
