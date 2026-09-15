package pt.ipp.isep.dei.domain.ScenarioTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.scenario.PortBehaviour;
import pt.ipp.isep.dei.us004.ScenarioCreationAssembler;
import pt.ipp.isep.dei.us004.ScenarioCreationController;
import pt.ipp.isep.dei.us004.ScenarioCreationDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PortBehaviourTest {

    private ScenarioCreationController controller;
    private ScenarioCreationAssembler assembler;
    private ScenarioCreationDTO dto;

    @BeforeEach
    void setUp() {
        controller = new ScenarioCreationController();
        assembler = new ScenarioCreationAssembler(controller);
        dto = new ScenarioCreationDTO();
    }

    @Test
    void createPortBehaviour() {
        // Arrange
        List<ProductType> imported = Arrays.asList(ProductType.STEEL, ProductType.MAIL);
        ProductType exported = ProductType.GRAINS;

        // Act
        PortBehaviour behaviour = new PortBehaviour(imported, exported);

        // Assert
        assertEquals(2, behaviour.getImportedProducts().size());
        assertEquals(exported, behaviour.getExportedProduct());
    }

    @Test
    void testCreatePortBehavior_ValidInput_Success() {
        // Arrange
        List<Integer> importedProducts = Arrays.asList(0, 1); // Indices for imported products
        int exportedProductIndex = 2; // Index for exported product

        // Act
        controller.createPortBehaviour(importedProducts, exportedProductIndex);

        // Assert
        assertNotNull(controller.getPortBehaviour());
        assertEquals(2, controller.getPortBehaviour().getImportedProducts().size());
        assertNotNull(controller.getPortBehaviour().getExportedProduct());
    }

}