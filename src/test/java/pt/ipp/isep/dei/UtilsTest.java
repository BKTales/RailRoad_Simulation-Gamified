package pt.ipp.isep.dei;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.utils.Utils;

class UtilsTest {

    // US04 – test getCurrentTime method
    @Test
    void testGetCurrentTimeMonotonic() throws InterruptedException {
        long t1 = Utils.getCurrentTime();
        Thread.sleep(100);
        long t2 = Utils.getCurrentTime();
        assertTrue(t2 >= t1);
    }
}
