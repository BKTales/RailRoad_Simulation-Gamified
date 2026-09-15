//package pt.ipp.isep.dei.repository;
//
//import org.junit.jupiter.api.Test;
//import pt.ipp.isep.dei.domain.position.Location;
//import pt.ipp.isep.dei.domain.position.Position;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class LocationRepositoryTest {
//
//    @Test
//    void createLocationValid() {
//        // Arrange
//        Repository r = new Repository();
//        LocationRepository lr = r.getLocationRepository();
//        r.getMapRepository().createMap("map", 10, 10);
//        r.setCurrentMap("map");
//        Position pos = new Position(1, 2);
//
//        // Act
//        Location l = lr.createLocation(pos);
//
//        // Assert
//        assertNotNull(l);
//    }
//
//    @Test
//    void createLocationInvalid() {
//        // Arrange
//        Repository r = new Repository();
//        LocationRepository lr = r.getLocationRepository();
//        r.getMapRepository().createMap("map", 10, 10);
//        r.setCurrentMap("map");
//        Position pos = new Position(1, 2);
//        Location l2 = null;
//
//        try {
//            // Act
//            Location l = lr.createLocation(pos);
//            l2 = lr.createLocation(pos);
//
//            // Assert
//            assertNull(l2);
//        } catch (Exception e) {
//            assertNull(l2);
//        }
//    }
//
//    @Test
//    void deleteLocationValid() {
//        // Arrange
//        Repository r = new Repository();
//        LocationRepository lr = r.getLocationRepository();
//        r.getMapRepository().createMap("map", 10, 10);
//        r.setCurrentMap("map");
//        Position pos = new Position(1, 2);
//        Location l = lr.createLocation(pos);
//
//        // Act
//        lr.deleteLocation(pos);
//
//        // Assert
//        assertNull(lr.getLocation(pos));
//    }
//
//    @Test
//    void deleteLocationInvalid() {
//        // Arrange
//        Repository r = new Repository();
//        LocationRepository lr = r.getLocationRepository();
//        r.getMapRepository().createMap("map", 10, 10);
//        r.setCurrentMap("map");
//        Position pos = new Position(1, 2);
//        Position pos2 = new Position(2, 4);
//        Location l1 = lr.createLocation(pos);
//        LocationRepository lr2 = lr;
//
//        // Act
//        lr.deleteLocation(pos2);
//
//        // Assert
//        assertEquals(lr, lr2);
//    }
//
//
//    @Test
//    void getLocationValid() {
//        // Arrange
//        Repository r = new Repository();
//        LocationRepository lr = r.getLocationRepository();
//        r.getMapRepository().createMap("map", 10, 10);
//        r.setCurrentMap("map");
//        Position pos = new Position(1, 2);
//        Location l = lr.createLocation(pos);
//
//        // Act
//        Location lTemp = lr.getLocation(pos);
//
//        // Assert
//        assertEquals(l, lTemp);
//    }
//
//    @Test
//    void getLocationInvalid() {
//        // Arrange
//        Repository r = new Repository();
//        LocationRepository lr = r.getLocationRepository();
//        r.getMapRepository().createMap("map", 10, 10);
//        r.setCurrentMap("map");
//        Position pos = new Position(1, 2);
//        Position posTemp = new Position(2, 4);
//        Location l = lr.createLocation(pos);
//
//        // Act
//        Location lTemp = lr.getLocation(posTemp);
//
//        // Assert
//        assertNull(lTemp);
//    }
//}