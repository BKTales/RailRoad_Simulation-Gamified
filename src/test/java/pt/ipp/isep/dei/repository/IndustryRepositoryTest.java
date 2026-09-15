package pt.ipp.isep.dei.repository;

class IndustryRepositoryTest {

//    @Test
//    void createIndustry() {
//        // Arrange
//        Simulator s = Simulator.getInstance();
//        Map m = s.createMap("map", 100, 100);
//        s.createScenario("scenario", m, new TimeRestrictions(1910, 1980), null, null, null, new Budget(100000));
//        s.setCurrentMap("map");
//        s.setCurrentScenario("scenario");
//        IndustryRepository indRepo = s.getI();
//
//        // Act
//        Industry i = indRepo.createIndustry(new Product(ProductType.GRAINS), r.getLocationRepository().createLocation(new Position(13, 14)));
//
//        // Assert
//        assertNotNull(i);
//    }
//
//    @Test
//    void createPort() {
//        // Arrange
//        Repository r = new Repository();
//        Map m = r.createMap("map", 100, 100);
//        List<ProductType> list = new ArrayList<>();
//        list.add(ProductType.GRAINS);
//        list.add(ProductType.PEOPLE);
//        r.getScenarioRepository().createScenario("scenario", m, new TimeRestrictions(1910, 1980), null, null, new PortBehaviour(list, ProductType.CAR), new Budget(100000));
//        r.setCurrentMap("map");
//        r.setCurrentScenario("scenario");
//        IndustryRepository indRepo = r.getIndustryRepository();
//
//        // Act
//        Industry i = indRepo.createPort(r.getCurrentScenario().getPortBehaviour(), r.getLocationRepository().createLocation(new Position(13, 14)));
//
//        // Assert
//        assertNotNull(i);
//    }
//
//    @Test
//    void getIndustryPosition() {
//        // Arrange
//        Repository r = new Repository();
//        Map m = r.createMap("map", 100, 100);
//        r.getScenarioRepository().createScenario("scenario", m, new TimeRestrictions(1910, 1980), null, null, null, new Budget(100000));
//        r.setCurrentMap("map");
//        r.setCurrentScenario("scenario");
//        IndustryRepository indRepo = r.getIndustryRepository();
//        indRepo.createIndustry(new Product(ProductType.RUBBER), r.getLocationRepository().createLocation(new Position(13, 15)));
//        indRepo.createIndustry(new Product(ProductType.COFFEE), r.getLocationRepository().createLocation(new Position(13, 16)));
//        Industry returned = indRepo.createIndustry(new Product(ProductType.GRAINS), r.getLocationRepository().createLocation(new Position(13, 14)));
//
//        // Act
//        Industry getFromExec = indRepo.getIndustry(new Position(13, 14));
//
//        // Assert
//        assertEquals(getFromExec, returned);
//    }
//
//    @Test
//    void getIndustryIndex() {
//        // Arrange
//        Repository r = new Repository();
//        Map m = r.createMap("map", 100, 100);
//        r.getScenarioRepository().createScenario("scenario", m, new TimeRestrictions(1910, 1980), null, null, null, new Budget(100000));
//        r.setCurrentMap("map");
//        r.setCurrentScenario("scenario");
//        IndustryRepository indRepo = r.getIndustryRepository();
//        indRepo.createIndustry(new Product(ProductType.RUBBER), r.getLocationRepository().createLocation(new Position(13, 15)));
//        indRepo.createIndustry(new Product(ProductType.COFFEE), r.getLocationRepository().createLocation(new Position(13, 16)));
//        Industry returned = indRepo.createIndustry(new Product(ProductType.GRAINS), r.getLocationRepository().createLocation(new Position(13, 14)));
//
//        // Act
//        Industry getFromExec = indRepo.getIndustry(2);
//
//        // Assert
//        assertEquals(getFromExec, returned);
//    }
//
//    @Test
//    void deleteIndustry() {
//        // Arrange
//        Repository r = new Repository();
//        Map m = r.createMap("map", 100, 100);
//        r.getScenarioRepository().createScenario("scenario", m, new TimeRestrictions(1910, 1980), null, null, null, new Budget(100000));
//        r.setCurrentMap("map");
//        r.setCurrentScenario("scenario");
//        IndustryRepository indRepo = r.getIndustryRepository();
//        indRepo.createIndustry(new Product(ProductType.GRAINS), r.getLocationRepository().createLocation(new Position(13, 14)));
//        indRepo.createIndustry(new Product(ProductType.RUBBER), r.getLocationRepository().createLocation(new Position(13, 15)));
//        indRepo.createIndustry(new Product(ProductType.COFFEE), r.getLocationRepository().createLocation(new Position(13, 16)));
//
//        // Act
//        int bf = indRepo.getIndustries().size();
//        indRepo.deleteIndustry(new Position(13, 14));
//        int after = indRepo.getIndustries().size();
//
//        // Assert
//        assertEquals(bf - 1, after);
//    }
//
//    @Test
//    void getIndustries() {
//        // Arrange
//        Repository r = new Repository();
//        Map m = r.createMap("map", 100, 100);
//        r.getScenarioRepository().createScenario("scenario", m, new TimeRestrictions(1910, 1980), null, null, null, new Budget(100000));
//        r.setCurrentMap("map");
//        r.setCurrentScenario("scenario");
//        IndustryRepository indRepo = r.getIndustryRepository();
//        indRepo.createIndustry(new Product(ProductType.GRAINS), r.getLocationRepository().createLocation(new Position(13, 14)));
//        indRepo.createIndustry(new Product(ProductType.GRAINS), r.getLocationRepository().createLocation(new Position(13, 15)));
//        indRepo.createIndustry(new Product(ProductType.GRAINS), r.getLocationRepository().createLocation(new Position(13, 16)));
//
//        // Act
//        List<Industry> getFromExec = indRepo.getIndustries();
//
//        // Assert
//        assertNotNull(getFromExec);
//    }
//
//    @Test
//    void getIndustryTypeFarm() {
//        // Arrange
//        Repository r = new Repository();
//        IndustryRepository indRepo = r.getIndustryRepository();
//
//        // Act
//        IndustryType type = indRepo.getIndustryType(ProductType.GRAINS);
//
//        // Assert
//        assertEquals(IndustryType.FARM, type);
//    }
//
//    @Test
//    void getIndustryTypeBakery() {
//        // Arrange
//        Repository r = new Repository();
//        IndustryRepository indRepo = r.getIndustryRepository();
//
//        // Act
//        IndustryType type = indRepo.getIndustryType(ProductType.BREAD);
//
//        // Assert
//        assertEquals(IndustryType.BAKERY, type);
//    }
//
//    @Test
//    void getIndustryTypeMine() {
//        // Arrange
//        Repository r = new Repository();
//        IndustryRepository indRepo = r.getIndustryRepository();
//
//        // Act
//        IndustryType type = indRepo.getIndustryType(ProductType.IRON);
//
//        // Assert
//        assertEquals(IndustryType.MINE, type);
//    }
//
//    @Test
//    void getIndustryTypeAutomobile() {
//        // Arrange
//        Repository r = new Repository();
//        IndustryRepository indRepo = r.getIndustryRepository();
//
//        // Act
//        IndustryType type = indRepo.getIndustryType(ProductType.CAR);
//
//        // Assert
//        assertEquals(IndustryType.AUTOMOBILE, type);
//   }
}