package pt.ipp.isep.dei.USoutOfProgram.us013;

import java.io.File;
import java.io.IOException;

public class VerifyTripController {
    private File stationFile;
    private File lineFile;

    public VerifyTripController() {
    }


    public String[] getStationsOfType(char[] typesChar) throws IOException {
        return (ReadCsvs.getAllStationsOfTypes(stationFile, typesChar));
    }

    public File getStationFile(int whichScenario) throws IOException{
        stationFile = new File("src/csvs/scenario" + whichScenario + "_stations.csv");
        return (stationFile);
    }
    public File getLineFile(int whichScenario) throws IOException{
        lineFile = new File("src/csvs/scenario" + whichScenario + "_lines.csv");
        return (lineFile);
    }

    public String[] getStations() throws IllegalArgumentException, IOException {
        return (ReadCsvs.getAllStations(stationFile));
    }


    public int runTwoStations(String station1, String station2, String[] stations, int trainType) throws IOException{
        VerifyTrip vrT = new VerifyTrip();
        return (vrT.verifyTripTwoStations(lineFile, station1, station2,  stations, trainType));
    }

    public int runStationTypes(int[] stationTypes, String[] stations, int trainType) throws IOException {
        VerifyTrip vrT = new VerifyTrip();
        return (vrT.verifyTripStationType(lineFile, stationTypes, stations, trainType));
    }

    //--------------------------------------Métodos-US29--------------------------------------------------

    public File getStationFileUS29(int whichScenario) throws IOException {
        stationFile = new File("src/main/dataUS29/Scenario_" + whichScenario + "_stations.csv");
        if(!stationFile.exists()) {
            throw new IOException("Station file not found: " + stationFile.getAbsolutePath());
        }
        return stationFile;
    }

    public File getLineFileUS29(int whichScenario) throws IOException {
        lineFile = new File("src/main/dataUS29/Scenario_" + whichScenario + "_lines.csv");
        if(!lineFile.exists()) {
            throw new IOException("Line file not found: " + lineFile.getAbsolutePath());
        }
        return lineFile;
    }

    public int runStationTypesUS29(File stationFile, File lineFile, int[] stationTypes, String[] stations, int trainType) throws IOException {
        VerifyTrip vrT = new VerifyTrip();
        return vrT.verifyTripStationType(lineFile, stationTypes, stations, trainType);
    }
}
