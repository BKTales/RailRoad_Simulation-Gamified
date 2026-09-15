package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.train.Locomotive;
import pt.ipp.isep.dei.domain.train.LocomotiveModel;
import pt.ipp.isep.dei.domain.train.Train;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code TrainRepository} class provides functionality for managing
 * a collection of {@link Train} objects. It allows creation, retrieval,
 * listing, and deletion of trains.
 */
public class TrainRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Train> trains;
    private static int id = 1;

    /**
     * Constructs a new, empty {@code TrainRepository}.
     */
    public TrainRepository() {
         trains = new ArrayList<Train>();
    }

    //========== Manipulation functions ==========//

    /**
     * Deletes the specified {@link Train} from the repository.
     *
     * @param t the train to be removed
     */
    public void deleteTrain(Train t) {
        trains.remove(t);
    }

    //========== Get functions ==========//

    /**
     * Retrieves the train at the specified index in the repository.
     *
     * @param index the index of the train to retrieve
     * @return the {@link Train} at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Train getTrain(int index) {
        if (index < 0 || index >= trains.size())
            throw new IndexOutOfBoundsException("Index out of bounds");
        return trains.get(index);
    }

    /**
     * Get the id to create the current train
     * @return (id)
     */
    public int getNewID(){
        int nowId = id;
        id++;
        return (nowId);
    }


    //========== Utils functions ==========//

    /**
     * Returns a string listing all trains with index
     *
     * @return a string with all trains
     */
    public String listTrains() {
        String s = "";
        int i = 0;

        for (Train t : trains) {
            s += "[" + t.getId() + "] " + t.toString() + "\n";
            i++;
        }
        return s;

    }

    /**
     * Get the info of all the train bought and put them in a string array
     * @return string array
     */
    public String getTrainsInfo(){
        int manyTrains = trains.size();
        String info = "";

        if (manyTrains != 0){
            for (int i = 0; i < manyTrains; i++) {
                info += trains.get(i).toString() + "\n";
            }
        }
        else
            info = "No Trains Were Created!\n";
        return info;
    }


    /**
     * Retrieves a copy of the list of all stored trains.
     * <p>
     * This method returns a new list containing the same {@link Train} objects
     * to prevent external modification of the internal repository list.
     *
     * @return a list of all trains currently in the repository
     */
    public List<Train> getTrains() {
        return new ArrayList<>(trains);
    }

    public void clean() {
        trains.clear();
    }


    /**
     * add a train to the Train array
     * @param train train to be added to the array
     */
    public void addTrain(Train train) {
        trains.add(train);
    }
}

