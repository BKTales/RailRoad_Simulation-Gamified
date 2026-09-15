package pt.ipp.isep.dei.us011;

import pt.ipp.isep.dei.domain.train.Cargo;
import pt.ipp.isep.dei.domain.train.Train;

import java.io.Serializable;
import java.util.*;

/**
 * The {@code SortTrains} class is responsible for sorting a list of trains.
 * Trains are grouped by locomotive type and then sorted alphabetically by locomotive name within each group.
 * It also provides functionality to convert the sorted list into a string representation.
 */
public class SortTrains implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Sorts a list of trains. First, it groups the trains by their locomotive type.
     * Then, each group is sorted alphabetically by the name of the locomotive.
     *
     * @param allTrains the list of trains to sort
     * @return a new list containing all trains sorted by type and then by name
     */
    public List<Train> sortTrains(List<Train> allTrains) {
        Map<String, List<Train>> groupedByType = new HashMap<>();
        for (Train train : allTrains) {
            String locomotiveType = train.getLocomotive().getLocomotiveType().name();
            if (!groupedByType.containsKey(locomotiveType)) {
                groupedByType.put(locomotiveType, new ArrayList<>());
            }
            groupedByType.get(locomotiveType).add(train);
        }

        List<Train> sortedTrains = new ArrayList<>();

        for (Map.Entry<String, List<Train>> entry : groupedByType.entrySet()) {
            List<Train> trainsByType = entry.getValue();

            Collections.sort(trainsByType, new Comparator<Train>() {
                @Override
                public int compare(Train train1, Train train2) {
                    return train1.getLocomotive().getName().compareTo(train2.getLocomotive().getName());
                }
            });

            sortedTrains.addAll(trainsByType);
        }

        return sortedTrains;
    }

    /**
     * Converts a list of trains into a formatted string.
     * Each train is displayed with its ID and the details returned by its {@code toString()} method.
     *
     * @param trainList the list of trains to convert
     * @return a string listing each train in the given list
     */

    public String convertTrainListToString(List<Train> trainList) {
        StringBuilder builder = new StringBuilder();

        for (Train train : trainList) {
            String name = train.getLocomotive().getName();
            String type = train.getLocomotive().getLocomotiveType().name();
            ArrayList<Cargo> cargo = train.getCargo();
            int id = train.getId();

            builder.append("Name: ").append(name)
                    .append("| ID: ").append(id)
                    .append("| Type: ").append(type)
                    .append("| Cargo: ").append(cargo)
                    .append("\n");
        }

        return builder.toString();
    }

}


