package pt.ipp.isep.dei.domain.industries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;

class ConsumedProductsTest {

    // US15 – test ConsumedProducts add and list
    @Test
    void testConsumedProductsList() {
        Product p1 = new Product(ProductType.COFFEE);
        ConsumedProducts cp = new ConsumedProducts(p1);
        assertEquals(1, cp.getProducts().size());
        cp.addProduct(new Product(ProductType.MAIL));
        assertEquals(2, cp.getProducts().size());
        String list = cp.listProducts();
        assertTrue(list.contains(p1.toString()));
    }
}
