package pt.ipp.isep.dei.USoutOfProgram.US027;

import pt.ipp.isep.dei.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ShortestWayUI {
    private final int  MIN_READING_VALUE = -1;
    private final ShortestWayController controller;
    private final Scanner sc;
    private int[] stationsIndexes;
    private String scenarioName;

    public static void main(String[] args) {
        ShortestWayUI ui = new ShortestWayUI();
        ui.run();
    }

    public ShortestWayUI() {
        controller = new ShortestWayController();
        sc = new Scanner(System.in);
    }

    private void run(){
        Utils.displayHeader("Shortest Way");

        askForScenarioName();
        try
        {
            System.out.println("1-Read stop from file\n2-Enter manually the stations");
            String input = sc.nextLine();
            controller.listStations(scenarioName);
            if (input.equals("1"))
                readFromFile();
            else if (input.equals("2"))
                readArrayOfStations();
            displayMessage(controller.getShortestWay(stationsIndexes));
        }
        catch (IOException e){
            displayMessage(e.getMessage());
        }
    }

    private void readFromFile() {
        System.out.println("Enter file name:");
        String input = sc.nextLine();
        stationsIndexes = controller.readStationsToGoThrough(input);
        System.out.println("After reading!");
        for(int i = 0; i < stationsIndexes.length; i++){
            System.out.println(i + ": " + stationsIndexes[i]);
        }
    }

    private void readArrayOfStations() throws IOException {
        String stationList = controller.listStations(scenarioName);
        int manyStations = stationList.split("\n").length;
        ArrayList<Integer> stationsArrayIndexes  = new ArrayList<>();
        int inputValue;

        do{
            displayMessage(stationList);
            displayMessage("Select a Station index(-1 to end read): ");
            inputValue = readInt(sc.nextLine(), manyStations, MIN_READING_VALUE);

            if (inputValue >= 0)
                stationsArrayIndexes.add(inputValue);
        } while (inputValue != -1);

        stationsIndexes = new int[stationsArrayIndexes.size()];
        for (int i = 0; i < stationsArrayIndexes.size(); i++){
            stationsIndexes[i] = stationsArrayIndexes.get(i);
        }
    }

    private void askForScenarioName(){
        do {
            displayMessage("Please enter a scenario name: ");
            scenarioName = sc.nextLine();
        }  while (scenarioName.isBlank());
    }

    // ===================== Utils Functions =====================//

    /**
     * Ensures that the input is an integer inside the given values
     * @param input  given input
     * @param max  max accepted int
     * @param min  min accepted int
     * @return (true if fits the range) (false if dont)
     */
    private int readInt(String input, int max, int min) {
        int value;

        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c) && c != '-') {
                displayMessage("Bad input, try again: ");
                return (-2);
            }
        }
        value = Integer.parseInt(input);
        if (value >= min && value <= max)
            return (value);
        displayMessage("Bad input, should be betweeen " + min + " and " + max + " , try again: ");
        return (-2);
    }

    private void displayMessage(String message){
        System.out.print(message);
    }
}
