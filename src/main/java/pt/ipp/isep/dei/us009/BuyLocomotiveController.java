package pt.ipp.isep.dei.us009;

import pt.ipp.isep.dei.repository.TrainRepository;
import pt.ipp.isep.dei.domain.scenario.Scenario;
import pt.ipp.isep.dei.domain.simulation.TimeCounter;
import pt.ipp.isep.dei.domain.train.LocomotiveModel;
import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.domain.simulation.Simulation;

import java.io.Serializable;
import java.util.List;

/**
 * Controller responsible for managing the purchase of locomotives.
 * <p>
 * This class handles the business logic for locomotive acquisition,
 * including budget validation, available technologies, time restrictions,
 * and train creation based on selected locomotive models.
 * <p>
 * Follows GRASP principles by delegating domain responsibilities to the {@link Scenario}.
 */
public class BuyLocomotiveController implements Serializable {
    private static final long serialVersionUID = 1L;
    private Simulation instance;
    private Scenario scenario;
    private TimeCounter timeCounter;

    /**
     * Constructs a new {@code BuyLocomotiveController} using the given {@link TimeCounter} instance.
     * Initializes access to the current scenario and shared repository.
     *
     * @param timeCounter the simulation context used to determine the current year
     */
    public BuyLocomotiveController(TimeCounter timeCounter) {
        this.timeCounter = timeCounter;
        initializeRepositories();
    }

    /**
     * Initializes internal references to the repository and current scenario.
     */
    private void initializeRepositories() {
        instance = Simulation.getInstance();
        scenario = instance.getCurrentScenario();
    }

    /**
     * Returns the current available budget in the scenario.
     *
     * @return the current amount of funds available
     */
    public double getCurrentBudget() {
        return  scenario.getBudget().getFunds();
    }

    /**
     * Returns the current simulation year.
     *
     * @return the current year of the simulation
     */
    public int getCurrentYear() {
        return timeCounter.getCurrentYear();
    }

    /**
     * Retrieves the list of available locomotive models based on the current year,
     * scenario's time restrictions, and allowed technologies.
     *
     * @return list of available {@link LocomotiveModel} instances
     */
    public List<LocomotiveModel> getAvailableLocomotives() {
        int currentYear = getCurrentYear();
        return scenario.getAvailableLocomotives(currentYear);
    }

    /**
     * Attempts to purchase a locomotive based on the given {@link LocomotiveModel}.
     * <p>
     * This method delegates the creation and validation of the train to the {@link Scenario}.
     * If the purchase is successful (i.e., there is enough budget), the train is created and added
     * to the {@link TrainRepository}. Otherwise, an error message is printed, and {@code null} is returned.
     * </p>
     *
     * @param model the {@link LocomotiveModel} selected for purchase
     * @return the created {@link Train} if the purchase was successful; {@code null} otherwise
     */
    public Train buyLocomotive(LocomotiveModel model) {
        Train train = scenario.purchaseLocomotive(model);

        if (train == null){
            return  null;
        }else{
            Simulation.getInstance().getTrainRepository().addTrain(train);
            return train;
        }
    }


}
