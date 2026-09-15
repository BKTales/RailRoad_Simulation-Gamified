package pt.ipp.isep.dei.us011;

import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.domain.simulation.Simulation;
import pt.ipp.isep.dei.repository.TrainRepository;
import pt.ipp.isep.dei.us009.BuyLocomotiveController;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsible for managing the listing and sorting of purchased trains.
 * It interacts with the {@link TrainRepository} to retrieve train data and delegates
 * sorting and formatting to the {@link SortTrains} utility class.
 */
public class ListAllTrainsController implements Serializable {
    private static final long serialVersionUID = 1L;
    private TrainRepository trainRepository;
    private SortTrains sortTrains;
    private Simulation instance;

    /**
     * Initializes the references to the repositories used by this controller.
     * <p>
     * Retrieves the singleton instance of the {@link Simulation} class and obtains
     * the {@link TrainRepository} from it.
     * </p>
     */
    private void initializeRepositories() {
        instance = Simulation.getInstance();
        this.trainRepository= instance.getTrainRepository();
    }

    /**
     * Constructs a new {@code ListAllTrainsController} instance.
     * <p>
     * Initializes the repositories and sets up the sorting utility used for train listing.
     * </p>
     */
    public ListAllTrainsController() {
        initializeRepositories();
        this.sortTrains = new SortTrains();
    }

    /**
     * Retrieves all trains currently stored in the train repository.
     *
     * @return a list of all purchased {@link Train} objects
     */
    public List<Train> getAllTrainsPurchased() {
        return trainRepository.getTrains();
    }

    /**
     * Returns a sorted list of trains.
     * Sorting is done by locomotive type and then by locomotive name.
     *
     * @param allTrains the list of trains to be sorted
     * @return a sorted list of {@link Train} objects
     */
    public List<Train> getSortedListOfAllTrainsPurchased(List<Train> allTrains) {
        return sortTrains.sortTrains(allTrains);
    }
    /**
     * Retrieves a list of all purchased trains as formatted strings.
     * <p>
     * The trains are first retrieved from the repository, then sorted by locomotive type
     * and name using the {@link SortTrains} utility. The sorted train list is converted into
     * a single formatted string, where each line represents a train with its details.
     * This string is then split into a list of individual strings, each representing one train.
     * </p>
     *
     * @return a list of formatted strings, each describing a purchased train;
     *         returns an empty list if there are no trains.
     */
    public List<String> getListOfAllTrains() {
        List<Train> allTrains = getAllTrainsPurchased();
        List<Train> sortedTrains = getSortedListOfAllTrainsPurchased(allTrains);
        String trainsString = sortTrains.convertTrainListToString(sortedTrains);

        if (trainsString == null || trainsString.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(trainsString.split("\\r?\\n"));
    }

}
