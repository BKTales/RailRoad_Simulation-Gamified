package pt.ipp.isep.dei.domain.station;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuildingTest {
    @Test
    public void testConstructor_initializesCorrectly() {

        BuildingType type = BuildingType.TELEPHONE;


        Building building = new Building(type);


        assertEquals("TELEPHONE", building.getName());
        assertEquals(2.0, building.getMoneyMultiplier());
        assertEquals(type, building.getBuildingType());
    }
}
