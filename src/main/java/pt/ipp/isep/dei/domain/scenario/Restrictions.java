package pt.ipp.isep.dei.domain.scenario;

import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Restrictions {


    String name;
    TimeRestrictions timeRestrictions;
    TechnologyRestriction technologyRestriction;
    ProductRestrictions productRestrictions;
    PortBehaviour portBehaviour;

    public Restrictions(String name, TimeRestrictions timeRestrictions, TechnologyRestriction technologyRestriction,
                        ProductRestrictions historicalRestrictions, PortBehaviour portBehaviour) {
        this.name = name;
        this.timeRestrictions = timeRestrictions;
        this.technologyRestriction = technologyRestriction;
        this.productRestrictions = historicalRestrictions;
        this.portBehaviour = portBehaviour;

    }

    /**
     * Creates a list of preset restrictions for various historical scenarios.
     * These restrictions can be used to simulate the impact of historical events on the game.
     *
     * @return a list of Restrictions objects representing different historical scenarios
     */
    public Restrictions presetRestrictions(String choice){

        List<Restrictions> presetRestrictions = new ArrayList<>();

        Restrictions restrictions = null;

        if(choice.equals("COVID-19")) {
            String covidName = "COVID-19";
            TimeRestrictions covidTimeRestrictions = new TimeRestrictions(2018, 2030);
            TechnologyRestriction covidTechnologyRestriction = new TechnologyRestriction();
            ProductRestrictions covidHistoricalRestrictions = new ProductRestrictions();
            covidHistoricalRestrictions.addProductRestriction("GRAINS", 5, 1.5);
            covidHistoricalRestrictions.addProductRestriction("VEGETABLES", 3, 1.2);
            covidHistoricalRestrictions.addProductRestriction("STEEL", 10, 2.0);
            PortBehaviour covidPortBehaviour = new PortBehaviour(Arrays.asList(ProductType.CLOTHING, ProductType.BREAD), ProductType.GRAINS);
            restrictions = new Restrictions(covidName, covidTimeRestrictions, covidTechnologyRestriction,
                    covidHistoricalRestrictions, covidPortBehaviour);
        }else if(choice.equals("Global Financial Crisis")){
            // Global Financial Crisis restrictions
            String econCrisisName = "Global Financial Crisis";
            TimeRestrictions econTimeRestrictions = new TimeRestrictions(2007, 2012);
            TechnologyRestriction econTechnologyRestriction = new TechnologyRestriction();
            ProductRestrictions econHistoricalRestrictions = new ProductRestrictions();
            econHistoricalRestrictions.addProductRestriction("CAR", 8, 0.8); // Car sales down
            econHistoricalRestrictions.addProductRestriction("STEEL", 6, 0.9);
            econHistoricalRestrictions.addProductRestriction("CLOTHING", 4, 0.85);
            PortBehaviour econPortBehaviour = new PortBehaviour(Arrays.asList(ProductType.MAIL, ProductType.PEOPLE), ProductType.CAR);
            restrictions = new Restrictions(econCrisisName, econTimeRestrictions, econTechnologyRestriction, econHistoricalRestrictions, econPortBehaviour);
        }else if(choice.equals("World War II")){
            // World War II restrictions

            String ww2Name = "World War II";
            TimeRestrictions ww2TimeRestrictions = new TimeRestrictions(1939, 1945);
            TechnologyRestriction ww2TechnologyRestriction = new TechnologyRestriction();
            ProductRestrictions ww2HistoricalRestrictions = new ProductRestrictions();
            ww2HistoricalRestrictions.addProductRestriction("STEEL", 20, 2.5);  // High demand for war effort
            ww2HistoricalRestrictions.addProductRestriction("CLOTHING", 10, 1.5); // Military uniforms
            ww2HistoricalRestrictions.addProductRestriction("RUBBER", 8, 2.0);   // Tire shortage
            ww2HistoricalRestrictions.addProductRestriction("COFFEE", 5, 0.5);   // Scarce luxury good
            PortBehaviour ww2PortBehaviour = new PortBehaviour(Arrays.asList(ProductType.PEOPLE, ProductType.MAIL), ProductType.STEEL);
            restrictions = new Restrictions(ww2Name, ww2TimeRestrictions, ww2TechnologyRestriction,
                    ww2HistoricalRestrictions, ww2PortBehaviour);
        }else if(choice.equals("Arab Oil Embargo")){
            // Arab oil embargo restrictions

            String oilEmbargoName = "Arab Oil Embargo";
            TimeRestrictions oilTimeRestrictions = new TimeRestrictions(1973, 1974);
            TechnologyRestriction oilTechnologyRestriction = new TechnologyRestriction();
            ProductRestrictions oilHistoricalRestrictions = new ProductRestrictions();
            oilHistoricalRestrictions.addProductRestriction("CAR", 10, 0.7); // Drop in car production due to oil shortage
            oilHistoricalRestrictions.addProductRestriction("STEEL", 7, 0.8); // Indirect impact
            PortBehaviour oilPortBehaviour = new PortBehaviour(Arrays.asList(ProductType.COFFEE, ProductType.MAIL), ProductType.CAR);
            restrictions = new Restrictions(oilEmbargoName, oilTimeRestrictions, oilTechnologyRestriction,
                    oilHistoricalRestrictions, oilPortBehaviour);

        }else if(choice.equals("Post-War Reconstruction")){
            // Post-War

            String postWarName = "Post-War Reconstruction";
            TimeRestrictions postWarTimeRestrictions = new TimeRestrictions(1946, 1950);
            TechnologyRestriction postWarTechRestriction = new TechnologyRestriction();
            ProductRestrictions postWarHistoricalRestrictions = new ProductRestrictions();
            postWarHistoricalRestrictions.addProductRestriction("STEEL", 12, 1.8); // Rebuilding infrastructure
            postWarHistoricalRestrictions.addProductRestriction("VEGETABLES", 5, 1.3); // Food programs
            postWarHistoricalRestrictions.addProductRestriction("CLOTHING", 4, 1.4);
            PortBehaviour postWarPortBehaviour = new PortBehaviour(Arrays.asList(ProductType.MAIL, ProductType.WOOL), ProductType.CLOTHING);
            restrictions = new Restrictions(postWarName, postWarTimeRestrictions, postWarTechRestriction,
                    postWarHistoricalRestrictions, postWarPortBehaviour);

        }else if(choice.equals("Great Depression")){
            // Great Depression

            String depressionName = "Great Depression";
            TimeRestrictions depressionTimeRestrictions = new TimeRestrictions(1929, 1939);
            TechnologyRestriction depressionTechnologyRestriction = new TechnologyRestriction();
            ProductRestrictions depressionHistoricalRestrictions = new ProductRestrictions();
            depressionHistoricalRestrictions.addProductRestriction("CAR", 7, 0.6);
            depressionHistoricalRestrictions.addProductRestriction("CLOTHING", 5, 0.7);
            depressionHistoricalRestrictions.addProductRestriction("GRAINS", 6, 0.8); // Dust Bowl impact
            PortBehaviour depressionPortBehaviour = new PortBehaviour(Arrays.asList(ProductType.MAIL), ProductType.GRAINS);
            restrictions = new Restrictions(depressionName, depressionTimeRestrictions, depressionTechnologyRestriction,
                    depressionHistoricalRestrictions, depressionPortBehaviour);
        }else if(choice.equals("Spanish Flu")){
            // Spanish flu

            String fluName = "Spanish Flu";
            TimeRestrictions fluTimeRestrictions = new TimeRestrictions(1918, 1920);
            TechnologyRestriction fluTechnologyRestriction = new TechnologyRestriction();
            ProductRestrictions fluHistoricalRestrictions = new ProductRestrictions();
            fluHistoricalRestrictions.addProductRestriction("PEOPLE", 10, 0.4); // Workforce shortage
            fluHistoricalRestrictions.addProductRestriction("MAIL", 5, 0.7);    // Communication disruption
            fluHistoricalRestrictions.addProductRestriction("CLOTHING", 3, 0.8); // Slowed production
            PortBehaviour fluPortBehaviour = new PortBehaviour(Arrays.asList(ProductType.PEOPLE), ProductType.MAIL);
            restrictions = new Restrictions(fluName, fluTimeRestrictions, fluTechnologyRestriction,
                    fluHistoricalRestrictions, fluPortBehaviour);

        }else if(choice.equals("Industrial Revolution")){
            // Industrial Revolution

            String industrialName = "Industrial Revolution";
            TimeRestrictions industrialTimeRestrictions = new TimeRestrictions(1760, 1840);
            TechnologyRestriction industrialTechnologyRestriction = new TechnologyRestriction(); // Could simulate new production tech
            ProductRestrictions industrialRestrictions = new ProductRestrictions();
            industrialRestrictions.addProductRestriction("STEEL", 15, 3.0);
            industrialRestrictions.addProductRestriction("CLOTHING", 10, 2.5);
            industrialRestrictions.addProductRestriction("COAL", 8, 3.5);  // Heavy coal use
            PortBehaviour industrialPortBehaviour = new PortBehaviour(Arrays.asList(ProductType.WOOL), ProductType.CLOTHING);
            restrictions = new Restrictions(industrialName, industrialTimeRestrictions, industrialTechnologyRestriction,
                    industrialRestrictions, industrialPortBehaviour);
        }

        return restrictions;
    }

    /**
     * Default constructor for Restrictions.
     * Initializes all restrictions to null.
     */

    public Restrictions(){}


    /**
     * Creates a time restriction for the scenario based on start and end years.
     *
     * @param startYear the start year of the time restriction
     * @param endYear the end year of the time restriction
     */
    public TimeRestrictions createTimeRestriction(int startYear, int endYear){
        return timeRestrictions = new TimeRestrictions(startYear, endYear);
    }

    /**
     * Creates and stores a new TechnologyRestriction based on the given list of
     * restricted technology types.
     *
     * @param restricted the list of TechnologyType enums that should be made unavailable
     */
    public TechnologyRestriction createTechnologicalRestriction(List<TechnologyType> restricted) {
        return technologyRestriction = new TechnologyRestriction(restricted);
    }


    public Product getProductByName(String productName) {
        return Arrays.stream(ProductType.values())
                .filter(pt -> pt.productName.equals(productName))
                .findFirst()
                .map(Product::new)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productName));
    }

    /**
     * Creates a historical restriction related to a product’s production time and multiplier.
     *
     * @param productName the product name the restriction applies to
     * @param timeToProduce the time needed to produce the product
     * @param multiplier the multiplier affecting production
     */
    public void addHistoricalProductRestriction(String productName, int timeToProduce, double multiplier) {
        if (productRestrictions == null) {
            productRestrictions = new ProductRestrictions();
        }
        productRestrictions.addProductRestriction(productName, timeToProduce, multiplier);
    }

    public void markProductAsUnavailable(String productName) {
        if (technologyRestriction == null) {
            technologyRestriction = new TechnologyRestriction();
        }
        technologyRestriction.markAsUnavailable(productName);
    }



    /**
     * Sets up the port behaviour specifying imported products and exported product.
     *
     * @param importedProductsIndex list of indexes representing imported products
     * @param exportedValueIndex the index of the exported product
     */
    public PortBehaviour createPortBehaviour(List<Integer> importedProductsIndex, int exportedValueIndex){
        List<ProductType> importedProducts = new ArrayList<>();
        ProductType exportedProduct;

        for (int i = 0; i < importedProductsIndex.size(); i++) {
            importedProducts.add(ProductType.valueOf(Product.getAllProducts().get(importedProductsIndex.get(i))));
        }
        exportedProduct = ProductType.valueOf(Product.getAllProducts().get(exportedValueIndex));

        portBehaviour = new PortBehaviour(importedProducts, exportedProduct);

        return portBehaviour;
    }

    public TimeRestrictions getTimeRestrictions() {
        return timeRestrictions;
    }

    public TechnologyRestriction getTechnologyRestriction() {
        return technologyRestriction;
    }

    public ProductRestrictions getProductRestrictions() {
        return productRestrictions;
    }

    public PortBehaviour getPortBehaviour() {
        return portBehaviour;
    }

    public String getName() {
        return name;
    }

    public String getPresetRestrictionsName() {
        return "COVID-19\nGlobal Financial Crisis\nWorld War II\nArab Oil Embargo\n" +
                "Post-War Reconstruction\nGreat Depression\nSpanish Flu\nIndustrial Revolution\n";
    }
}
