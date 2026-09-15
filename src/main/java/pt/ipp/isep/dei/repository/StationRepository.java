package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.position.Position;
import pt.ipp.isep.dei.domain.station.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


/**
 * The {@code StationRepository} class manages a collection of {@link StationType} objects,
 * providing functionality to create, retrieve, list, and delete stations.
 * It supports multiple station types including {@link Station}, {@link Depot}, and {@link Terminal}.
 */
public class StationRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<StationType> stations;

    public StationRepository() {
        stations = new ArrayList<StationType>();
    }

    //========== Manipulation functions ==========//

    /**
     * Returns a list of available upgrades for the specified station.
     * <p>
     * The upgrades depend on the current type of the station:
     * <ul>
     *     <li>If it's a {@link Depot}, it can be upgraded to a {@code Station} or {@code Terminal}.</li>
     *     <li>If it's a {@link Station}, it can be upgraded to a {@code Terminal}.</li>
     *     <li>If it's already a {@code Terminal}, no upgrades are available.</li>
     * </ul>
     * </p>
     *
     * <p>The returned string includes an indexed list of possible upgrades.</p>
     *
     * @param station the station whose upgrade options are to be determined
     * @return a formatted string listing the available upgrades
     */
    public String getAvailableUpgradeStation(StationType station) {
        Upgrade[] upgrades;

        if (station instanceof Depot) {
            upgrades = new Upgrade[]{
                    Upgrade.DEPOTTOSTATION,
                    Upgrade.DEPOTTOTERMINAL
            };
        } else if (station instanceof Station) {
            upgrades = new Upgrade[]{ Upgrade.STATIONTOTERMINAL };
        } else {
            upgrades = new Upgrade[0];
        }

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < upgrades.length; i++) {
            s.append(" [").append(i).append("] - ").append(upgrades[i].toString()).append("\n");
        }

        return s.toString();
    }

    /**
     * Deletes a station located at the specified {@link Position}.
     *
     * @param pos the position of the station to delete
     */
    public void deleteStation(Position pos) {
        for (StationType tmp : stations) {
            if (tmp.getLocation().getPosition().equals(pos)) {
                stations.remove(tmp);
                break;
            }
        }
    }

    /**
     * Deletes the specified {@link StationType} from the repository.
     *
     * @param station the station to be removed
     */
    public void deleteStation(StationType station) {
        stations.remove(station);
    }


    //========== Get functions ==========//

    /**
     * Retrieves a station by its name.
     *
     * @param name the name of the station
     * @return the matching {@link StationType}, or {@code null} if not found
     */
    public StationType getStation(String name) {
        for (StationType tmp : stations) {
            if(tmp.getName().equals(name))
                return tmp;
        }
        return null;
    }

    /**
     * Retrieves a station by its index in the repository.
     *
     * @param index the index of the station
     * @return the {@link StationType} at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public StationType getStation(int index) {
        if (index < 0 || index >= stations.size())
            throw new IndexOutOfBoundsException("Index out of bounds");
        return stations.get(index);
    }

    /**
     * Returns the information of the station at the specified index.
     *
     * @param index the index of the station
     * @return a string containing the station's information
     */
    public String getStationInfo(int index) {
        return getStation(index).getInfo();
    }

    /**
     * Returns a copy of the list of all stations currently stored in the repository.
     * Modifications to the returned list will not affect the internal state of the repository.
     *
     * @return a new {@link ArrayList} containing all {@link StationType} objects
     */
    public ArrayList<StationType> getStations() {
        return (stations);
    }

    /**
     * Returns a formatted list of all available station types.
     *
     * @return a string listing station types with indices
     */
    public String getListOfStationTypes() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < (StationTypes.values().length); i++) {
            StationTypes stationTypes = StationTypes.values()[i];
            s.append(" [").append(i).append("] - ").append(stationTypes.toString()).append("\n");
        }

        return (s.toString());
    }

    public void addStation(StationType station) {
        stations.add(station);
    }

    //========== Utils functions ==========//

    /**
     * Returns string listing all stations with its index.
     *
     * @return a string listing all stations
     */
    public String listAllStations() {
        StringBuilder s = new StringBuilder();
        int i = 0;

        for (StationType tmp : stations) {
            s.append("[").append(i).append("] ").append(tmp.toString()).append("\n");
            i++;
        }
        return s.toString();
    }

    /**
     * Finds a station by its associated map name.
     * Currently not implemented.
     *
     * @param mapName the name of the map
     * @return the matching {@link StationType}, or {@code null} if not found
     */
    public StationType findByMap(String mapName){
        return null;
    }

    public void clean() {
        stations.clear();
    }

    public String getStationsInfo() {
        int manyStations = stations.size();
        String info = "";

        if (manyStations != 0){
            for (int i = 0; i < manyStations; i++) {
                info += stations.get(i).toString() + "\n";
            }
        }
        else {
            info += "No Stations Were Created!\n";
        }
        return info;
    }
}
