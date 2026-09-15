package pt.ipp.isep.dei.domain.station;

import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.industries.Industry;
import pt.ipp.isep.dei.domain.industries.Port;
import pt.ipp.isep.dei.domain.industries.PrimaryIndustry;
import pt.ipp.isep.dei.domain.industries.TransformingIndustry;
import pt.ipp.isep.dei.domain.position.HouseBlock;
import pt.ipp.isep.dei.domain.position.InfluenceArea;
import pt.ipp.isep.dei.domain.position.Location;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.rails.RailwayLines;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.io.Serializable;
import java.util.*;

/**
 * Represents an abstract type of railway station with shared characteristics and behaviors.
 * <p>
 * Each {@code StationType} has a name, a defined location, an area of influence, and
 * maintains a record of nearby elements such as industries and house blocks. It also manages
 * available products that can be provided by those entities.
 * </p>
 */
public abstract class StationType implements Comparable<StationType>, Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private InfluenceArea area;
    private Location location;
    private RailwayLines railwayLines;
    private Buildings acquiredBuildings;
    private Set <Object> inArea;
    public Set<Product> availableProducts = new HashSet<>();
    private ArrayList<Industry> validIndustries = new ArrayList<>();
    private ArrayList<HouseBlock> validHouseBlocks = new ArrayList<>();
    private ArrayList<City> validCities = new ArrayList<>();
    private List<PrimaryIndustry> primaryIndustries = new ArrayList<>();
    private List<TransformingIndustry> transformingIndustries = new ArrayList<>();
    private List<Port> ports = new ArrayList<>();
    private static final Product MAIL_PRODUCT = new Product(ProductType.MAIL);
    private static final Product PEOPLE_PRODUCT = new Product(ProductType.PEOPLE);
    private Map<Product, Integer> storedProducts = new HashMap<>();
    private Map<Product, Integer> anualCargoes = new HashMap<>();
    private Map<Product, Integer> demand = new HashMap<>();
    private static final int MAX_DEMAND_LEVEL = 9;
    private static final int MIN_DEMAND_LEVEL = 0;
    private static final int MED_DEMAND_LEVEL = 5;
    private static final int MAX_CARGOES_LEVEL = 30;
    private static final int MIN_CARGOES_LEVEL = 10;


    /**
     * Constructs a station with a name and location.
     * @param name     the station's name, cannot be null or empty
     * @param location the station's location, cannot be null
     * @throws IllegalArgumentException if name is null/empty or location is null
     */
    public StationType(String name, Location location) {
        if (name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (location == null){
            throw new IllegalArgumentException("Location cannot be null");
        }
        this.name = name;
        this.location = location;
        this.acquiredBuildings = new Buildings();
        this.railwayLines = new RailwayLines();
    }

    /**
     * Abstract method to create the station's influence area.
     * Must be implemented by subclasses.
     *
     * @param maxHeight maximum height of the map
     * @param maxWidth  maximum width of the map
     * @return the created influence area
     */
    abstract InfluenceArea createArea(int maxHeight, int maxWidth);

    // =========== Station logic =========== //

    /**
     * Initializes the influence area of the station using maximum dimensions of the map.
     *
     * @param maxHeight maximum height of the map
     * @param maxWidth  maximum width of the map
     */
    public void initArea(int maxHeight, int maxWidth) {
        this.area = createArea(maxHeight, maxWidth);
    }

    /**
     * Initializes available products and categorizes entities in the area.
     *
     * <p>Iterates over the objects in the area (inArea), and:
     * <ul>
     *   <li>If the object is an Industry, adds its product to available products and stores the industry in validIndustries.</li>
     *   <li>If the object is a HouseBlock, adds mail and people products to available products and stores the house block in validHouseBlocks.</li>
     *   <li>If the object is a City, adds mail and people products to available products and stores the city in validCities.</li>
     * </ul>
     * Then classifies the valid industries into primary industries, transforming industries, and ports.
     */
    public void initializeProducts(){
        if (inArea != null) {
            for (Object obj : inArea) {
                if (obj instanceof Industry) {
                    Industry industry = (Industry) obj;
                    Product product = industry.getProduct();
                    availableProducts.add(product);
                    validIndustries.add(industry);
                } else if (obj instanceof HouseBlock) {
                    HouseBlock houseBlock = (HouseBlock) obj;
                    availableProducts.add(MAIL_PRODUCT);
                    availableProducts.add(PEOPLE_PRODUCT);
                    validHouseBlocks.add(houseBlock);
                }  else if (obj instanceof City) {
                    City city = (City) obj;
                    availableProducts.add(MAIL_PRODUCT);
                    availableProducts.add(PEOPLE_PRODUCT);
                    validCities.add(city);
                }
            }

            for (Industry industry : validIndustries) {
                if (industry instanceof PrimaryIndustry) {
                    primaryIndustries.add((PrimaryIndustry) industry);
                } else if (industry instanceof TransformingIndustry) {
                    transformingIndustries.add((TransformingIndustry) industry);
                } else if (industry instanceof Port) {
                    ports.add((Port) industry);
                }
            }
        }
    }

    /**
     * Sets the map of objects present in the influence area.
     *
     * @param inArea a map from Position to objects within the area
     */
    public void setObjectsInArea(Set<Object> inArea) {
        this.inArea = inArea;
        initializeProducts();
        initializeDemandValues();
    }

    /**
     * Adds a railway line to this station.
     *
     * @param line the railway line to add
     */
    public void addRailwayLines(RailwayLine line){
        railwayLines.addRailwayline(line);
    }

    // =========== Demand logic =========== //

    /**
     * Initializes the demand map with default demand levels for products required by
     * transforming industries, ports, cities, and house blocks within the area.
     *
     * <p>This method populates the demand map with products and sets their initial demand level
     * to a medium value (MED_DEMAND_LEVEL) if they are not already present in the map.
     *
     * <p>The demand is initialized for:
     * <ul>
     *   <li>Primary products required by transforming industries.</li>
     *   <li>Primary products required by ports.</li>
     *   <li>Products consumed by valid cities, including mail and people products.</li>
     *   <li>Mail and people products for valid house blocks.</li>
     * </ul>
     */
    public void initializeDemandValues() {
        if (transformingIndustries != null) {
            for (TransformingIndustry p : transformingIndustries) {
                ProductType targetProduct = p.getProductType();
                ArrayList<ProductType> requiredTypes = Simulation.getInstance()
                        .getCurrentScenario().getPrimaryProduct(targetProduct);
                for (ProductType required : requiredTypes) {
                    Product product = new Product(required);
                    demand.putIfAbsent(product, MED_DEMAND_LEVEL);
                }
            }
        }

        if (ports != null) {
            for (Port p : ports) {
                ProductType targetProduct = p.getProductType();
                ArrayList<ProductType> requiredTypes = Simulation.getInstance()
                        .getCurrentScenario().getPrimaryProduct(targetProduct);
                for (ProductType required : requiredTypes) {
                    Product product = new Product(required);
                    demand.putIfAbsent(product, MED_DEMAND_LEVEL);
                }
            }
        }

        if (validCities != null) {
            for(City city : validCities) {
                ProductType targetProduct = city.getConsumedProductType();
                Product product = new Product(targetProduct);
                demand.putIfAbsent(product, MED_DEMAND_LEVEL);
            }
            demand.putIfAbsent(MAIL_PRODUCT, MED_DEMAND_LEVEL);
            demand.putIfAbsent(PEOPLE_PRODUCT, MED_DEMAND_LEVEL);
        }

        if (validHouseBlocks != null && !validHouseBlocks.isEmpty()) {
            demand.putIfAbsent(MAIL_PRODUCT, MED_DEMAND_LEVEL);
            demand.putIfAbsent(PEOPLE_PRODUCT, MED_DEMAND_LEVEL);
        }
    }

    /**
     * Updates the demand levels for products based on the annual cargo quantities.
     *
     * <p>For each product in the annual cargoes map:
     * <ul>
     *   <li>If the quantity exceeds MAX_CARGOES_LEVEL and current demand is below MAX_DEMAND_LEVEL,
     *       increment the demand by 1.</li>
     *   <li>If the quantity is below MIN_CARGOES_LEVEL and current demand is above MIN_DEMAND_LEVEL,
     *       decrement the demand by 1.</li>
     * </ul>
     * This method helps dynamically adjust demand according to product flow.
     */
    private void atualizeDemand() {
        for (Product produto : demand.keySet()) {
            int demandaAtual = demand.get(produto);
            Integer quantidade = anualCargoes.get(produto);

            if (quantidade == null) {

                demandaAtual = Math.max(0, demandaAtual - 1);
                demand.put(produto, demandaAtual);
                continue;
            }

            if (quantidade > MAX_CARGOES_LEVEL) {
                demandaAtual = Math.min(MAX_DEMAND_LEVEL, demandaAtual -1);
            } else if (quantidade < MIN_CARGOES_LEVEL) {
                demandaAtual = Math.max(MIN_DEMAND_LEVEL, demandaAtual + 1);
            }

            demand.put(produto, demandaAtual);
        }
    }

    // =========== other =========== //

    /**
     * Sets the acquired buildings for this station.
     *
     * @param acquiredBuildings the buildings acquired
     */
    public void setAcquiredBuildings(Buildings acquiredBuildings) {
        this.acquiredBuildings = acquiredBuildings;
    }

    /**
     * Sets the railway lines associated with this station.
     *
     * @param railwayLines the railway lines
     */
    public void setRailwayLines(RailwayLines railwayLines) {
        this.railwayLines = railwayLines;
    }

    public void addProductsToStorage(Product product, int quantity) {
        if (quantity <= 0) return;
        storedProducts.put(product, getStoredQuantity(product) + quantity);
        addToAnualCargoes(product, quantity);
    }

    public void addToAnualCargoes(Product product, int quantity) {
        if (product == null || quantity <= 0) return;
        anualCargoes.put(product, anualCargoes.getOrDefault(product, 0) + quantity);
    }

    /**
     * Updates the stored products map by subtracting the quantities that have been distributed.
     * Removes the product entry entirely if the resulting quantity is zero or less.
     *
     * @param storedProducts Map of products currently stored with their quantities
     * @param toSubtract     Map of products and quantities to subtract from storage
     */
    private void updateStoredProducts(Map<Product, Integer> storedProducts,
                                      Map<Product, Integer> toSubtract) {
        for (Map.Entry<Product, Integer> subtractEntry : toSubtract.entrySet()) {
            Product product = subtractEntry.getKey();
            int amountToSubtract = subtractEntry.getValue();
            int currentQuantity = storedProducts.get(product);

            int newQuantity = currentQuantity - amountToSubtract;

            if (newQuantity > 0) {
                storedProducts.put(product, newQuantity);
            } else {
                storedProducts.remove(product);
            }
        }
    }

    /*public boolean removeProductsFromStorage(Product product, int quantity) {
        int currentQty = getStoredQuantity(product);
        if (currentQty >= quantity && quantity > 0) {
            storedProducts.put(product, currentQty - quantity);
            return true;
        }
        return false;
    }*/

    /**
     * Returns a formatted list of the available products.
     * Format: [index] - ProductName
     */
    public String listOfAvailableProducts() {
        StringBuilder s = new StringBuilder();

        if (availableProducts == null || availableProducts.isEmpty()) {
            s.append("No products available in the area of influence.\n");
            return s.toString();
        }

        int i = 0;
        for (Product product : availableProducts) {
            int quantity = getAvailableQuantityForProductFromInfluenceArea(product);
            s.append(" [").append(i).append("] - ")
                    .append(product.getProductName())
                    .append(" (Available: ").append(quantity).append(")\n");
            i++;
        }

        return s.toString();
    }

    /**
     * Function verify the existence of industries in the station area and populate the two list received
     * as parameter
     * @param primaryIndustries list to be fulfilled
     * @param transformingIndustries list to be fulfilled
     * @return (true - there are stations) (false - there are not)
     */
    private boolean isThereIndustriesInArea(List<PrimaryIndustry> primaryIndustries, List<TransformingIndustry> transformingIndustries, List<Port> port){
        if (primaryIndustries.isEmpty() && transformingIndustries.isEmpty() && port.isEmpty())
            return false;
        return true;
    }

    // =========== Receive Cargo logic =========== //
    /**
     * Distributes stored products to transforming industries, primary industries, and cities.
     * For each product in storage, adds the product to the appropriate industry or city based on matching product types.
     * Tracks and subtracts the quantities distributed from storage after processing.
     */
    public void addProductsToIndustryStorage() {
        Map<Product, Integer> toSubtract = new HashMap<>();

        for (Map.Entry<Product, Integer> entry : storedProducts.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            addProductToTransforming(toSubtract, entry, product, quantity);
//            addProductToPorts(toSubtract, entry, product, quantity);
            addProductToPrimary(toSubtract, entry, product, quantity);

            for (City city : validCities) {
                if (product.equals(MAIL_PRODUCT)){
                    city.increaseMailQuantity(entry.getValue());
                }else if (product.equals(PEOPLE_PRODUCT)){
                    city.increasePassangersQuantity(entry.getValue());
                }
            }
        }
        updateStoredProducts(storedProducts, toSubtract);
    }

    /**
     * Distributes stored products to Port
     */
//    public void addProductToPorts(Map<Product, Integer> toSubtract, Map.Entry<Product, Integer> entry, Product product, int quantity){
//        if (!ports.get(0).isPrimaryProduct()) {
//            for (Port port : ports) {
//                if (port.getProductType().equals(product.getProductType())) {
//                    ArrayList<Product> productsToAdd = new ArrayList<>();
//                    for (int i = 0; i < quantity; i++) {
//                        productsToAdd.add(product);
//                    }
//                    port.addProductsToBeConsumed(productsToAdd);
//                    toSubtract.put(product, toSubtract.getOrDefault(product, 0) + quantity);
//                }
//            }
//        }
//        else {
//            for (Port port : ports) {
//                if (port.getProductType().equals(product.getProductType())) {
//                    port.increaseProductQuantity(entry.getValue());
//                    toSubtract.put(product, toSubtract.getOrDefault(product, 0) + quantity);
//                }
//            }
//        }
//    }

    /**
     * Distributes stored products to Primary
     */
    public void addProductToPrimary(Map<Product, Integer> toSubtract, Map.Entry<Product, Integer> entry, Product product, int quantity) {
        for (PrimaryIndustry primaryIndustry : primaryIndustries) {
            if (primaryIndustry.getProductType().equals(product.getProductType())) {
                primaryIndustry.increaseProductQuantity(entry.getValue());
                toSubtract.put(product, toSubtract.getOrDefault(product, 0) + quantity);
            }
        }
    }

    /**
     * Distributes stored products to Transforming
     */
    public void addProductToTransforming(Map<Product, Integer> toSubtract, Map.Entry<Product, Integer> entry, Product product, int quantity) {
        for (TransformingIndustry transformingIndustry : transformingIndustries) {
            if (transformingIndustry.getProductType().equals(product.getProductType())) {
                ArrayList<Product> productsToAdd = new ArrayList<>();
                for (int i = 0; i < quantity; i++) {
                    productsToAdd.add(product);
                }
                transformingIndustry.addProductsToBeConsumed(productsToAdd);
                toSubtract.put(product, toSubtract.getOrDefault(product, 0) + quantity);
            }
        }
    }

    public double demandBonus(Product product) {
        double value = 1;
        double bonusTax = 0.2;
        if (demand.get(product) != null) {
            int level = demand.get(product);
            double difference = level -  MED_DEMAND_LEVEL ;
            if (difference != 0){
                return 1 + (bonusTax * difference);
            }
        }

        return value;
    }

    // =========== Distribution logic =========== //

    /**
     * Coordinates the transformation process by distributing produced products to transforming industries and ports.
     * Checks if industries and ports exist in the area before distribution.
     *
     * @return Always returns null (consider revising this return type if necessary)
     */
    public String productsTransformation() {
        if (isThereIndustriesInArea(primaryIndustries, transformingIndustries, ports)) {
            for (Industry industry : validIndustries) {
                int available = industry.getProducedQuantity();
                if (available <= 0)
                    continue ;
                ProductType producedType = industry.getProduct().getProductType();
                distributeProduct(industry, available, producedType);
            }
        }
        if (!ports.isEmpty())
            distributePort();
        return null;
    }

    /**
     * Distributes products between primary industries and ports depending on whether the ports produce primary products.
     * If ports do not produce primary products, primary industries distribute products to ports.
     * Otherwise, ports distribute products to transforming industries.
     *
     */
    private void distributePort() {
        if (!ports.get(0).isPrimaryProduct()) {
            for (Industry industry : validIndustries) {
                int available = industry.getProducedQuantity();
                if (available <= 0)
                    continue ;
                ProductType producedType = industry.getProduct().getProductType();
                distributeProductPort(industry, ports, available, producedType);
            }
        }
    }

    /**
     * Distributes products from a primary industry to a list of transforming industries based on demand.
     * Only distributes products if the transforming industry requires the product type produced.
     * The distribution stops when the available quantity is exhausted.
     *
     * @param industry       The primary industry producing the product
     * @param available             The quantity of product available for distribution
     * @param producedType          The type of product produced by the primary industry
     */
    private void distributeProduct(Industry industry, int available, ProductType producedType) {
        for (TransformingIndustry transformingIndustry : transformingIndustries) {
            ProductType targetProduct = transformingIndustry.getProductType();
            ArrayList<ProductType> requiredTypes = Simulation.getInstance()
                    .getCurrentScenario().getPrimaryProduct(targetProduct);
            if (!requiredTypes.contains(producedType))
                continue;
            int needed = transformingIndustry.quantityNeededOfProduct(producedType);
            if (needed <= 0)
                continue;
            int toTransfer = Math.min(available, needed);
            if (toTransfer > 0) {
                industry.removeFromProduction(toTransfer); // reduzimos a quantidade que iremos passsar para a transformadora
                ArrayList<Product> productsDoBeConsumed = new ArrayList<>();
                for (int i = 0; i < toTransfer; i++) {
                    Product product = new Product(producedType);
                    productsDoBeConsumed.add(product); // array de produtos do mesmo tipo(primária atual)
                }
                transformingIndustry.addProductsToBeConsumed(productsDoBeConsumed);
                available -= toTransfer; // retirar a mesma quantidade reduzida na industria
                if (available <= 0)
                    break; // caso o valor chegue a zero, nao temos mais quantidade, passar para a proxima industria primaria
            }
        }
    }

    /**
     * Distributes products from a primary industry to a list of ports based on demand.
     * Only distributes products if the port requires the product type produced.
     * The distribution stops when the available quantity is exhausted.
     *
     * @param industry The primary industry producing the product
     * @param ports           List of ports requiring input products
     * @param available       The quantity of product available for distribution
     * @param producedType    The type of product produced by the primary industry
     */
    private void distributeProductPort(Industry industry, List<Port> ports, int available, ProductType producedType) {
        for (Port port : ports) {
            ProductType targetProduct = port.getProductType();
            ArrayList<ProductType> requiredTypes = Simulation.getInstance()
                    .getCurrentScenario().getPrimaryProduct(targetProduct);
            if (!requiredTypes.contains(producedType))
                continue;
            int needed = port.quantityNeededOfProduct(producedType);
            if (needed <= 0)
                continue;
            int toTransfer = Math.min(available, needed);
            if (toTransfer > 0) {
                industry.removeFromProduction(toTransfer); // reduzimos a quantidade que iremos passsar para a transformadora
                ArrayList<Product> productsDoBeConsumed = new ArrayList<>();
                for (int i = 0; i < toTransfer; i++) {
                    Product product = new Product(producedType);
                    productsDoBeConsumed.add(product); // array de produtos do mesmo tipo(primária atual)
                }
                port.addProductsToBeConsumed(productsDoBeConsumed);
                available -= toTransfer; // retirar a mesma quantidade reduzida na industria
                if (available <= 0)
                    break; // caso o valor chegue a zero, nao temos mais quantidade, passar para a proxima industria primaria
            }
        }
    }

    /**
     * Refreshes annual cargo values by updating demand and clearing the stored cargo data.
     */
    public void refreshAnualCargoeValues(){
        atualizeDemand();
        anualCargoes.clear();
    }

    // =========== Getters =========== //

    public List<Product> getAvailableProducts() {
        return new ArrayList<>(availableProducts);
    }

    /**
     * Get all the quantity of a given product from a industry or city
     * @param product the wanted product
     * @return (how many product was taken away)
     */
    public int getAvailableQuantityForProductFromInfluenceArea(Product product) {
        int totalQuantity = 0;
        int availableQuantity = storedProducts.getOrDefault(product, 0);

        ProductType type = product.getProductType();
        for (Industry industry : validIndustries) {
            if (industry.getProductType().equals(type)) {
                totalQuantity += industry.getProducedQuantity();
            }
        }
        for (City city : validCities) {
            if (type == ProductType.MAIL) {
                totalQuantity += city.getMailQnt();
            } else if (type == ProductType.PEOPLE) {
                totalQuantity += city.getPassengersQnt();
            }
        }

        if (availableQuantity > 0) {
            totalQuantity += availableQuantity;
            storedProducts.remove(product);
        }
        return totalQuantity;
    }

    /**
     * Retrieves the current stored quantity of a specific product.
     *
     * @param product The product for which the quantity is requested
     * @return The stored quantity of the specified product, or zero if the product is not stored
     */
    public int getStoredQuantity(Product product) {
        return storedProducts.getOrDefault(product, 0);
    }

    /**
     * Gets the station on the other end of the given railway line.
     *
     * @param rail the railway line
     * @return the opposite station on this railway line relative to this station
     */
    public StationType getNextStation(RailwayLine rail){
        if (rail.getStation1().equals(this)) {
            return rail.getStation2();
        } else if (rail.getStation2().equals(this)) {
            return rail.getStation1();
        } else {
            throw new IllegalArgumentException("This station is not part of the given railway line.");
        }
    }

    /**
     * Returns the location of this station.
     *
     * @return the location
     */
    public Location getLocation(){ return this.location; }

    /**
     * Returns the name of this station.
     *
     * @return the station's name
     */
    public String getName() { return name; }

    /**
     * Returns the railway lines associated with this station.
     *
     * @return the railway lines
     */
    public RailwayLines getRailwayLines() { return railwayLines; }

    /**
     * Returns the count of railway lines connected to this station.
     *
     * @return number of railway lines
     */
    public int getManyRailwayLines() { return railwayLines.getManyRailwayLines(); }

    public Set<Object> getObjectsInArea() {
        return inArea;
    }

    /**
     * Constructs and returns a detailed information summary about this object.
     *
     * <p>The summary includes:
     * <ul>
     *   <li>The name of the entity.</li>
     *   <li>The position obtained from the location object.</li>
     *   <li>Information about buildings associated with the entity.</li>
     *   <li>Details about the area of influence.</li>
     *   <li>Information about objects present in the area.</li>
     *   <li>Current supply details.</li>
     *   <li>Current demand details.</li>
     * </ul>
     *
     * @return a multi-line string summarizing the key attributes and state.
     */
    public String getInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Name: ").append(name).append("\n");
        info.append("Position: ").append(location.getPosition()).append("\n");

        info.append(getBuildingsInfo());
        info.append(getInfluenceAreaInfo());
        info.append(getObjectsInAreaInfo());
        info.append(getSupplyInfo());
        info.append(getDemandInfo());
        return info.toString();
    }

    /**
     * Returns a formatted list of the buildings acquired by the station.
     * A newline is inserted after every 7 buildings to enhance readability.
     *
     * @return a formatted string listing all acquired buildings.
     */
    private String getBuildingsInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Buildings: ");
        int buildingCount = 0;
        int totalBuildings = acquiredBuildings.getAcquiredBuildings().size();
        for (int i = 0; i < totalBuildings; i++) {
            info.append(acquiredBuildings.getAcquiredBuildings().get(i).getName());
            buildingCount++;
            if (i < totalBuildings - 1) {
                info.append(", ");
            }
            if (buildingCount % 7 == 0 && i < totalBuildings - 1) {
                info.append("\n");
            }
        }
        if (buildingCount % 7 == 0) {
            info.append("\n");
        }

        return info.toString();
    }

    /**
     * Returns the positions that define the station's area of influence.
     * A newline is inserted after every 12 positions to maintain clarity.
     *
     * @return a formatted string representing the positions in the influence area.
     */
    private String getInfluenceAreaInfo() {
        StringBuilder info = new StringBuilder();
        info.append("InfluenceArea: ");

        List<Position> positions = area.getArea();
        int totalPositions = positions.size();

        int positionsPerLine;
        if (totalPositions <= 12) {
            positionsPerLine = 6;
        } else {
            positionsPerLine = 8;
        }

        int count = 0;
        for (int i = 0; i < totalPositions; i++) {
            Position p = positions.get(i);
            if (p != null) {
                info.append(p.toString());

                count++;
                if (i < totalPositions - 1) {
                    info.append(", ");
                }

                if (count % positionsPerLine == 0 && i < totalPositions - 1) {
                    info.append("\n");
                }
            }
        }

        info.append("\n");
        return info.toString();
    }

    /**
     * Returns the positions that define the station's area of influence.
     * A newline is inserted after every 12 positions to maintain clarity.
     *
     * @return a formatted string representing the positions in the influence area.
     */
    private String getObjectsInAreaInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Objects in Area: ");

        int count = 0;
        int total = validCities.size() + validHouseBlocks.size() + validIndustries.size();
        int objectsPerLine = 8;

        for (City city : validCities) {
            info.append(city.getName());
            count++;
            if (count % objectsPerLine == 0 && count < total) {
                info.append("\n");
            } else if (count < total) {
                info.append(", ");
            }
        }

        for (HouseBlock house : validHouseBlocks) {
            info.append(house.toString());
            count++;
            if (count % objectsPerLine == 0 && count < total) {
                info.append("\n");
            } else if (count < total) {
                info.append(", ");
            }
        }

        for (Industry industry : validIndustries) {
            info.append(industry.getType());
            count++;
            if (count % objectsPerLine == 0 && count < total) {
                info.append("\n");
            } else if (count < total) {
                info.append(", ");
            }
        }

        if (count % objectsPerLine != 0) {
            info.append("\n");
        }

        return info.toString();
    }

    /**
     * Returns a string listing the products available in the area of influence.
     */
    private String getSupplyInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Supply: ");

        int count = 0;
        int size = availableProducts.size();

        for (Product product : availableProducts) {
            info.append(product.getProductName());
            count++;
            if (count < size) {
                info.append(", ");
            }
        }

        info.append("\n");
        return info.toString();
    }

    /**
     * Retrieves a string representation of the current demand levels.
     *
     * @return a string listing all products and their current demand levels.
     */
    private String getDemandInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Demand: ");
        if (demand != null &&  !demand.isEmpty()) {
            for (Product o : demand.keySet()) {
                info.append("(").append(o.getProductName()).append(",").append(demand.get(o)).append(")");
            }
        }
        return info.toString();
    }

    /**
     * Returns the buildings acquired by this station.
     *
     * @return acquired buildings
     */
    public Buildings getAcquiredBuildings() {
        return acquiredBuildings;
    }

    /**
     * Returns the influence area of this station.
     *
     * @return the influence area
     */
    public InfluenceArea getArea() { return area; }

    public Map<Product, Integer> getDemand() {
        return demand;
    }

    // =========== Auxiliary functions =========== //

    /**
     * Compares this station with another object for equality. Two stations are considered equal if:
     * - They are of the exact same runtime class (both Station or both Depot)
     * - They have the same name (case-sensitive)
     * - They are at the same location
     * - They have equal influence areas
     * - They have the same railway lines
     * - They have the same acquired buildings
     * - They have the same objects in their influence areas
     *
     * @param obj the object to compare with
     * @return true if the objects are fully equal station instances, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        StationType that = (StationType) obj;

        return Objects.equals(name, that.name) &&
                Objects.equals(location, that.location) &&
                Objects.equals(area, that.area) &&
                Objects.equals(railwayLines, that.railwayLines) &&
                Objects.equals(acquiredBuildings, that.acquiredBuildings) &&
                Objects.equals(inArea, that.inArea) &&
                Objects.equals(availableProducts, that.availableProducts) &&
                Objects.equals(validIndustries, that.validIndustries) &&
                Objects.equals(validHouseBlocks, that.validHouseBlocks)&&
                Objects.equals(validCities, that.validCities);
    }

    /**
     * Returns a hash code consistent with the equals() implementation.
     * @return a hash code value for this station
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, location, area, railwayLines, acquiredBuildings,
                inArea, availableProducts, validIndustries, validHouseBlocks);
    }

    /**
     * Returns a string representation of the station including its name and position.
     *
     * @return the string representation
     */
    @Override
    public String toString(){
        return this.name + " at " + this.location.getPosition().toString();
    }
}