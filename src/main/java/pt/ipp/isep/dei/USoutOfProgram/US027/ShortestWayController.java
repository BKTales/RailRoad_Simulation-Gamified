package pt.ipp.isep.dei.USoutOfProgram.US027;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import pt.ipp.isep.dei.USoutOfProgram.us013.ReadCsvs;
import pt.ipp.isep.dei.USoutOfProgram.us014.GraphBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ShortestWayController {
    private  String [] stations;
    private  String [][] stationsConnections;
    private String filename;
    private MultiGraph graph;

    public int[] readStationsToGoThrough(String filename) {
        String stationsPath[];
        int [] stationsIndexes;

        try{
            File f = new File("src/csvs/" + filename);
            BufferedReader br = new BufferedReader(new FileReader(f));
            stationsPath = br.readLine().split(";");

            stationsIndexes = new int[stationsPath.length];
            // conversion to its positions
            for (int i = 0; i < stationsPath.length; i++) {
                for(int j = 0; j < stations.length; j++){
                    if (stationsPath[i].equals(stations[j])) {
                        stationsIndexes[i] = j;
                        break;
                    }
                }
            }

            return (stationsIndexes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a string with the read stations in filename
     * @param filename filename inputed from user
     * @return the created string
     * @throws IOException errors reading the file
     */
    public String listStations(String filename) throws IOException {
        this.filename = filename;
        try{
            File f = new File("src/csvs/" + filename + "_stations.csv");

            stations = ReadCsvs.getAllStations(f);
            String toReturn = "";
            for (int i = 0; i < stations.length; i++) {
                toReturn += "[" + i + "] - " +  stations[i] + "\n";
            }

            return (toReturn);
        } catch (Exception e) {
            throw new IOException("Error on reading the file: " + filename + "\n");
        }
    }

    /**
     * Read the file where is hold the lines information
     * @throws IOException errors that happen while reading or opening file
     */
    private void getConnections() throws IOException {
        System.out.println("filename: " + filename);
        stationsConnections = ReadCsvs.readCSVTo2DArray(new File("src/csvs/" + filename + "_lines.csv"));
    }

    /**
     * Read the connections in the lines file and execute the call the ShortestWay.run() to execute the algorithm
     * @param stationIndexes stations wanted to be passed through
     * @return the path needed to be taken to get to the wanted stations
     * @throws IOException
     */
    public String getShortestWay(int [] stationIndexes) throws IOException {
        getConnections();

        graph = GraphBuilder.buildGraph(stations, stationsConnections);
        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

        ShortestWay shortestWay = new ShortestWay(stations, stationsConnections, stationIndexes, graph);
        String answer = shortestWay.run();
        return (answer);
    }
}