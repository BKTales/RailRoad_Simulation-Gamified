package pt.ipp.isep.dei;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.ProductType;

class ProductTypeTest {

    // US02 – verify product type values and properties
    @Test
    void testProductTypeAttributes() {
        ProductType grains = ProductType.GRAINS;
        assertEquals(15, grains.productValue);
        assertEquals("GRAINS", grains.productName);
        assertEquals(3, grains.timeToProduce);

        ProductType rubber = ProductType.RUBBER;
        assertEquals(15, rubber.productValue);
        assertEquals("RUBBER", rubber.productName);
        assertEquals(5, rubber.timeToProduce);
    }

    @Test
    void testAllProductTypesExist() {
        String[] expectedNames = {"GRAINS", "VEGETABLES", "COFFEE", "RUBBER", "WOOL",
                "IRON", "COAL", "STEEL", "BREAD", "CAR",
                "CLOTHING", "PEOPLE", "MAIL"};
        assertEquals(expectedNames.length, ProductType.values().length);
        for (ProductType pt : ProductType.values()) {
            assertNotNull(pt.productName);
        }
    }
}
