package pt.ipp.isep.dei;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;

class ProductTest {

    // US05 – test Product creation and value modification
    @Test
    void testProductInitialValues() {
        Product prod = new Product(ProductType.GRAINS);
        assertEquals(ProductType.GRAINS, prod.getProductType());
        assertEquals(15.0, prod.getProductValue());
        assertEquals("GRAINS", prod.getProductName());
        assertEquals(3, prod.getTimeToProduce());
    }

    @Test
    void testChangeProductValueAndTime() {
        Product prod = new Product(ProductType.COFFEE);
        prod.changeProductValue(2.5);
        assertEquals(15.0 * 2.5, prod.getProductValue());
        prod.setTimeToProduce(120);
        assertEquals(120, prod.getTimeToProduce());
    }
}
