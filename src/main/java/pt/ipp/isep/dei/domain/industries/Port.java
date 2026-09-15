package pt.ipp.isep.dei.domain.industries;

import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.train.Cargo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Port industry which produces a product and imports a list of product types.
 * <p>
 * Extends the generic Industry class with specifics for a port,
 * including imported products and consumption management.
 */
public class Port extends Industry implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int TIME_BETWEEN_SHEEPMENTS = 5; // every 5s a new shipment is going to arrive in the Port

    private List<ProductType> importedProducts; // products that the port will bring in to the simulation
    private int[] importedProductsQuantity; // products that the port will bring in to the simulation

    private ConsumedProducts productToConsume; // can be null if there is nothing to be consumed!
    private List<Product> storage;  // if the product is a secondary product it will need storage to save the

    /**
     * Constructs a Port industry with given product, imported products, location,
     * production time, and maximum production capacity.
     *
     * @param product          The product produced by the port.
     * @param importedProducts List of product types that the port imports.
     * @param location         The geographic location of the port.
     * @param timeToProduce    The time (in seconds) required to produce the product.
     * @param maxProduction    The maximum production capacity of the port.
     */
    public Port(Product product, List<ProductType> importedProducts, Location location, int timeToProduce, int maxProduction) {
        super(product, IndustryType.PORT, location, timeToProduce, maxProduction);
        this.importedProducts = importedProducts;
        this.sector = IndustrySector.MIXED;
        storage = new ArrayList<>();
        setProductToConsume(product);
        importedProductsQuantity = new int[importedProducts.size()];
    }

    /**
     * Sets the products that this industry consumes based on the product type.
     * <p>
     * Note: The current implementation has duplicate checks for CLOTHING and may be extended for other products.
     *
     * @param p The product to be consumed.
     */
    private void setProductToConsume(Product p)     {
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
        else
            productToConsume = null;
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

    // =============== Update =============== //

    /**
     * Will update the production from port and the items that were imported and give
     * a report of that
     * @param currentTime currentTime sent from timeHandler
     * @return update made report
     */
    @Override
    public String updateProduction(long currentTime) {
        String output = "";
        if (productToConsume == null){
            output += updatePrimaryProduct(currentTime);
        }
        else{
            output += updateTransformedProduct(currentTime);
        }

        if ((currentTime - productionStartTime) >= TIME_BETWEEN_SHEEPMENTS){
            output += updateImportedProduct(currentTime);
        }

        return output;
    }

    // =================== other stuff =================== //

    /**
     * In case of the product being a primary, this function will refresh the production
     * @param currentTime currentTime sent from timeHandler
     * @return update made report
     */
    public String updatePrimaryProduct(long currentTime) {
        String toReturn = "";
        long manyProduced = 0;

        if (producedQuantity < maxProduction) {
            manyProduced = (currentTime - productionStartTime) / timeToProduce;
            if (manyProduced == 1) {
                producedQuantity++;
                productionStartTime = currentTime;
                toReturn = type.toString() + " at " + location.getPosition().toString()
                        + " has " + producedQuantity + " of " + product.getProductType().productName + "\n";
            }
        }
        return toReturn;
    }

    /** In case of the product being a primary, this function will refresh the production
     * @param currentTime currentTime sent from timeHandler
     * @return update made report
     */
    public String updateTransformedProduct(long currentTime) {
        String output = "";
        if (hasEnoughProducts()){
            storage.clear(); // change it!
            producedQuantity++;
            isProducing = false;
            output += type.toString() + location.getPosition().toString() + " has " + producedQuantity + " of " + product.getProductName() + "\n";
        }
        return (output);
    }

    /**  In case of the product being a primary, this function will refresh the production
     * @param currentTime currentTime sent from timeHandler
     * @return update made report
     */
    public String updateImportedProduct(long currentTime) {
        String output = type.toString() + location.getPosition().toString() + " has ";
        productionStartTime = currentTime;
        for (int i = 0; i < importedProductsQuantity.length; i++) {
            importedProductsQuantity[i]++;
            output +=  importedProductsQuantity[i] + " of " + importedProducts.get(i).productName + " ";
        }
        output += "\n";
        return  (output);
    }

    /**
     * Function will say if industry has enough storage to produce its product
     * @return (true - if there is enough products) (false - if there is not)
     */
    public boolean hasEnoughProducts() {
        boolean []hasProduct;

        hasProduct = new boolean[productToConsume.getProducts().size()];
        int i = 0;
        for (Product tmp : productToConsume.getProducts()) {
            for (Product tmp2 : storage) {
                if (tmp.equals(tmp2)) {
                    hasProduct[i] = true;
                }
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
     * add the wanted products to the storage of the industry
     * @param products
     */
    public void addProductsToGoToStorage(ArrayList<Product> products) {
        for (Product p : products) {
            storage.add(p);
        }
    }

     /**
     * Function will throw the given items in the "void"/consume them
     * @param products - products to be consumed
     */
    public void addProductsToBeConsumed(ArrayList<Product> products) {
        for (Product p : products) {
            storage.add(p);
        }
    }

    // =============== Logic =============== //

    /**
     * Function will tell if the port has primary or secondary product
     * @return (true - if is primary product) (false - if it is not)
     */
    public boolean isPrimaryProduct(){
        if (productToConsume == null)
            return true;
        return false;
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


    // =============== Getters =============== //

    /**
     * Returns the products that this port needs to consume.
     *
     * @return The consumed products or null if not set.
     */
    public ConsumedProducts getProductToConsume() {
        return productToConsume;
    }

    /**
     * Returns the list of product types that this port imports.
     *
     * @return List of imported product types.
     */
    public List<ProductType> getImportedProducts() {
        return importedProducts;
    }

    /**
     * Provides a detailed string representation of the port's production status,
     * including location, product produced, quantity, max production, and imported products.
     *
     * @return String describing production details of the port.
     */
    public String getProductionDetails() {
        return String.format(
                "PORT at %s | Producing: %s (Qty: %d/%d) | Imports: %s",
                getLocation().getPosition(),
                getProduct().getProductType().productName,
                getProducedQuantity(),
                getMaxProduction(),
                getImportedProducts().stream()
                        .map(p -> p.productName)
                        .collect(Collectors.joining(", "))
        );
    }

    /**
     * Returns the maximum production capacity of the port.
     *
     * @return The max production quantity.
     */
    private int getMaxProduction() {
        return maxProduction;
    }

    /**
     * Returns a string representation of the port industry.
     *
     * @return A string describing the port.
     */
    @Override
    public String toString() {
        return "Port " + super.toString();
    }
}
