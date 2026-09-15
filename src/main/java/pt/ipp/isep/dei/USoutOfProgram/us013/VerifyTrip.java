package pt.ipp.isep.dei.USoutOfProgram.us013;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VerifyTrip {
    private final int DEPOT = 1;
    private final int STATION = 2;
    private final int TERMINAL = 3;
    private final char DEPOT_CHAR = 'D';
    private final char STATION_CHAR = 'S';
    private final char TERMINAL_CHAR = 'T';
    private final int ELECTRIC = 2;

    private boolean ignoreNormalRails;

    public VerifyTrip() {
        ignoreNormalRails = false;
    }

    //=============== Utils functions ===============//

    public boolean isOfType(char type, int[] stationTypes){
        for(int i = 0; i < stationTypes.length; i++){
            if (stationTypes[i] == DEPOT && type == DEPOT_CHAR)
                return true;
            else if (stationTypes[i] == STATION && type == STATION_CHAR)
                return true;
            else if (stationTypes[i] == TERMINAL && type == TERMINAL_CHAR)
                return true;
        }
        return (false);
    }

    private boolean[][] multiplyBooleanMatrices(boolean[][] a, boolean[][] b) {
        int n = a.length;
        boolean[][] result = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boolean sum = false;
                for (int k = 0; k < n; k++) {
                    sum = sum || (a[i][k] && b[k][j]);
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    private boolean[][] addBooleanMatrices(boolean[][] matrix1, boolean[][] matrix2) {
        int n = matrix1.length;
        boolean[][] result = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = matrix1[i][j] || matrix2[i][j];
            }
        }
        return result;
    }

    private int getStationIndex(String[] stations, String name){
        for(int i = 0; i < stations.length; i++){
            if(stations[i].equals(name))
                return (i);
        }
        return (-1);
    }

    private int[] connectionToStation(String stationName, String[] stations, String[][] connections) {
        List<Integer> list = new ArrayList<>();

        for(int i = 0; i < connections.length; i++){
            if (connections[i][0].equals(stationName) && (!ignoreNormalRails || connections[i][2].equals("1")))
                list.add(getStationIndex(stations, connections[i][1]));
            if (connections[i][1].equals(stationName) && (!ignoreNormalRails || connections[i][2].equals("1")))
                list.add(getStationIndex(stations, connections[i][0]));
        }
        int[] result = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            result[i] = list.get(i);
        }
        return (result);
    }

    private char[] convertStationTypesToChars(int[] stationTypes){
        char[] typesChar = new char[stationTypes.length];

        for (int i = 0; i < stationTypes.length; i++) {
            if (stationTypes[i] == 1)
                typesChar[i] = 'D';
            if (stationTypes[i] == 2)
                typesChar[i] = 'S';
            if (stationTypes[i] == 3)
                typesChar[i] = 'T';
        }
        return (typesChar);
    }

    //=============== Transitive matrix  functions ===============//

    /**
     * Function will be used to check if one certain station can another selected one.</p>
     * Math explanation: Function will check if the matrix after all the operation is a transitive matrix.
     *
     * @param matrix matrix after all the operations
     * @return (true - if it is transitive) (false - if it is not)
     */
     public static boolean isTransitive(boolean[][] matrix) {
        int n = matrix.length;

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (matrix[i][j])
                {
                    for (int k = 0; k < n; k++)
                    {
                        if (matrix[j][k])
                        {
                            if (!matrix[i][k])
                            {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Function will run through all the stations looking for the wanted type, </p>
     * if it is, the loop will check if has any connections, if so, loop continues </p>
     * otherwise, it will return false
     * @param matrix ( the matrix that hold the connections)
     * @param stations (the array with the map stations)
     * @param stationTypes (the types that want to be checked)
     * @return (true - if all wanted types have a connection) (false - if some of them ain't connected to everything)
     */
    public boolean areAllOfTypeOn(boolean[][] matrix, String[] stations ,int[] stationTypes) {
        boolean toReturn = false;

        // 1 - go through every station
        for (int i = 0; i < stations.length; i++) {
            // 2 - check if it is of one of the wanted type(int[] types)
            if (isOfType(stations[i].charAt(0), stationTypes)) {
                int index = getStationIndex(stations, stations[i]);
                // 3 - check if any of the index of its row in turned on (if so, continue) (if not, return false)
                for (int j = i; j < stations.length; j++) {
                    if (isOfType(stations[j].charAt(0), stationTypes) && !matrix[index][j]){
                        return false;
                    }
                }
            }
        }
        return (true);
    }

    /**
     * Function will check if the matrix after all the operation is all true besides the
     * principal diagonal. This will guarantee us that you can get from any station to
     * another one in the given railway. </p>
     *
     * Math explanation: We are checking if the matrix(representation of our station graph)
     * is connected.
     *
     * @param matrix the adjacentMatrix after the operation
     * @return (true - if it is transitive) (false - if it is not)
     */
    public boolean areAllTripsPossible(boolean[][] matrix) {
         int n = matrix.length;
         for (int i = 0; i < n; i++) {
             for (int j = 0; j < n; j++) {
                 if (!matrix[i][j] && i != j) {
                     return (false);
                 }
             }
         }
         return (true);
    }

    /**
     *  Function will do the (put the algorithm here!), and save the generated matrix in the
     *  adjacent matrix.
     *
     *  math : doing the transitive closure
     *
     * @param adjacentMatrix matrixInputed
     * @param size quantity of sums that will need to be done
     */
    public void multiplyNTimes(boolean[][] adjacentMatrix, int size) {
        boolean[][] sum = new boolean[adjacentMatrix.length][adjacentMatrix.length];
        boolean[][] currentPower = adjacentMatrix;

        // Initialize sum with M¹ (adjacentMatrix)
        for (int i = 0; i < adjacentMatrix.length; i++) {
            System.arraycopy(adjacentMatrix[i], 0, sum[i], 0, adjacentMatrix.length);
        }

        // Compute M², M³, ..., Mⁿ and add to sum
        for (int p = 2; p <= size; p++) {
            currentPower = multiplyBooleanMatrices(currentPower, adjacentMatrix);
            sum = addBooleanMatrices(sum, currentPower);
        }

        // Update the original matrix
        for (int i = 0; i < adjacentMatrix.length; i++) {
            System.arraycopy(sum[i], 0, adjacentMatrix[i], 0, adjacentMatrix.length);
        }
    }

    /**
     * Function will initialize an int array that will hold the adjacent matrix based
     * on a giving list of stations and </p> connections between them
     *
     * @return (matrix of the connections)
     */
    private boolean[][] initializeAdjacencyMatrix(String[] stations, String[][] connections){
        boolean[][] adjacencyMatrix = new boolean[stations.length][stations.length];
        for(int i = 0; i < stations.length; i++){
            for(int j = 0; j < stations.length; j++){
                adjacencyMatrix[i][j] = false;
            }
        }

        for (int i = 0; i < stations.length; i++){
            int[] indexes = connectionToStation(stations[i], stations, connections);
            for(int j = 0; j < indexes.length; j++){
                adjacencyMatrix[indexes[j]][i] = true;
                adjacencyMatrix[i][indexes[j]] = true;
            }
        }
        return (adjacencyMatrix);
    }

    //=============== main options functions ===============//`

    public int verifyTripStationType(File lineFile, int[] stationTypes, String[] stations, int trainType) throws IOException{
        if (trainType == ELECTRIC)
            ignoreNormalRails = true;
        if (stationTypes.length == 3)
            return (verifyPossibilityOfAllStations(lineFile,  stations));

        String[][] connections = ReadCsvs.readCSVTo2DArray(lineFile);
        boolean[][] adjacentMatrix = initializeAdjacencyMatrix(stations, connections);

        multiplyNTimes(adjacentMatrix, stations.length);
        if(areAllOfTypeOn(adjacentMatrix, stations, stationTypes))
            return 1;
        return 0;
    }

    public int verifyTripTwoStations(File lineFile, String station1, String station2, String[] stations, int trainType) throws IOException {
        if (trainType == ELECTRIC)
            ignoreNormalRails = true;
        String[][] connections = ReadCsvs.readCSVTo2DArray(lineFile);
        boolean[][] adjacentMatrix = initializeAdjacencyMatrix(stations, connections);

        multiplyNTimes(adjacentMatrix, stations.length);

        if (adjacentMatrix[getStationIndex(stations, station1)][getStationIndex(stations, station2)])
            return 1;
        return 0;
    }

    public int verifyPossibilityOfAllStations(File lineFile, String[] stations) throws IOException{
        String[][] connections;
        connections = ReadCsvs.readCSVTo2DArray(lineFile);
        boolean[][] adjacentMatrix = initializeAdjacencyMatrix(stations, connections);

        multiplyNTimes(adjacentMatrix, stations.length);

        if (areAllTripsPossible(adjacentMatrix))
            return 1;
        return 0;
    }
}