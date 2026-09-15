package pt.ipp.isep.dei.domain.CityTest;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    @Test
    void testCreateMap() {
        // Arrange
        Simulator s = Simulator.getInstance();
        String name = "mapName";

        // Act
        Map returned = s.createMap(name, 10, 10, 10);

        // Assert
        assertInstanceOf(Map.class, returned);
    }

    @Test
    void testCreateMapSameName() {
        // Arrange
        Simulator s = Simulator.getInstance();
        String name = "mapSameName";

        // Act
        Map returned1 = s.createMap(name, 10, 10, 10);
        try {
            Map returned2 = s.createMap(name, 10, 10, 10);
        }
        catch(IllegalArgumentException e) {

        // Assert
            assert true;
            return ;
        }
        assert false;
    }

    @Test
    void testCreateMapDifferentName() {
        // Arrange
        Simulator s = Simulator.getInstance();
        String name = "mapDifferentName";
        String name2 = "mapDifferentName2";

        // Act
        Map returned1 = s.createMap(name, 10, 10, 10);
        try {
            Map returned2 = s.createMap(name2, 10, 10, 10);
        }
        catch(IllegalArgumentException e) {

            // Assert
            assert false;
            return ;
        }
        assert true;
    }

    @Test
    void testConstructorTypeClass() {
        // Arrange
        Simulator s = Simulator.getInstance();

        // Act
        Map map = s.createMap("mapTestConstructorTypeClass", 10, 10, 10);

        // Assert
        assertInstanceOf(Map.class, map);
    }

    @Test
    void testGetName() {
        // Arrange
        Simulator s = Simulator.getInstance();
        String name = "mapNameTestGetName";

        // Act
        Map map = s.createMap(name, 10, 10, 10);

        // Assert
        assertEquals(name, map.getName());
    }

    @Test
    void testGetLength() {
        // Arrange
        Simulator s = Simulator.getInstance();
        int length = 10;

        // Act
        Map map = s.createMap("mapNameTestGetLength", 10, length, 10);

        // Assert
        assertEquals(length, map.getLength());
    }

    @Test
    void testGetHeight() {
        // Arrange
        Simulator s = Simulator.getInstance();
        int height = 10;

        // Act
        Map map = s.createMap("mapNameTestGetHeight", height, 10, 10);

        // Assert
        assertEquals(height, map.getHeight());

    }

    @Test
    void testGetArea() {
        // Arrange
        Simulator s = Simulator.getInstance();
        int height = 10;
        int length = 10;

        // Act
        Map map = s.createMap("mapNameTestGetArea", height, length, 10);

        // Assert
        assertEquals((height * length), map.getMapArea());
    }
}