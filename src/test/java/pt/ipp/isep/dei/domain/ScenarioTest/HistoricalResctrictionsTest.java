package pt.ipp.isep.dei.domain.ScenarioTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRestrictions {
    private pt.ipp.isep.dei.domain.scenario.ProductRestrictions historicalRestrictions;

    @BeforeEach
    void setUp() {
        historicalRestrictions = new pt.ipp.isep.dei.domain.scenario.ProductRestrictions();
    }

    @Test
    void addProductRestriction_shouldAddNewRestriction() {
        // Arrange
        String productName = "TestProduct";
        int timeToProduce = 5;
        double multiplier = 1.5;

        // Act
        historicalRestrictions.addProductRestriction(productName, timeToProduce, multiplier);

        // Assert
        pt.ipp.isep.dei.domain.scenario.ProductRestrictions.ProductRestriction restriction =
                historicalRestrictions.getProductRestriction(productName);
        assertNotNull(restriction);
        assertEquals(timeToProduce, restriction.getTimeToProduce());
        assertEquals(multiplier, restriction.getMultiplier(), 0.001);
    }

    @Test
    void addProductRestriction_shouldOverrideExistingRestriction() {
        // Arrange
        String productName = "TestProduct";
        historicalRestrictions.addProductRestriction(productName, 5, 1.5);

        // Act
        historicalRestrictions.addProductRestriction(productName, 10, 2.0);

        // Assert
        pt.ipp.isep.dei.domain.scenario.ProductRestrictions.ProductRestriction restriction =
                historicalRestrictions.getProductRestriction(productName);
        assertEquals(10, restriction.getTimeToProduce());
        assertEquals(2.0, restriction.getMultiplier(), 0.001);
    }

    @Test
    void removeProductRestriction_shouldRemoveExistingRestriction() {
        // Arrange
        String productName = "TestProduct";
        historicalRestrictions.addProductRestriction(productName, 5, 1.5);

        // Act
        historicalRestrictions.removeProductRestriction(productName);

        // Assert
        assertNull(historicalRestrictions.getProductRestriction(productName));
    }

    @Test
    void removeProductRestriction_shouldDoNothingForNonExistentProduct() {
        // Arrange
        String productName = "NonExistentProduct";

        // Act
        historicalRestrictions.removeProductRestriction(productName);

        // Assert
        assertNull(historicalRestrictions.getProductRestriction(productName));
    }

    @Test
    void getProductRestriction_shouldReturnNullForNonExistentProduct() {
        // Arrange
        String productName = "NonExistentProduct";

        // Act
        pt.ipp.isep.dei.domain.scenario.ProductRestrictions.ProductRestriction restriction =
                historicalRestrictions.getProductRestriction(productName);

        // Assert
        assertNull(restriction);
    }

    @Test
    void getAllProductsWithRestrictions_shouldReturnEmptyListForNoRestrictions() {
        // Act
        List<String> products = historicalRestrictions.getAllProductsWithRestrictions();

        // Assert
        assertTrue(products.isEmpty());
    }

    @Test
    void getAllProductsWithRestrictions_shouldReturnAllProductsWithRestrictions() {
        // Arrange
        historicalRestrictions.addProductRestriction("Product1", 5, 1.5);
        historicalRestrictions.addProductRestriction("Product2", 10, 2.0);

        // Act
        List<String> products = historicalRestrictions.getAllProductsWithRestrictions();

        // Assert
        assertEquals(2, products.size());
        assertTrue(products.contains("Product1"));
        assertTrue(products.contains("Product2"));
    }

    @Test
    void productRestriction_shouldStoreTimeAndMultiplier() {
        // Arrange
        int timeToProduce = 5;
        double multiplier = 1.5;

        // Act
        pt.ipp.isep.dei.domain.scenario.ProductRestrictions.ProductRestriction restriction =
                new pt.ipp.isep.dei.domain.scenario.ProductRestrictions.ProductRestriction(timeToProduce, multiplier);

        // Assert
        assertEquals(timeToProduce, restriction.getTimeToProduce());
        assertEquals(multiplier, restriction.getMultiplier(), 0.001);
    }
}