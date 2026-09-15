//package pt.ipp.isep.dei.repository;
//
//import org.junit.jupiter.api.Test;
//import pt.ipp.isep.dei.domain.Map;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class MapRepositoryTest {
//
//    @Test
//    void createMap() {
//        // Arrange
//        MapRepository mr = new MapRepository();
//        mr.createMap("map1", 100, 200);
//
//        // Act
//        Map m = mr.getMap("map1");
//
//        // Assert
//        assertNull(m);
//    }
//
//    @Test
//    void deleteMapValidName() {
//        // Arrange
//        String mapName = "map1";
//        MapRepository mr1 = new MapRepository();
//        mr1.createMap("map1", 100, 200);
//        mr1.createMap("map2", 100, 200);
//        mr1.createMap("map3", 100, 200);
//
//        MapRepository oldMr1 = mr1;
//
//        // Act
//        mr1.deleteMap(mapName);
//
//        // Assert
//        assertNull(mr1.getMap(mapName));
//    }
//
//    @Test
//    void deleteMapInvalidName() {
//        // Arrange
//        MapRepository mr1 = new MapRepository();
//        mr1.createMap("map1", 100, 200);
//        mr1.createMap("map2", 100, 200);
//        mr1.createMap("map3", 100, 200);
//
//        MapRepository oldMr1 = mr1;
//
//        // Act
//        mr1.deleteMap("map1123");
//
//
//        // Assert
//        assertEquals(mr1, oldMr1);
//    }
//
//    @Test
//    void getMapValidName() {
//        // Arrange
//        MapRepository mr = new MapRepository();
//        mr.createMap("map1", 100, 200);
//        mr.createMap("map2", 100, 200);
//        mr.createMap("map3", 100, 200);
//
//        // Act
//        Map mTemp = mr.getMap("map1");
//
//        // Assert
//        assertEquals(mr.getMap("map1"), mTemp);
//    }
//
//    @Test
//    void getMapWrongName() {
//        // Arrange
//        MapRepository mr = new MapRepository();
//        mr.createMap("map1", 100, 200);
//        mr.createMap("map2", 100, 200);
//        mr.createMap("map3", 100, 200);
//
//        // Act
//        Map mTemp = mr.getMap("map5");
//
//        // Assert
//        assertNull(mTemp);
//    }
//}