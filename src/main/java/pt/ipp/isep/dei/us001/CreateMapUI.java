package pt.ipp.isep.dei.us001;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.utils.ReadStringInput;
import pt.ipp.isep.dei.utils.Utils;

import java.io.IOException;

public class CreateMapUI {
    private final CreateMapController controller = new CreateMapController();

    private String returnedFromCreation;
    private String saveAnswer;
    private String mapName;
    private int scale;
    private int height;
    private int length;

    // -------------------- public methods --------------------

    public void run() {
        readName();
    }

    /**
     * Asks and read the name to be given to the map
     */
    private void readName() {
        App.setMessage("Enter the name of the map: ");

        ReadStringInput.requestUserInputString(userInput -> {
            mapName = userInput;
            if (isValidCharacters())
                readHeight();
            else {
                try {
                    Utils.displayReturnEditor("The map name contains invalid characters!\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * Asks and read the height to be given to the map
     */
    private void readHeight() {
        App.setMessage("Enter the height of the map: ");

        ReadStringInput.requestUserInputString(userInput -> {
            try {
                height = Integer.parseInt(userInput);
                if (!isValidInt(height))
                    Utils.displayReturnEditor("Height must be a positive integer.");

                readLength();
            } catch (NumberFormatException | IOException e) {

                try {
                    Utils.displayReturnEditor("Invalid number.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * Asks and read the length to be given to the map
     */
    private void readLength() {
        App.setMessage("Enter the length of the map: ");

        ReadStringInput.requestUserInputString(userInput -> {
            try {
                length = Integer.parseInt(userInput);
                if (!isValidInt(length))
                    Utils.displayReturnEditor("Length must be a positive integer.");
                readScale();
            } catch (NumberFormatException | IOException e) {

                try {
                    Utils.displayReturnEditor("Invalid number.");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    /**
     * Asks and read the height to be given to the map
     */
    private void readScale() {
        App.setMessage("Enter the scale of the map (km per block): ");

        ReadStringInput.requestUserInputString(userInput -> {
            try {
                scale = Integer.parseInt(userInput);
                if (!isValidInt(scale))
                    Utils.displayReturnEditor("Scale must be a positive integer.");

                validateAndSubmit();  // final step
            } catch (NumberFormatException | IOException e) {

                try {
                    Utils.displayReturnEditor("Invalid number.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * Validate all the user's input and create the wanted map
     */
    private void validateAndSubmit()    {
        returnedFromCreation = (controller.createMap(mapName, height, length, scale));

        if (returnedFromCreation == null){
            try {
                Utils.displayReturnEditor("Could not create the map. Bad arguments were entered!");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else {
            App.setMessage("Do you want to save this map? [Y/N] ");
            ReadStringInput.requestUserInputString(userInput -> {
                try {
                    saveAnswer = userInput.trim().toUpperCase();
                    returnedFromCreation += controller.saveMap(saveAnswer, mapName);
                    Utils.displayReturnEditor(returnedFromCreation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

      // -------------------- private methods --------------------

    /**
     * Check if the number given is a positive Integer
     * @return (true - if it is) (false - if it is not)
     */
    private boolean isValidInt(int value){
        if (value < 1)
            return (false);
        return (true);
    }

    /**
     * Check if the given name for the map has only the allowed characters
     * @return (true - if it is) (false - if it is not)
     */
    private boolean isValidCharacters(){
        if (this.mapName == null || this.mapName.isEmpty() || mapName.length() > 255)
            return (false);
        for (int i = 0; i < mapName.length(); i++) {
            if (!((mapName.charAt(i) >= 'A' && mapName.charAt(i) <= 'Z')
                    ||( mapName.charAt(i) >= 'a' && mapName.charAt(i) <= 'z')
                    || mapName.charAt(i) == ',' || mapName.charAt(i) <= ' '
                    || mapName.charAt(i) == '.' || mapName.charAt(i) == '-'
                    || mapName.charAt(i) == '_' || mapName.charAt(i) == 39
                    || mapName.charAt(i) >= '0' || mapName.charAt(i) <= '9'))
            {
                return (false);
            }
        }
        return (true);
    }
}
