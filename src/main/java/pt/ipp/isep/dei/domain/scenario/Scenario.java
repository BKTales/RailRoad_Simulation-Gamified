    package pt.ipp.isep.dei.domain.scenario;

    import pt.ipp.isep.dei.domain.Budget;
    import pt.ipp.isep.dei.domain.City;
    import pt.ipp.isep.dei.domain.Map;
    import pt.ipp.isep.dei.domain.industries.Industry;
    import pt.ipp.isep.dei.domain.position.HouseBlock;
    import pt.ipp.isep.dei.domain.position.Position;
    import pt.ipp.isep.dei.domain.train.LocomotiveType;
    import pt.ipp.isep.dei.repository.*;
    import pt.ipp.isep.dei.domain.Product;
    import pt.ipp.isep.dei.domain.ProductType;
    import pt.ipp.isep.dei.domain.industries.*;
    import pt.ipp.isep.dei.domain.position.Location;
    import pt.ipp.isep.dei.domain.rails.RailType;
    import pt.ipp.isep.dei.domain.rails.RailwayLine;
    import pt.ipp.isep.dei.domain.rails.RailwayLines;
    import pt.ipp.isep.dei.domain.rails.TrackType;
    import pt.ipp.isep.dei.domain.station.*;
    import pt.ipp.isep.dei.domain.train.LocomotiveModel;
    import pt.ipp.isep.dei.domain.train.Train;


    import java.io.Serializable;
    import java.util.*;

    /**
     * Represents a simulation scenario containing configuration parameters such as
     * map, time and historical restrictions, technology restrictions, port behavior, and budget.
     */
    public class Scenario implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private Map attachedMap;
        private TimeRestrictions timeRestrictions;
        private ProductRestrictions historicalRestrictions;
        private TechnologyRestriction technologyRestriction;
        private PortBehaviour portBehaviour;
        private Budget initialBudget;
        private Budget budget;

        private IndustryRepository industryRepository;
        private TrainRepository trainRepository;
        private StationRepository stationRepository;
        private RailwaylineRepository railwaylineRepository;
        private RouteRepository routeRepository;
        private TripRepository tripRepository;
        private InfluenceAreaRepository influenceAreaRepository;
        private CityRepository cityRepository;


        /**
         * Constructs a new Scenario instance with the specified parameters.
         *
         * @param name                   the scenario's name
         * @param attachedMap            the map associated with the scenario
         * @param timeRestrictions       the time-based restrictions applied to the scenario
         * @param historicalRestrictions the historical restrictions applied to the scenario
         * @param technologyRestriction  the technology-related restrictions in the scenario
         * @param portBehaviour          the import/export behavior of ports in the scenario
         * @param budget                 the budget available for the scenario
         */
        public Scenario(String name, Map attachedMap, TimeRestrictions timeRestrictions,
                        ProductRestrictions historicalRestrictions, TechnologyRestriction technologyRestriction,
                        PortBehaviour portBehaviour, Budget budget) {
            this.name = name;
            this.timeRestrictions = timeRestrictions;
            this.historicalRestrictions = historicalRestrictions;
            this.technologyRestriction = technologyRestriction;
            this.portBehaviour = portBehaviour;
            this.initialBudget = budget;
            this.budget = budget;

            // duplicating the map to do not have problem with two scenario using the same map
            this.attachedMap = new Map(attachedMap.getName(), attachedMap.getLength(),
                    attachedMap.getHeight(), attachedMap.getScale());

            influenceAreaRepository = new InfluenceAreaRepository();
            industryRepository = new IndustryRepository();
            trainRepository = new TrainRepository();
            stationRepository = new StationRepository();
            railwaylineRepository = new RailwaylineRepository();
            routeRepository = new RouteRepository();
            tripRepository = new TripRepository();
            cityRepository = new CityRepository();
        }

        public void startRepos(){
            if (this.influenceAreaRepository == null) this.influenceAreaRepository = new InfluenceAreaRepository();
            if (this.industryRepository == null) this.industryRepository = new IndustryRepository();
            if (this.trainRepository == null) this.trainRepository = new TrainRepository();
            if (this.stationRepository == null) this.stationRepository = new StationRepository();
            if (this.railwaylineRepository == null) this.railwaylineRepository = new RailwaylineRepository();
            if (this.routeRepository == null) this.routeRepository = new RouteRepository();
            if (this.tripRepository == null) this.tripRepository = new TripRepository();
            if (this.cityRepository == null) this.cityRepository = new CityRepository();
        }

        // =============== Create methods =============== //

        /**
         * Creates a new {@link Train} using the given {@link LocomotiveModel} and assigns it a unique ID.
         * <p>
         * The train is created internally with a locomotive based on the provided model.
         * The new train is then added to the {@link TrainRepository}.
         * </p>
         *
         * @param model the {@link LocomotiveModel} used to initialize the train's locomotive
         * @return the newly created {@link Train}
         */
        public Train createTrain(LocomotiveModel model) {
            return new Train(model, trainRepository.getNewID());
        }

        /**
         * Creates and adds a new {@link Industry} to the repository
         *
         * @param productType the product associated with the industry
         * @param location the location where the industry is placed
         * @return the created {@link Industry} instance, or {@code null} if no valid industry was created
         */
        public Industry createIndustry(ProductType productType, Location location) {
            Product product = new Product(productType);
            IndustryType type = industryRepository.getIndustryType(product.getProductType());
            IndustrySector sector = type.getSector();
            Industry i = null;

            if (sector == IndustrySector.PRIMARY)
                i = new PrimaryIndustry(product, type, location, product.getProductType().timeToProduce, 1000);
            else if (sector == IndustrySector.TRANSFORMING)
                i = new TransformingIndustry(product, type, location, product.getProductType().timeToProduce, 1000);

            if (i != null)
                industryRepository.addIndustry(i);
            return i;
        }

        /**
         * Creates and adds a new {@link Port} to the repository
         *
         * @param location the location where the industry is placed
         * @return {@link Port} on success, {@code null} if no Port was created
         */
        public Port createPort(Location location) throws NullPointerException {
            if (portBehaviour == null)
                throw new NullPointerException("Behaviour cannot be null");
            ProductType exP = portBehaviour.getExportedProduct();
            List<ProductType> imP = portBehaviour.getImportedProducts();

            Port p = new Port(new Product(exP), imP, location, exP.timeToProduce, 1000);
            industryRepository.addPort(p);

            return p;
        }

        /**
         * Creates a new station and adds it to the repository.
         *
         * @param name the name of the station
         * @param cardinalPosition the cardinal direction (used for standard stations)
         * @param location the location of the station
         * @param stationType the type of the station to create ("STATION", "DEPOT", "TERMINAL")
         * @param maxHeight maximum height allowed at the station
         * @param maxLength maximum length allowed at the station
         * @return the created {@link StationType}
         */
        public StationType createStation(String name, String stationType, Location location, String cardinalPosition, int maxHeight, int maxLength) {
            StationType station;

            if (stationType.equals("STATION")) {
                station = new Station(name, cardinalPosition, location, maxHeight, maxLength);
            } else if (stationType.equals("DEPOT")) {
                station = new Depot(name, location, maxHeight, maxLength);
            } else {
                station = new Terminal(name, location, maxHeight, maxLength);
            }
            setObjetcsInArea(station);
            stationRepository.addStation(station);
            return station;
        }

        /**
         * Creates a new {@link RailwayLine} between two stations with the given track and line types,
         * adds it to the repository, and associates it with both stations.
         *
         * @param trackType TrackType
         * @param lineType RailType
         * @param station1 the first station connected by the railway line
         * @param station2 the second station connected by the railway line
         * @param distance the distance between the two stations
         * @return the newly created {@code RailwayLine}
         */
        public RailwayLine createRailwayLine(StationType station1, StationType station2, String trackType, String lineType, Double distance) {
            TrackType trackType1 = null;
            for (TrackType t : TrackType.values()) {
                if (!trackType.isEmpty() && t.name().equals(trackType)) {
                    trackType1 = t;
                }
            }
            RailType lineType1 = null;
            for (RailType r : RailType.values()) {
                if (!lineType.isEmpty() && r.name().equals(lineType)) {
                    lineType1 = r;
                }
            }
            RailwayLine rl = new RailwayLine(station1, station2, lineType1, trackType1, distance);
            station1.addRailwayLines(rl);
            station2.addRailwayLines(rl);
            railwaylineRepository.addRailwayLine(rl);
            return rl;
        }

        /**
         * Upgrades a given station to a new type (Station or Terminal), transferring all relevant data.
         *
         * <p>This method creates a new station object based on the provided upgrade type, preserving
         * the original station's name, location, railway lines, and acquired buildings. The original
         * station is removed from the repository after the new one is created and added.</p>
         *
         * <p>Possible upgrade transitions:</p>
         * <ul>
         *     <li>{@code Depot → Station}</li>
         *     <li>{@code Depot → Terminal}</li>
         *     <li>{@code Station → Terminal}</li>
         * </ul>
         *
         * @param station the station to upgrade
         * @param maxHeight maximum height of the map
         * @param maxLength maximum length of the map
         * @param upgrade the upgrade type as a string
         * @param cardinalPosition the cardinal direction of the new station (used for Station upgrades)
         * @return the new {@link StationType} after upgrade
         */
        public void passDataFromStation(StationType station, int maxHeight, int maxLength, String upgrade, String cardinalPosition) {
            Location location = station.getLocation();
            String name = station.getName();
            RailwayLines railwayLines = station.getRailwayLines();
            Buildings acquiredBuildings = station.getAcquiredBuildings();
            Upgrade upgrade1 = null;
            for (Upgrade u : Upgrade.values()) {
                if (!upgrade.isEmpty() && u.name().equals(upgrade)) {
                    upgrade1 = u;
                }
            }
            if (station instanceof Depot) {
                if (upgrade1 == Upgrade.DEPOTTOSTATION) {
                    Station stationNew = new Station(name.replace("depot","station"), cardinalPosition, location, maxHeight, maxLength);
                    stationNew.setRailwayLines(railwayLines);
                    stationNew.setAcquiredBuildings(acquiredBuildings);
                    stationRepository.deleteStation(station);
                    stationRepository.getStations().add(stationNew);
                    setObjetcsInArea(stationNew);
                } else if (upgrade1 == Upgrade.DEPOTTOTERMINAL) {
                    Terminal terminalNew = new Terminal(name.replace("depot","terminal"), location, maxHeight, maxLength);
                    terminalNew.setRailwayLines(railwayLines);
                    terminalNew.setAcquiredBuildings(acquiredBuildings);
                    stationRepository.deleteStation(station);
                    stationRepository.getStations().add(terminalNew);
                    setObjetcsInArea(terminalNew);
                }
            }else {
                Terminal terminalNew = new Terminal(name.replace("station","terminal"), location, maxHeight, maxLength);
                terminalNew.setRailwayLines(railwayLines);
                terminalNew.setAcquiredBuildings(acquiredBuildings);
                stationRepository.deleteStation(station);
                stationRepository.getStations().add(terminalNew);
                setObjetcsInArea(terminalNew);
            }

        }

        /**
         * Associates objects located within the influence area of the given station.
         *
         * This method creates mappings between positions inside the station's influence area
         * and the corresponding objects found at those positions. It considers cities and industries
         * located within the area and collects them into a map with their positions as keys.
         *
         * The resulting map is then passed to the repository for further use.
         *
         * @param station the {@link StationType} whose influence area is being processed
         */
        public void setObjetcsInArea(StationType station) {


            Set<Object> closeObjects = new HashSet<>();

            ArrayList<Position> areaPositions = station.getArea().getArea();

            for (City city : cityRepository.getCities()) {
                if (areaPositions.contains(city.getPosition())) {
                    closeObjects.add(city);
                }
                for (HouseBlock houseBlock : city.getHouseBlocks()) {
                    if (areaPositions.contains(houseBlock.getPosition())) {
                        closeObjects.add(houseBlock);
                    }
                }
            }

            for (Industry industry : industryRepository.getIndustries()) {
                if (areaPositions.contains(industry.getLocation().getPosition())) {
                    closeObjects.add(industry);
                }
            }

            for (StationType stationType : stationRepository.getStations()) {
                if (areaPositions.contains(stationType.getLocation().getPosition())) {
                    closeObjects.add(stationType);
                }
            }

            // Supondo que você tenha um método para setar uma Collection<Object>
            station.setObjectsInArea(closeObjects);
        }

        //  =============== Logic methods =============== //

        /**
         * Converts a {@link LocomotiveType} to a corresponding {@link TechnologyType}.
         *
         * @param type the type of locomotive
         * @return the corresponding technology type
         * @throws IllegalArgumentException if the type is unknown
         */
        private TechnologyType convertToTechnologyType(LocomotiveType type) {
            switch (type) {
                case DIESEL_LOCOMOTIVE:
                    return TechnologyType.DIESEL;
                case ELECTRICITY_LOCOMOTIVE:
                    return TechnologyType.ELECTRIC;
                case STEAM_LOCOMOTIVE:
                    return TechnologyType.STEAM;
                default:
                    throw new IllegalArgumentException("Type of locomotive unknown: " + type);
            }
        }

        /**
         * Purchases a locomotive by deducting its cost and creating a train.
         * <p>
         * The created train is returned, but not persisted — that responsibility lies outside this class.
         * </p>
         *
         * @param model the locomotive model to be purchased
         * @return the created Train instance
         * @throws IllegalArgumentException if budget is insufficient
         */
        public Train purchaseLocomotive(LocomotiveModel model) {
            if (budget.hasEnoughFunds(model.getAcquisitionPrice())){
                budget.purchase(model.getAcquisitionPrice());
                return createTrain(model);
            }

            return null;
        }

        /**
         * Finds and returns the name of the city nearest to the given coordinates.
         *
         * @param x the x-coordinate to compare
         * @param y the y-coordinate to compare
         * @return the name of the nearest city or null if no cities exist or exact match is found
         */
        public String nearestCityNames(int x, int y) {
            Position p = new Position(x, y);
            return shortestDistance(p);
        }

        /**
         * Computes the city or cities with the shortest distance to the given position.
         *
         * @param p the position to calculate distances from
         * @return the name of one of the closest cities, or null if no cities or exact position match
         */
        private String shortestDistance(Position p) {
            ArrayList<String> closestCitiesNames = new ArrayList<>();
            double minDistance = Double.MAX_VALUE;

            if (!cityRepository.getCities().isEmpty()) {
                for (City city : cityRepository.getCities()) {
                    double dis = Math.abs(city.getPosition().getX() - p.getX())
                            + Math.abs(city.getPosition().getY() - p.getY());
                    if (dis < minDistance) {
                        closestCitiesNames.clear();
                        minDistance = dis;
                        closestCitiesNames.add(city.getName());
                    } else if (dis == minDistance) {
                        closestCitiesNames.add(city.getName());
                    }
                }
            } else {
                return null;
            }
            if (minDistance == 0) {
                return null;
            }
            return closestCitiesNames.get(0);
        }

        //  =============== Getters =============== //


        /**
         * Returns the scenario's name.
         *
         * @return the scenario name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the time restrictions applied to this scenario.
         *
         * @return time restrictions
         */
        public TimeRestrictions getTimeRestrictions() {
            return timeRestrictions;
        }

        /**
         * Returns the port behavior configuration.
         *
         * @return port behavior
         */
        public PortBehaviour getPortBehaviour() {
            return portBehaviour;
        }

        /**
         * Returns the technology restrictions applied to this scenario.
         *
         * @return technology restriction
         */
        public TechnologyRestriction getTechnologyRestriction() {
            return technologyRestriction;
        }

        /**
         * Returns the historical restrictions applied to this scenario.
         *
         * @return historical restrictions
         */
        public ProductRestrictions getHistoricalRestrictions() {
            return historicalRestrictions;
        }

        /**
         * Returns the budget available for this scenario.
         *
         * @return budget
         */
        public Budget getBudget() {
            return budget;
        }

        public Budget getInitialBudget() {
            return initialBudget;
        }

        /**
         * Returns the map attached to this scenario.
         *
         * @return attached map
         */
        public Map getAttachedMap() {
            return attachedMap;
        }

        public InfluenceAreaRepository getInfluenceAreaRepository() {
            return influenceAreaRepository;
        }

        public IndustryRepository getIndustryRepository() {
            return industryRepository;
        }

        public TrainRepository getTrainRepository() {
            return trainRepository;
        }

        public StationRepository getStationRepository() {
            return stationRepository;
        }

        public RailwaylineRepository getRailwaylineRepository() {
            return railwaylineRepository;
        }

        public RouteRepository getRouteRepository() {
            return routeRepository;
        }

        public TripRepository getTripRepository() {
            return tripRepository;
        }

        public CityRepository getCityRepository() {
            return cityRepository;
        }

        /**
         * Returns a formatted string listing all available products,
         * excluding the last two (assumed to be city-generated).
         *
         * @return a string of product options with indices
         */
        public String getAvailableProducts() {
            StringBuilder s = new StringBuilder();

            for (int i = 0; i < ProductType.values().length - 2; i++) {
                ProductType product = ProductType.values()[i];
                if (technologyRestriction == null || technologyRestriction.isProductAvailable(product))
                    s.append(" [").append(i).append("] - ").append(product).append("\n");
            }
            return s.toString();
        }

        /**
         * Get the primary products needed to transforme into the given product
         * @param p product To get the primaries of
         * @return the list with the products needed
         */
        public ArrayList<ProductType> getPrimaryProduct(ProductType p) {
            ArrayList<ProductType> primaryProduct = new ArrayList<>();
            if (p == ProductType.BREAD)
                primaryProduct.add(ProductType.GRAINS);
            else if (p == ProductType.CLOTHING)
                primaryProduct.add(ProductType.WOOL);
            else if (p == ProductType.CAR){
                primaryProduct.add(ProductType.STEEL);
                primaryProduct.add(ProductType.STEEL);
                primaryProduct.add(ProductType.RUBBER);
            }
            else if (p == ProductType.STEEL) {
                primaryProduct.add(ProductType.IRON);
                primaryProduct.add(ProductType.COAL);
            }
            return (primaryProduct);
        }

        /**
         * Retrieves the list of available locomotive models for purchase,
         * based on the current simulation year, the scenario's time restrictions,
         * and the allowed technology types.
         *
         * @param currentYear the current simulation year
         * @return a list of {@link LocomotiveModel} instances that are available for purchase
         */
        public List<LocomotiveModel> getAvailableLocomotives(int currentYear) {
            int scenarioStartYear = timeRestrictions.getStartYear();
            int scenarioEndYear = timeRestrictions.getEndYear();
            List<TechnologyType> allowedTechs = getAllowedTechs();

            List<LocomotiveModel> availableModels = new ArrayList<>();

            //System.out.println("Current Year: " + currentYear);
            //System.out.println("Scenario Start Year: " + scenarioStartYear);
            //System.out.println("Scenario End Year: " + scenarioEndYear);
            //System.out.println("Allowed Technologies: " + allowedTechs);
            //System.out.println();

            for (LocomotiveModel model : LocomotiveModel.values()) {
                int modelStartYear = model.getStartYear();
                TechnologyType techType = convertToTechnologyType(model.getType());

                if (modelStartYear <= currentYear &&
                        modelStartYear <= scenarioEndYear &&
                        allowedTechs.contains(techType)) {
                    availableModels.add(model);
                }
            }
            return availableModels;
        }

        /**
         * Retrieves the list of allowed technology types for the current scenario.
         * <p>
         * This method first fetches the list of restricted technologies defined in the scenario's
         * {@code TechnologyRestriction}. It then computes the allowed technologies by removing
         * the restricted types from the full set of possible technologies
         * ({@code DIESEL}, {@code ELECTRIC}, {@code STEAM}).
         * <p>
         * If no restriction is defined (e.g., due to a {@code NullPointerException}),
         * it assumes that no technology is allowed and returns an empty list.
         *
         * @return a list of {@link TechnologyType} values representing the allowed technologies
         *         in the current scenario.
         */
        private List<TechnologyType> getAllowedTechs() {
            if (technologyRestriction == null || technologyRestriction.getTechList() == null) {
                // Nenhuma restrição = todas as tecnologias permitidas
                return List.of(TechnologyType.DIESEL, TechnologyType.ELECTRIC, TechnologyType.STEAM);
            }

            List<TechnologyType> restricted = technologyRestriction.getTechList();
            System.out.println("Restricted Technologies: " + restricted);

            List<TechnologyType> allTechs = List.of(TechnologyType.DIESEL, TechnologyType.ELECTRIC, TechnologyType.STEAM);
            List<TechnologyType> allowed = new ArrayList<>(allTechs);
            allowed.removeAll(restricted);

            return allowed;
        }

        //  =============== Setters =============== //

        public void setInfluenceAreaRepository(InfluenceAreaRepository influenceAreaRepository) {
            this.influenceAreaRepository = influenceAreaRepository;
        }

        public void setIndustryRepository(IndustryRepository industryRepository) {
            this.industryRepository = industryRepository;
        }

        public void setTrainRepository(TrainRepository trainRepository) {
            this.trainRepository = trainRepository;
        }

        public void setStationRepository(StationRepository stationRepository) {
            this.stationRepository = stationRepository;
        }

        public void setRailwaylineRepository(RailwaylineRepository railwaylineRepository) {
            this.railwaylineRepository = railwaylineRepository;
        }

        public void setRouteRepository(RouteRepository routeRepository) {
            this.routeRepository = routeRepository;
        }

        public void setTripRepository(TripRepository tripRepository) {
            this.tripRepository = tripRepository;
        }
    }
