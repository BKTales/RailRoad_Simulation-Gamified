package pt.ipp.isep.dei.us004;

import pt.ipp.isep.dei.domain.Budget;
import pt.ipp.isep.dei.repository.Simulator;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.scenario.*;
import pt.ipp.isep.dei.domain.ProductType;
import pt.ipp.isep.dei.domain.user.Player;
import pt.ipp.isep.dei.domain.user.User;
import pt.ipp.isep.dei.repository.MapRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller responsible for managing the creation of scenarios.
 * Now works with the adapter pattern for data transfer.
 */
public class ScenarioCreationController implements Serializable {
    private static final long serialVersionUID = 1L;
    private TimeRestrictions timeRestrictions;
    private TechnologyRestriction technologyRestriction;
    private ProductRestrictions productRestrictions;
    private PortBehaviour portBehaviour;
    private Budget initialBudget;
    private final Restrictions restrictions = new Restrictions();
    private final Simulator simulator = Simulator.getInstance();
    private final MapRepository mapRepo = simulator.getMapRepository();
    private boolean scenarioCreated = false;
    private Restrictions presetRestrictions;
    String scenarioName;

    /**
     * Creates a time restriction for the scenario based on start and end years.
     *
     * @param startYear the start year of the time restriction
     * @param endYear the end year of the time restriction
     */
    public void createTimeRestriction(int startYear, int endYear){
        timeRestrictions = restrictions.createTimeRestriction(startYear, endYear);
    }

    public void createTechnologicalRestriction(List<TechnologyType> restricted) {
        technologyRestriction = restrictions.createTechnologicalRestriction(restricted);
    }

    public void addHistoricalRestriction(String productName, int timeToProduce, double multiplier) {
        restrictions.addHistoricalProductRestriction(productName, timeToProduce, multiplier);
    }

    public void markProductAsUnavailable(String productName) {
        restrictions.markProductAsUnavailable(productName);
    }

    public List<String> getAvailableProductNames() {
        return Arrays.stream(ProductType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public void createPortBehaviour(List<Integer> importedProductsIndex, int exportedValueIndex) {
        portBehaviour = restrictions.createPortBehaviour(importedProductsIndex, exportedValueIndex);
    }

    public void setInitialBudget(double initialFunds) {
        if (initialFunds < 0) {
            throw new IllegalArgumentException("Budget cannot be negative.");
        }
        initialBudget = simulator.getScenarioRepository().setInitialBudget(initialFunds);
    }

    public void createScenario(String name, String attachedMapName) {
        scenarioName = name;
        Map map = mapRepo.getMap(attachedMapName);
        if (Objects.isNull(map)) {
            throw new NullPointerException("Please attach a map to the scenario.");
        }


        simulator.createScenario(name, map, timeRestrictions, productRestrictions,technologyRestriction, portBehaviour, initialBudget);

        scenarioCreated = true;

        User currentUser = simulator.getAuthenticationRepository().getCurrentUserSession().getUser();
        if (currentUser instanceof Player) {
            Player player = (Player) currentUser;
            player.setBudgetAmount(initialBudget.getFunds());
        }
    }

    public boolean successScenarioCreationFlag() {
        if(simulator.getScenarioRepository().getScenarios().contains(simulator.getScenarioRepository().getScenario(scenarioName))) {
            scenarioCreated = false;
            return true;
        }
        return false;
    }

    public void saveScenario(Scenario scenario) throws IOException {
        try (ObjectOutputStream objectOut = new ObjectOutputStream(
                new FileOutputStream("scenario_savefile.bin"))) {
            objectOut.writeObject(scenario);
        }
    }

    public List<String> getAllMapNames() {
        return mapRepo.getAllMapNames();
    }

    public Scenario getScenarioByName(String name) {
        return simulator.getScenarioRepository().getScenario(name);
    }

    public ProductRestrictions getProductRestrictions() {
        return productRestrictions;
    }

    public PortBehaviour getPortBehaviour() {
        return portBehaviour;
    }

    public TechnologyRestriction getTechnologyRestriction() {
        return technologyRestriction;
    }

    public String getPresetRestrictions() {
        return restrictions.getPresetRestrictionsName();
    }

    public void initiateHistoricalRestriction(String choice) {
        presetRestrictions = restrictions.presetRestrictions(choice);
    }

    public Restrictions getRestriction() {
        return presetRestrictions;
    }
}