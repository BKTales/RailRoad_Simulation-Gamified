package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Map;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The {@code MapRepository} class manages a collection of {@link Map} objects.
 * It provides methods to create, retrieve, and delete maps by name or object reference.
 */
public class MapRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ArrayList<Map> maps;

    /**
     * Constructs an empty {@code MapRepository}.
     */
    public MapRepository() {
        maps = new ArrayList<Map>();
    }
    //========== Manipulation functions ==========//

    /**
     * Add a map to the array
     * @param map
     */
    public void addMap(Map map){
        maps.add(map);
    }

    /**
     * Deletes the specified {@link Map} from the repository.
     *
     * @param map the map to be removed
     */
    public void deleteMap(Map map){ maps.remove(map); }

    /**
     * Deletes a map with certain name from the repository
     *
     * @param name the name of the map to remove
     */
    public void deleteMap(String name)  {
        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).getName() == name) {
                maps.remove(i);
                break ;
            }
        }
    }

    //========== Get functions ==========//

    /**
     * Retrieves a map from the repository by its name.
     *
     * @param name the name of the map to retrieve
     * @return  {@link Map} object or {@code null} if not found
     */
    public Map getMap(String name) {
        for (Map m : maps)
            if (m.getName().equals(name))
                return (m);
        return null;
    }

    public ArrayList<Map> getAllMaps(){
        return (this.maps);
    }

    public ArrayList<String> getAllMapNames() {
        ArrayList<String> mapNames = new ArrayList<>();
        for (Map m : maps) {
            mapNames.add(m.getName());
        }
        return mapNames;
    }
    //========== Utils functions ==========//

    /**
     * Prints all maps currently stored in the repository to the standard output.
     */
    public void printMaps() {
        for (Map m : maps) {
            System.out.println(m);
        }
    }

    /**
     * Retrieves the single map in the repository if only one exists.
     *
     * @return the single {@link Map} if exactly one exists, otherwise {@code null}
     */
    public Map getMaps() {
        if (maps.size() == 1)
            return maps.get(0);
        return null;
    }

    public boolean isEmpty() {
        return maps.isEmpty();
    }
}
