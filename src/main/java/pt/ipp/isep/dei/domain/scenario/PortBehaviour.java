package pt.ipp.isep.dei.domain.scenario;

import pt.ipp.isep.dei.domain.ProductType;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the behavior of a port regarding the types of products it imports and exports.
 */
public class PortBehaviour implements Serializable {
    private static final long serialVersionUID = 1L;
    List<ProductType> importedProducts;
    ProductType exportedProduct;

    /**
     * Constructs a PortBehaviour instance with the specified imported and exported products.
     *
     * @param importedProducts a list of product types that the port imports
     * @param exportedProduct the product type that the port exports
     */
    public PortBehaviour(List<ProductType> importedProducts, ProductType exportedProduct){
        this.importedProducts = importedProducts;
        this.exportedProduct = exportedProduct;
    }

    /**
     * Returns the list of product types imported by the port.
     *
     * @return list of imported product types
     */
    public List<ProductType> getImportedProducts() {
        return importedProducts;
    }

    /**
     * Returns the product type exported by the port.
     *
     * @return exported product type
     */
    public ProductType getExportedProduct() {
        return exportedProduct;
    }

}
