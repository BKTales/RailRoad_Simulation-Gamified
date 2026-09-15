package pt.ipp.isep.dei.us002;

import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.utils.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

public class CreateIndustryUi implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Scanner scanner;
    private final CreateIndustryController controller;
    private int x;
    private int y;

    /**
     * Constructs a new CreateIndustryUi, initializing the scanner and controller.
     */
    public CreateIndustryUi() {
        this.scanner = new Scanner(System.in);
        this.controller = new CreateIndustryController();
    }

    /**
     * Runs the UI for creating a new industry.
     * Asks the user for coordinates and whether to create a port or a regular industry.
     */
    public void run() {
        try {
            readX();
        } catch (Exception e) {
            Utils.displayErrorMessage(e.getMessage());
        }

    }

    /**
     * Read the x coordinate to put the industry in
     */
    private void readX(){
        App.setMessage("Enter the x coordinate: ");

        ReadStringInput.requestUserInputString(userInput -> {
            x = Integer.parseInt(userInput);
            readY();
        });
    }

    /**
     * Read the y coordinate to put the industry in
     */
    private void readY(){
        App.setMessage("Enter the y coordinate: ");

        ReadStringInput.requestUserInputString(userInput -> {
            y = Integer.parseInt(userInput);
            isToCreatePort();
        });
    }

    /**
     * Ask the user if he wants to create a port and receive the answer
     */
    private void isToCreatePort() {
        App.setMessage("Do you want to create a port? (y/n): ");

        ReadStringInput.requestUserInputString(userInput -> {
            String isToCreatePort = userInput;

            if (isToCreatePort.equals("y")){
                try {
                    if (controller.createPort(x,y) != null)
                            Utils.displayReturnMapEditor("Successfully created Industry!!");
                } catch (IllegalArgumentException | IOException e){
                    try {
                        Utils.displayReturnMapEditor("Could not createIndustry!" + e.getMessage());
                    } catch (IOException i){
                        i.printStackTrace();
                    }
                }
            }
            else
                readProduct();

        });
    }

    /**
     * Reads a valid product selection from the user by index.
     * Keeps prompting until a valid index is entered.
     *
     * @return the selected ProductType
     */
    private void readProduct() {
        App.setMessage("Enter product index: ");
        String listOfProducts = controller.getProductList();
        if (listOfProducts.isBlank()){
            try {
                Utils.displayReturnMapEditor("There are no available products! No industry can be created!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            App.setList(listOfProducts);
            ReadListInput.requestUserInputList(userInput -> {
                try {
                    if (controller.createIndustry(controller.getWhichProduct(userInput), x, y) != null)
                        Utils.displayReturnMapEditor("Successfully created Industry!!");
                } catch (IOException | IllegalArgumentException e) {
                    try {
                        Utils.displayReturnMapEditor("Could not create Industry!!" + e.getMessage());
                    } catch (IOException e1) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
