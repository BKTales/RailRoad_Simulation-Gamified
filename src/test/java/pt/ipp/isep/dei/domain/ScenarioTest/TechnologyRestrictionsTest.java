package pt.ipp.isep.dei.domain.ScenarioTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.scenario.TechnologyType;
import pt.ipp.isep.dei.domain.scenario.TechnologyRestriction;
import pt.ipp.isep.dei.us004.ScenarioCreationAssembler;
import pt.ipp.isep.dei.us004.ScenarioCreationController;
import pt.ipp.isep.dei.us004.ScenarioCreationDTO;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TechnologyRestrictionTest {

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
    void createTechnologyRestrictionWithValidList() {
        // Arrange
        List<TechnologyType> techList = Arrays.asList(TechnologyType.DIESEL, TechnologyType.ELECTRIC);

        // Act
        TechnologyRestriction restriction = new TechnologyRestriction(techList);

        // Assert
        assertEquals(2, restriction.getTechList().size());
        assertTrue(restriction.getTechList().contains(TechnologyType.DIESEL));
    }

    @Test
    void createTechnologyRestrictionWithEmptyList() {
        // Arrange
        List<TechnologyType> techList = List.of();

        // Act
        TechnologyRestriction restriction = new TechnologyRestriction(techList);

        // Assert
        assertTrue(restriction.getTechList().isEmpty());
    }

    @Test
    void testCreateTechnologyRestriction_ValidInput_Success() {
        // Arrange
        List<TechnologyType> restrictedTechs = Arrays.asList(TechnologyType.STEAM, TechnologyType.DIESEL);

        // Act
        controller.createTechnologicalRestriction(restrictedTechs);

        // Assert
        assertNotNull(controller.getTechnologyRestriction());
        assertEquals(2, controller.getTechnologyRestriction().getTechList().size());
        assertTrue(controller.getTechnologyRestriction().getTechList().contains(TechnologyType.STEAM));
        assertTrue(controller.getTechnologyRestriction().getTechList().contains(TechnologyType.DIESEL));
    }
}