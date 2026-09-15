package pt.ipp.isep.dei.USoutOfProgram.us029;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class US29UI {

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showOddVertices(String station, int degree) {
        System.out.println(station + " : " + degree);
    }

    public void writeResult(BufferedWriter writer, int inputSize, long timeUS13, long timeUS14) throws IOException {
        writer.write(inputSize + "," + timeUS13 + "," + timeUS14);
        writer.newLine();
    }
}