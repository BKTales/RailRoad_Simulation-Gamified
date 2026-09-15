package pt.ipp.isep.dei;//package pt.ipp.isep.dei.domain.train;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.train.Locomotive;
import pt.ipp.isep.dei.domain.train.LocomotiveModel;
import pt.ipp.isep.dei.domain.train.LocomotiveType;

class LocomotiveTest {

    // US09 – test Locomotive creation from model and properties
    @Test
    void testLocomotiveProperties() {
        Locomotive loco = new Locomotive(LocomotiveModel.TREVITHICK_1);
        assertEquals("TREVITHICK 1", loco.getName());
        assertEquals(LocomotiveType.STEAM_LOCOMOTIVE, loco.getLocomotiveType());
        assertEquals(10.0, loco.getTopSpeed());
        assertEquals(10000.0, loco.getAcquisitionPrice());
        String s = loco.toString();
        assertTrue(s.contains("TREVITHICK 1"));
    }
}
