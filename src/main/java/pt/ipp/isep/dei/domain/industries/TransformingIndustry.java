package pt.ipp.isep.dei.domain.industries;

import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.train.Cargo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an industry in the transforming sector that processes raw materials into finished products.
 * <p>
 * This class extends the generic Industry class and defines the products that this industry consumes.
 */
public class TransformingIndustry extends Industry implements Serializable {
    private static final long serialVersionUID = 1L;
    private ConsumedProducts productToConsume; //store the products that are consumed to produce the industry product
    private List<Product> storage;

    /**
     * Constructs a TransformingIndustry with the specified product, industry type, location,
     * production time, and maximum production capacity.
     *
     * @param product       The product produced by this industry.
     * @param type          The industry type.
     * @param location      The geographic location of the industry.
     * @param timeToProduce The time (in seconds) required to produce the product.
     * @param maxProduction The maximum production capacity of this industry.
     */
    public TransformingIndustry(Product product, IndustryType type, Location location, int timeToProduce, int maxProduction) {
        super(product, type, location, timeToProduce, maxProduction);
        this.sector = IndustrySector.TRANSFORMING;
        storage = new ArrayList<>();
        isProducing = false;
        setProductToConsume(product);
    }

    /**
     * Function will, when receving the cargo, check if any of the ones in it, is of its consumed products,
     * if so it will take it from the cargo and add it to the storage.
     * @param cargo cargo from the trip.
     */
    public void receiveCargo(ArrayList<Cargo> cargo) {
        ArrayList<Product> products = productToConsume.getProducts();

        if (products == null)
            return;

        for (Cargo c : cargo){
            for (Product tmp : products) {
                if (tmp.equals(c.getProduct())) {
                    storage.add(c.getProduct());
                    cargo.remove(c);
                }
            }
        }

    }

    // =================== Generate =================== //

    /**
     * Function will check if any product was produced and if not, it will do nothing, and if it has
     * it will increase the produced quantity and turn to false the isProducing
     * @param currentTime timeNow
     * @return stirng with the status if it produced  or nota
     */
    @Override
    public String updateProduction(long currentTime) {
        String output = "";
        if (hasEnoughProducts()){
            storage.clear(); // change it!
            producedQuantity++;
            isProducing = false;
            output += type.toString() + location.getPosition().toString() + " has " + producedQuantity + " of " + product.getProductName() + "\n";
        }
        return (output);
    }

    // =================== Something else  =================== //

    /**
     * Function will say if industry has enough storage to produce its product
     * @return
     */
    public boolean hasEnoughProducts() {
        boolean []hasProduct;
        int i = 0;

        hasProduct = new boolean[productToConsume.getProducts().size()];
        for (Product tmp : productToConsume.getProducts()) {
            for (Product tmp2 : storage) {
                if (tmp.equals(tmp2))
                    hasProduct[i] = true;
            }
            i++;
        }
        for (int j = 0; j < i; j++) {
            if (!hasProduct[j]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the products that this industry consumes based on the product type.
     * <p>
     * Note: The current implementation has duplicate checks for CLOTHING and may be extended for other products.
     *
     * @param p The product to be consumed.
     */
    private void setProductToConsume(Product p) {
        if (p.getProductType() == ProductType.BREAD)
            productToConsume = new ConsumedProducts(new Product(ProductType.GRAINS));
        else if (p.getProductType() == ProductType.CLOTHING)
            productToConsume = new ConsumedProducts(new Product(ProductType.WOOL));
        else if (p.getProductType() == ProductType.CAR){
            productToConsume = new ConsumedProducts(new Product(ProductType.STEEL));
            productToConsume.addProduct(new Product(ProductType.STEEL));
            productToConsume.addProduct(new Product(ProductType.RUBBER));
        }
        else if (p.getProductType() == ProductType.STEEL) {
            productToConsume = new ConsumedProducts(new Product(ProductType.IRON));
            productToConsume.addProduct(new Product(ProductType.COAL));
        }
    }

    /**
     * Returns a detailed string of this industry's production status,
     * including the products it consumes.
     *
     * @return A string describing production details and consumed products.
     */
    @Override
    public String getProductionDetails() {
        String baseDetails = super.getProductionDetails();
        return baseDetails + String.format(" | Consumes: %s", productToConsume.toString());
    }

    /**
     * Calculate the need of a certain product inside this industry
     * @param product product that is available to be sent to this industry
     * @return quantity that can be sent
     */
    public int quantityNeededOfProduct(ProductType product) {
        int quantityNeeded = 0;

        ArrayList<Product> p = productToConsume.getProducts();
        for (Product pTemp : productToConsume.getProducts()){
            if (pTemp.getProductType() == product)
                quantityNeeded++; // many needed to produce
        }
        quantityNeeded -= productsInStorageOfType(product);
        return (quantityNeeded);
    }

    /**
     * Function gets how many objects of given type are in the storage
     * @param productType type to search for in storage
     * @return objects in storage quantity
     */
    private int productsInStorageOfType(ProductType productType) {
        int quantity = 0;
        for (Product pTemp : storage) {
            if (pTemp.getProductType() == productType){
                quantity++;
            }
        }
        return quantity;
    }

    /**
     * add the wanted products to the storage of the industry
     * @param products
     */
    public void addProductsToBeConsumed(ArrayList<Product> products) {
        for (Product p : products) {
            storage.add(p);
        }
    }

    /**
     * Returns a string representation of this transforming industry.
     *
     * @return A string describing this transforming industry.
     */
    @Override
    public String toString() {
        return "TransformingIndustry " + super.toString();
    }
}
