package pt.ipp.isep.dei.USoutOfProgram.us013;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCsvs {


    //================= Utils Methods ===============//

    /**
     * Helper method to check if the station name starts with one of the allowed types.
     */
    private static boolean isStationOfType(String stationName, char[] types) {
        if (stationName == null || stationName.isEmpty()) return false;
        char stationType = stationName.charAt(0);
        for (char type : types) {
            if (stationType == type)
                return (true);
        }
        return (false);
    }

    //================= Reading Methods ===============//

    public static String[][] readCSVTo2DArray(File file) {
        List<String[]> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                String[] row = new String[4];
                row[0] = values[0];
                row[1] = values[1];
                row[2] = values[2];
                row[3] = values[3];
                lines.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String [][] a = lines.toArray(new String[lines.size()][3]);
        return (a);
    }

    public static String[][] readCSVTo2DArrayForTypes(File file, char[] types) {
        List<String[]> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length >= 3 && isStationOfType(values[0], types)) {
                    String[] row = new String[3];
                    row[0] = values[0];
                    row[1] = values[1];
                    row[2] = values[2];
                    lines.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return lines.toArray(new String[0][3]);
    }

    public static String[] getAllStations(File stationFile) throws IOException {
        String[] stations = null;

        try (BufferedReader br = new BufferedReader(new FileReader(stationFile))) {
            String line = br.readLine();
            if (line != null) {
                stations = line.split(";");
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return (stations);
    }

    public static String[] getAllStationsOfTypes(File stationFile, char []types) throws IOException {
        String[] stations = getAllStations(stationFile);
        ArrayList<String> wantedStations = new ArrayList<>();

        for (int i = 0; i < stations.length; i++) {
            System.out.println(stations[i].charAt(0));
            if (isStationOfType(stations[i], types)) {
                wantedStations.add(stations[i]);
            }
        }
        return (wantedStations.toArray(new String[wantedStations.size()]));
    }
}
