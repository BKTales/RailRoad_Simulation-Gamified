package pt.ipp.isep.dei;

import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.Product;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.route.CargoMode;
import pt.ipp.isep.dei.domain.scenario.PortBehaviour;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.scenario.TimeRestrictions;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.domain.station.StationType;
import pt.ipp.isep.dei.domain.train.Cargo;
import pt.ipp.isep.dei.domain.train.LocomotiveModel;
import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.us010.PointOfRoute;

import java.util.ArrayList;
import java.util.List;

public class Bootstrap {
    private Simulator simulator;

    public Bootstrap() {
        simulator = Simulator.getInstance();
    }

    public void run() {
        Map map1 = simulator.createMap("Map1", 100, 100, 5);


        List<ProductType> productImport = new ArrayList<>();
        productImport.add(ProductType.CLOTHING);
        productImport.add(ProductType.CAR);

        Scenario s =  simulator.createScenario("scenarioBootstrap", map1, new TimeRestrictions(1900, 1915),
                null, null, new PortBehaviour(productImport, ProductType.STEEL),  new Budget(999999999));
        Simulation.getInstance(s);
        Simulator.getInstance().getScenarioRepository().setActiveScenario(s.getName());
        City city1 = map1.createCity("cityUM", map1.createLocation(new Position(14,14)), 100, 400,ProductType.GRAINS);
        city1.createHouseBlockList();
        city1.createHouseBlock(14, 15);

        s.createIndustry(ProductType.STEEL, map1.createLocation(new Position(12, 12)));
        s.createIndustry(ProductType.IRON, map1.createLocation(new Position(12, 13)));
        s.createIndustry(ProductType.COAL, map1.createLocation(new Position(12, 14)));
        s.createPort(map1.createLocation(new Position(14, 12)));
        StationType st1 = s.createStation("Porto", "TERMINAL", map1.createLocation(new Position(13,13)), null, 100, 100);

        City city2 = map1.createCity("cityDOIS", map1.createLocation(new Position(44,44)), 50, 20, ProductType.IRON);
        city2.createHouseBlockList();
        city2.createHouseBlock(44, 45);
        s.createIndustry(ProductType.BREAD, map1.createLocation(new Position(52, 52)));
        s.createIndustry(ProductType.GRAINS, map1.createLocation(new Position(52, 53)));
        s.createIndustry(ProductType.GRAINS, map1.createLocation(new Position(52, 54)));
        StationType st2 =  s.createStation("Lisboa", "DEPOT", map1.createLocation(new Position(53,53)), null, 100, 100);

        s.createIndustry(ProductType.IRON, map1.createLocation(new Position(60, 13)));
        s.createIndustry(ProductType.COAL, map1.createLocation(new Position(60, 14)));
        s.createIndustry(ProductType.RUBBER, map1.createLocation(new Position(60, 15)));
        s.createIndustry(ProductType.CAR, map1.createLocation(new Position(62, 13)));
        s.createIndustry(ProductType.STEEL, map1.createLocation(new Position(62, 14)));
        StationType st3 = s.createStation("Faro", "TERMINAL", map1.createLocation(new Position(61,14)), null, 100, 100);

        City city3 = map1.createCity("cityTRES", map1.createLocation(new Position(96,96)), 200, 500, ProductType.IRON);
        city3.createHouseBlockList();
        city3.createHouseBlock(94, 95);
        city3.createHouseBlock(95, 95);
        city3.createHouseBlock(96, 95);
        s.createIndustry(ProductType.WOOL, map1.createLocation(new Position(94, 93)));
        s.createIndustry(ProductType.WOOL, map1.createLocation(new Position(95, 93)));
        s.createIndustry(ProductType.CLOTHING, map1.createLocation(new Position(96, 93)));
        StationType st4 = s.createStation("Braga", "TERMINAL", map1.createLocation(new Position(95, 94)), null, 100, 100);

        s.createRailwayLine(st1, st2, "SINGLE_RAIL", "NON_ELECTRIFIED", RailwayLine.calculateDistBetweenStations(st1, st2, map1.getScale()));
        s.createRailwayLine(st2, st3, "SINGLE_RAIL", "NON_ELECTRIFIED", RailwayLine.calculateDistBetweenStations(st2, st3, map1.getScale()));
        s.createRailwayLine(st1, st4, "SINGLE_RAIL", "NON_ELECTRIFIED", RailwayLine.calculateDistBetweenStations(st1, st4, map1.getScale()));

        Train train1 = new Train(LocomotiveModel.STEPHENSON_ROCKET, s.getTrainRepository().getNewID());
        s.getTrainRepository().addTrain(train1);

        List<Cargo> portoCargo = new ArrayList<>();
        portoCargo.add(new Cargo(new Product(ProductType.STEEL), 1));
        PointOfRoute p1 = new PointOfRoute(st1, portoCargo, CargoMode.AVAILABLE);

        List<Cargo> lisboaCargo = new ArrayList<>();
        lisboaCargo.add(new Cargo(new Product(ProductType.BREAD), 1));
        PointOfRoute p2 = new PointOfRoute(st2, lisboaCargo, CargoMode.AVAILABLE);

        List<PointOfRoute> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);

        train1.setCurrentLocation(st1);
        train1.setActive(true);
        s.getRouteRepository().createAndSaveRoute(points, train1, Simulation.getInstance());
    }
}