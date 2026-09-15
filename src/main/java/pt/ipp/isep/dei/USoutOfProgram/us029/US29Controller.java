package pt.ipp.isep.dei.USoutOfProgram.us029;

import pt.ipp.isep.dei.USoutOfProgram.us013.ReadCsvs;
import pt.ipp.isep.dei.USoutOfProgram.us013.VerifyTripController;
import pt.ipp.isep.dei.USoutOfProgram.us014.GraphBuilder;
import pt.ipp.isep.dei.USoutOfProgram.us014.FleuryAlgorithm;
import org.graphstream.graph.implementations.MultiGraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controller class for executing tests related to use case 29.
 *
 * <p>It loads datasets, runs two algorithms (US13 and US14), measures their execution times,
 * and writes results to separate CSV files.</p>
 */
public class US29Controller {

    private static final String DATA_DIR = "src/main/dataUS29/";
    private static final String US13_RESULTS_CSV = "src/main/java/pt/ipp/isep/dei/USoutOfProgram/us029/results/us13_times.csv";
    private static final String US14_RESULTS_CSV = "src/main/java/pt/ipp/isep/dei/USoutOfProgram/us029/results/us14_times.csv";
    private static final String CSV_HEADER = "size,time";

    private final US29UI ui;

    /**
     * Creates a new controller with the specified UI.
     *
     * @param ui the UI interface to display messages and results
     */
    public US29Controller(US29UI ui) {
        this.ui = ui;
    }

    /**
     * Runs performance tests for multiple datasets representing different scenario sizes.
     * <p>
     * For each dataset, it loads the corresponding station and line files, executes
     * the algorithms from US13 and US14, records their execution times, and writes
     * the results to two separate CSV files.
     */
    public void runTests() {
        try {
            List<String> stationFiles = generateStationFileNames(50, 1500, 50);
            List<DataSet> datasets = loadDataSets(stationFiles);

            try (
                    BufferedWriter writer13 = new BufferedWriter(new FileWriter(US13_RESULTS_CSV));
                    BufferedWriter writer14 = new BufferedWriter(new FileWriter(US14_RESULTS_CSV))
            ) {
                writer13.write(CSV_HEADER);
                writer13.newLine();
                writer14.write(CSV_HEADER);
                writer14.newLine();

                for (DataSet dataset : datasets) {
                    processDataset(dataset, writer13, writer14);
                }
            }

            ui.showMessage("Tests completed! Results saved in:\n" +
                    "- " + US13_RESULTS_CSV + "\n" +
                    "- " + US14_RESULTS_CSV);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a list of station CSV file paths using a numerical sequence.
     *
     * @param start the starting scenario number (inclusive)
     * @param end   the ending scenario number (inclusive)
     * @param step  the increment step between scenarios
     * @return a list of file paths for station CSVs
     */
    private List<String> generateStationFileNames(int start, int end, int step) {
        return IntStream.iterate(start, i -> i <= end, i -> i + step)
                .mapToObj(i -> DATA_DIR + "Scenario_" + i + "_stations.csv")
                .collect(Collectors.toList());
    }

    /**
     * Loads station and line data for each scenario and creates a list of datasets.
     *
     * @param stationFiles list of paths to station CSV files
     * @return a list of datasets containing station and line information
     * @throws IOException if any file cannot be read
     */
    private List<DataSet> loadDataSets(List<String> stationFiles) throws IOException {
        List<DataSet> datasets = new ArrayList<>();

        for (String stationsPath : stationFiles) {
            String linesPath = stationsPath.replace("stations", "lines");

            List<String> allLinesStations = Files.readAllLines(Paths.get(stationsPath));
            if (allLinesStations.isEmpty()) {
                datasets.add(new DataSet(Paths.get(stationsPath).getFileName().toString(), new String[0], new String[0][0]));
                continue;
            }

            String[] stations = allLinesStations.get(0).split(",");
            List<String[]> linesList = new ArrayList<>();
            List<String> allLinesLines = Files.readAllLines(Paths.get(linesPath));

            for (int i = 1; i < allLinesLines.size(); i++) {
                String[] parts = allLinesLines.get(i).split(";");
                if (parts.length >= 3) {
                    linesList.add(new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()});
                }
            }

            ui.showMessage("Loaded lines for " + linesPath + ":");

            datasets.add(new DataSet(Paths.get(stationsPath).getFileName().toString(), stations, linesList.toArray(new String[0][0])));
        }
        return datasets;
    }

    /**
     * Extracts the scenario number from a filename.
     *
     * @param filename the filename (e.g. "Scenario_300_stations.csv")
     * @return the extracted number (e.g. 300)
     */
    private int extractScenarioNumber(String filename) {
        return Integer.parseInt(filename.replaceAll("\\D+", ""));
    }

    /**
     * Processes a dataset by executing US13 and US14 algorithms, measuring their execution times,
     * and writing the results to the respective CSV writers.
     *
     * @param dataset  the dataset to process
     * @param writer13 the writer for US13 CSV results
     * @param writer14 the writer for US14 CSV results
     * @throws IOException if writing to either file fails
     */
    private void processDataset(DataSet dataset, BufferedWriter writer13, BufferedWriter writer14) throws IOException {
        ui.showMessage("Running test for dataset: " + dataset.getName());

        if (dataset.getStations().length == 0) {
            ui.showMessage("Dataset " + dataset.getName() + " does not contain stations. Skipping...");
            return;
        }

        MultiGraph graph = GraphBuilder.buildGraph(dataset.getStations(), dataset.getLines());

        long time13 = measureExecutionTime(() -> {
            try {
                VerifyTripController controller = new VerifyTripController();
                int scenarioNumber = extractScenarioNumber(dataset.getName());
                File stationFile = controller.getStationFileUS29(scenarioNumber);
                File lineFile = controller.getLineFileUS29(scenarioNumber);
                String[] stations = ReadCsvs.getAllStations(stationFile);
                controller.runStationTypesUS29(stationFile, lineFile, new int[]{1, 2, 3}, stations, 1); // Steam
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        long time14 = measureExecutionTime(() -> {
            Map<String, Integer> degrees = FleuryAlgorithm.calculateVertexDegrees(dataset.getLines(), dataset.getStations());
            Map<String, Integer> oddVertices = FleuryAlgorithm.countOddDegreeVertices(degrees);
            oddVertices.forEach(ui::showOddVertices);
            Map<String, Integer> indexMap = FleuryAlgorithm.createIndexMap(dataset.getStations());
            int[][] adjacencyMatrix = buildAdjacencyMatrix(dataset.getLines(), indexMap);
            List<String> validStations = FleuryAlgorithm.getValidStations(oddVertices, degrees, dataset.getStations());
            if (validStations.isEmpty()) {
                if (dataset.getStations().length > 0) {
                    validStations = List.of(dataset.getStations()[0]);
                    ui.showMessage("No odd vertices found. Starting at: " + dataset.getStations()[0]);
                } else {
                    ui.showMessage("No valid station to start Fleury's algorithm in the dataset " + dataset.getName());
                    return;
                }
            }
            ArrayList<String> path = new ArrayList<>();
            path.add(validStations.get(0));
            FleuryAlgorithm.execute(graph, path, adjacencyMatrix, indexMap, dataset.getLines(), degrees);
        });

        int inputSize = dataset.getStations().length;
        writer13.write(inputSize + "," + time13);
        writer13.newLine();
        writer14.write(inputSize + "," + time14);
        writer14.newLine();
    }

    /**
     * Builds an adjacency matrix from line connections and a station index map.
     *
     * @param lines     2D array of line connections [source, destination, otherInfo]
     * @param indexMap  map of station names to matrix indices
     * @return a 2D adjacency matrix representing connections between stations
     */
    private int[][] buildAdjacencyMatrix(String[][] lines, Map<String, Integer> indexMap) {
        int n = indexMap.size();
        int[][] adjacencyMatrix = new int[n][n];

        for (String[] line : lines) {
            String src = line[0];
            String dest = line[1];

            if (!indexMap.containsKey(src) || !indexMap.containsKey(dest)) {
                ui.showMessage("Station not found in indexMap: " + src + " or " + dest);
                continue;
            }

            int i = indexMap.get(src);
            int j = indexMap.get(dest);
            adjacencyMatrix[i][j]++;
            adjacencyMatrix[j][i]++;
        }

        return adjacencyMatrix;
    }

    /**
     * Measures the execution time of a given task in milliseconds.
     *
     * @param task the task to measure
     * @return the execution time in milliseconds
     */
    private long measureExecutionTime(Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        return (end - start) / 1_000_000;
    }
}
