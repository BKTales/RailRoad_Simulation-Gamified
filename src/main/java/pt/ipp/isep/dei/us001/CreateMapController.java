package pt.ipp.isep.dei.us001;

import pt.ipp.isep.dei.Serializer.Save;
import pt.ipp.isep.dei.repository.Simulator;

import java.io.*;


public class CreateMapController implements Serializable {
    private static final long serialVersionUID = 1L;
    private Simulator simulator;

    public CreateMapController() {
        simulator = Simulator.getInstance();
    }

    /**
     * Instance all the needed classes in order to create Map and create the Object
     * @param name mapName
     * @param width mapWidth
     * @param height mapHeight
     * @return (stringMessage of wat was the output from the execution!)
     */
    public String createMap(String name, int width, int height, int scale) {
        String s = "";
            try {
                if (width <= 0 || height <= 0 || scale <= 0) {
                    return null;
                }
                simulator.createMap(name, width, height, scale);
                if (height < 10)
                    s += "The wanted height is less than 10, this can impact in the reality of the simulation!\n";
                if (width < 10)
                    s += "The wanted length is less than 10, this can impact in the reality of the simulation!\n";
                s += "Map Create\n";

                return (s);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
    }

    /**
     * Save the map in the memory
     * @param saveAnswer Yes or no
     * @param mapName name of the map
     * @return (message indicating if the map was saved or not)
     * @throws IOException
     */
    public String saveMap(String saveAnswer, String mapName) throws IOException {
        return (Save.saveMap(saveAnswer, mapName));
    }
}
