package pt.ipp.isep.dei.domain.Locomotive;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.train.LocomotiveModel;
import pt.ipp.isep.dei.domain.train.LocomotiveType;

class LocomotiveModelTest {

    // US09 – test LocomotiveModel enum values
    @Test
    void testLocomotiveModelAttributes() {

        //Arrange
        int startYear = 1800;
        double acquisitionPrice = 10000;
        int topSpeed = 10;
        int maintenance = 6000;
        int fuelCoast = 7543;
        String name = "TREVITHICK 1";
        LocomotiveType type = LocomotiveType.STEAM_LOCOMOTIVE;

        //Act
        LocomotiveModel model = LocomotiveModel.TREVITHICK_1;

        //Assert
        assertEquals(startYear,model.getStartYear());
        assertEquals(acquisitionPrice, model.getAcquisitionPrice());
        assertEquals(topSpeed, model.getTopSpeed());
        assertEquals(maintenance, model.getMaintenance());
        assertEquals(fuelCoast, model.getFuelCoast());
        assertEquals(name, model.getName());
        assertEquals(type, model.getType());

    }
}
