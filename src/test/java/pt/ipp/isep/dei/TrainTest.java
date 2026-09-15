package pt.ipp.isep.dei;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.train.Cargo;
import pt.ipp.isep.dei.domain.train.LocomotiveModel;
import pt.ipp.isep.dei.domain.train.Train;

class TrainTest {

    // US11 – test Train toString, cargo loading, and location
    @Test
    void testTrainToStringAndCargo() {
        Train train = new Train(LocomotiveModel.TREVITHICK_1, 42);
        assertEquals(42, train.getId());
        assertEquals(LocomotiveModel.TREVITHICK_1, train.getLocomotive().getModel());
        assertEquals("inventory", train.getWhereItIs());
        assertNull(train.getCargo());
        String noCargoStr = train.toString();
        assertTrue(noCargoStr.contains("No cargo"));

        Cargo cargo = new Cargo(new Product(ProductType.COFFEE), 3);
        //train.setCargo(cargo);
        //assertEquals(cargo, train.getCargo());
        //String withCargoStr = train.toString();
        //assertTrue(withCargoStr.contains("Cargo: 3 x COFFEE"));
    }
}
