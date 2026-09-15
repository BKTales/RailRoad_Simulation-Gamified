package pt.ipp.isep.dei.USoutOfProgram.us013;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import pt.ipp.isep.dei.USoutOfProgram.us014.GraphBuilder;

import java.io.IOException;
import java.util.Scanner;

public class VerifyTripUi {
    private final VerifyTripController c = new VerifyTripController();
    private String depStation;
    private String arriStation;
    private int[] stationTypes;
    private String[] stations;
    private boolean isTwoStations = false;
    private int typeOfTrain = -1;
    private Scanner sc = new Scanner(System.in);;

    public static void main(String[] args) {
        VerifyTripUi ui = new VerifyTripUi();
        ui.run();
    }

    private boolean isValidInput(String input, int max, int min) {
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                System.out.printf("Bad input, try again: ");
                return false;
            }
        }
        int value = Integer.parseInt(input);
        if (value >= min && value <= max) {
            return true;
        }
        System.out.printf("Bad input, should be betweeen " + min + " and " + max + " , try again: ");
        return false;
    }

    private int getWhichScenario(){
        int whichScenario = 0;
        String input;

        System.out.printf("Enter scenario that you want to be used: ");
        do {
            input = sc.nextLine();
        } while (input.isEmpty() || input.isBlank() || !isValidInput(input, 4, 1));
        return (Integer.parseInt(input));
    }

    private void getTrainType() {
        String input;

        System.out.printf("[1] - Steam\n[2] - Electric\n[3] - Diesel\nEnter train type: ");
        do {
            input = sc.nextLine();
        } while (!isValidInput(input, 3, 1));
        typeOfTrain = Integer.parseInt(input);
    }

    private void selectStations() throws IllegalArgumentException {
        String input;
        isTwoStations = true;
        try {
            for (int i = 0; i < stations.length; i++) {
                System.out.printf("[%d] - %s\n", i, stations[i]);
            }

            System.out.println("Select departure station:");
            do {
                input = sc.nextLine();
            } while (!isValidInput(input, stations.length, 0));
            depStation = stations[Integer.parseInt(input)];


            System.out.println("Select arrival station:");
            do {
                input = sc.nextLine();
            } while (!isValidInput(input, stations.length, 0));
            arriStation = stations[Integer.parseInt(input)];

            if (arriStation.equals(depStation)) {
                throw new IllegalArgumentException("Arrival and departure stations are the same");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWantedStationsTypes(int []typesInt) {
        //System.out.println("1 - Depot      2- Station      3- Terminal");
        char[] typesChar = new char[typesInt.length];

        for (int i = 0; i < typesInt.length; i++) {
            if (typesInt[i] == 1)
                typesChar[i] = 'D';
            if (typesInt[i] == 2)
                typesChar[i] = 'S';
            if (typesInt[i] == 3)
                typesChar[i] = 'T';
        }
        try{
            stations = c.getStationsOfType(typesChar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectStationTypes() throws IllegalArgumentException{
        String input;
        System.out.println("Input how many types of station do you want(should be between 1 and 3): ");
        do {
            input = sc.nextLine();
        } while (!isValidInput(input, 3, 1));

        int manyTypes = Integer.parseInt(input);


        System.out.println("Select station types:");
        System.out.println("1 - Depot      2- Station      3- Terminal");

        int []temp = new int[manyTypes];

        for (int i = 0; i < manyTypes; i++) {
            do {
                input = sc.nextLine();
            } while (!isValidInput(input, 3, 1));

            temp[i] = Integer.parseInt(input);
        }

        for (int i = 0; i < manyTypes; i++) {
            for (int j = i + 1; j < manyTypes; j++) {
                if (temp[i] == temp[j]) {
                    throw new IllegalArgumentException(String.format("Repeated station type!"));
                }
            }
        }
        stationTypes = temp;
    }

    private void getStationsOrStationType(){
        String input;
        System.out.println("Would you like to:");
        System.out.println(" 1 - Select two specific stations to see if it possible to travel between them");
        System.out.println(" 2 - Select the station type to see if it possible to travel between all them");
        do {
            input = sc.nextLine();
        } while (input.isBlank() || !isValidInput(input, 4, 1));
        int num = Integer.parseInt(input);

        try{
            if (num == 1)
                selectStations();
            else
                selectStationTypes();
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        int whichScenario = getWhichScenario();

        try {
            c.getLineFile(whichScenario);
            c.getStationFile(whichScenario);

            getTrainType();

            stations = c.getStations();

            getStationsOrStationType();

            System.out.println(" Output of (1) - means trip is possible (0) - mean trip is impossible");
            if (isTwoStations)
                System.out.println("is possible to runTwoStations = " + c.runTwoStations(depStation, arriStation, stations, typeOfTrain));
            else
                System.out.println("is possible to runStationTypes = " + c.runStationTypes(stationTypes, stations, typeOfTrain));
            MultiGraph graph = GraphBuilder.buildGraph(stations, ReadCsvs.readCSVTo2DArray(c.getLineFile(whichScenario)));
            Viewer viewer = graph.display();
            viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
            } catch (IOException e) {
          e.printStackTrace();
        }
    }
}
