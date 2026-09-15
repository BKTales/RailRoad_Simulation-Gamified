package pt.ipp.isep.dei.USoutOfProgram.US027;

import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;

public class ShortestWay {
    private int[][] connections;
    private MultiGraph graph;

    private String[] stationsFrom;
    private String[] stations;
    private int[] stationsFromIndex;
    private int[] costs;

    private int[] stationsToGoThrough;
    private int startingPoint;
    private int totalCostOfTrips;

    public ShortestWay(String[] stations, String[][] stationConnections, int[] stationIndexes, MultiGraph graph) {
        this.stations = stations;
        this.graph = graph;
        stationsToGoThrough = stationIndexes;
        connections = getStationsConnectionsToInt(stationConnections);

        costs = new int[stations.length];
        stationsFrom = new String[stations.length];
        stationsFromIndex = new int[stations.length];
        totalCostOfTrips = 0;
    }

    // =========== setup ===========

    /**
     * Search for the station in the stations array
     *
     * @param station name of station to search for
     * @return (wanted station index) (-1 not found)
     */
    private int getStationIndex(String station) {
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].equals(station))
                return (i);
        }
        return (-1);
    }

    /**
     * Will set the connection from which to which station, with their cost in each intersection
     *
     * @param connectReadFromFile connections read from file, splited by "\n" and ";"
     * @return the created int[][] array of connections
     */
    private int[][] getStationsConnectionsToInt(String[][] connectReadFromFile) {
        connections = new int[stations.length][stations.length];

        for (int i = 0; i < stations.length; i++) { // loop through each station
            for (int j = 0; j < connectReadFromFile.length; j++) { // loop through each connection
                if (connectReadFromFile[j][0].equals(stations[i])) {
                    int stationIndex = getStationIndex(connectReadFromFile[j][1]);
                    connections[i][stationIndex] = Integer.parseInt(connectReadFromFile[j][3]);
                    connections[stationIndex][i] = connections[i][stationIndex];
                }
            }
        }
        return (connections);
    }

    // =========== runnable ===========

    /**
     * Function will execute the dijkstra algorithm get the shortestPath
     *
     * @return the path to be used formated in String
     */
    public String run() {
        System.out.println("==========stations==========");
        for (int i = 0; i < stations.length; i++) {
            System.out.println("Station: " + stations[i]);
        }
        System.out.println();
        System.out.println("==========connections==========");
        for (int i = 0; i < connections.length; i++) {
            for (int j = 0; j < connections.length; j++) {
                System.out.print(connections[i][j] + " ");
            }
            System.out.println();
        }


        String shortestWay = "";
        String s = null;

        for (int i = 0; i < stationsToGoThrough.length - 1; i++) {
            startingPoint = stationsToGoThrough[i];
            s = getShortestWay(stationsToGoThrough[i], stationsToGoThrough[i + 1]);
            if (s == null)
                return "Impossible Path!";
            shortestWay += s;
            if (i != stationsToGoThrough.length - 2)
                shortestWay += " ==> ";
        }
        shortestWay += "\n";
        shortestWay += "total cost = " + totalCostOfTrips;
        return (shortestWay);
    }

    /**
     * Function will get the shortest way to the wanted station
     * Math: function will execute the dikjstra algorithm
     *
     * @return (String with the shortest path) (null, if there is no path to go through to get to the wanted station)
     */
    private String getShortestWay(int startingStation, int wantedStation) {
        // initilizing variables
        for (int i = 0; i < stations.length; i++)
            costs[i] = 10000000;
        costs[startingStation] = 0;


        for (int i = startingStation; i < stations.length; i++) {
            for (int j = 0; j < stations.length; j++) {
                if (i == j)
                    continue;
                if (connections[i][j] != 0) {
                    if ((costs[i] + connections[i][j]) < costs[j]) {
                        costs[j] = costs[i] + connections[i][j];
                        stationsFrom[j] = stations[i];
                        stationsFromIndex[j] = i;
                    }
                }
            }
        }
        for (int i = (startingStation - 1); i >= 0; i--) {
            for (int j = 0; j < stations.length; j++) {
                if (i == j)
                    continue;
                if (connections[i][j] != 0) {
                    if ((costs[i] + connections[i][j]) < costs[j]) {
                        costs[j] = costs[i] + connections[i][j];
                        stationsFrom[j] = stations[i];
                        stationsFromIndex[j] = i;
                    }
                }
            }
        }

        for (int j = 0; j < stations.length; j++) {
            System.out.println(stationsFromIndex[j] + " = " + stationsFrom[j] + ": " + costs[j]);
        }
        return (transformingAnswerToString(wantedStation, startingPoint));
    }

    private String transformingAnswerToString(int wantedStation, int startingPoint) {
        ArrayList<String> answer = new ArrayList<>();
        String answerString = "";
        int index;

        if (stationsFrom[wantedStation] == null)
            return (null);

        index = wantedStation;
        answer.add(stations[index]);
        totalCostOfTrips += costs[index];

        while (index != startingPoint) {
            index = stationsFromIndex[index];
            answer.add(stations[index]);
        }

        for (int i = 0; i < (answer.size() - 1); i++) {
            String lineID1 = answer.get(i) + answer.get(i + 1);
            String lineID2 = answer.get(i + 1) + answer.get(i);
            if (graph.getEdge(lineID1) != null) {
                graph.getEdge(lineID1).setAttribute("ui.class", "colorline");
            } else if (graph.getEdge(lineID2) != null) {
                graph.getEdge(lineID2).setAttribute("ui.class", "colorline");
            }
        }


        for (int i = (answer.size() - 1); i >= 0; i--) {
            answerString += answer.get(i);
            if (i != 0) {
                answerString += " ==> ";
            }
        }
        return (answerString);
    }
}

