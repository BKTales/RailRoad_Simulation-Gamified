package pt.ipp.isep.dei.us009;


import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.domain.InsuficientBudget;
import pt.ipp.isep.dei.domain.simulation.TimeCounter;
import pt.ipp.isep.dei.domain.train.LocomotiveModel;
import pt.ipp.isep.dei.domain.train.Train;
import pt.ipp.isep.dei.utils.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UI class responsible for presenting and handling the user interface for purchasing locomotives
 * during a simulation. It displays available locomotives and allows the user to select and buy one,
 * if budget and criteria are met.
 *
 * <p>This class interacts with {@link BuyLocomotiveController} to manage the purchase logic,
 * and uses various utility classes to handle user interface display and messaging.
 */
public class BuyLocomotiveUI implements Serializable {
    private static final long serialVersionUID = 1L;
    private final BuyLocomotiveController controller;

    /**
     * Constructs the UI component for purchasing locomotives,
     * initializing its controller with the current {@link TimeCounter} instance.
     *
     * @param timeCounter the active simulation from which to retrieve contextual data
     */
    public BuyLocomotiveUI(TimeCounter timeCounter) {
        this.controller = new BuyLocomotiveController(timeCounter);
    }

    /**
     * Runs the UI flow for purchasing a locomotive. Displays a list of available locomotives.
     * If the user selects one, the controller attempts to process the purchase.
     * Displays appropriate messages based on the result.
     */
    public void run() {
        List<LocomotiveModel> locomotives = controller.getAvailableLocomotives();

        if (locomotives.isEmpty()) {
            App.setMessage("No locomotives available with current technologies and period.");
            DisplayMessage.displayMessage(receiver -> {
            });
            return;
        }

        List<String> formattedLocos = formatLocomotivesForDisplay(locomotives);

        DisplayDynamicList.displayList(formattedLocos, new DisplayListControllerInterface() {
            @Override
            public void initialize(List<String> items) {
            }
            @Override
            public void onItemSelected(int index) {

                Train createdTrain = controller.buyLocomotive(locomotives.get(index));

                if(createdTrain == null){
                    try{
                        Utils.displayReturnPlayer("Purchase failed: Not enough budget");
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                }else {
                    try {
                        Utils.displayReturnPlayer("LOCOMOTIVE PURCHASED SUCCESSFULLY!\n \n" + createdTrain);
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }

                }

            }
        },"====LIST OF AVAILABLE LOCOMOTIVES====");
    }

    /**
     * Formats the list of locomotive models into a readable string format for display.
     *
     * @param locomotives the list of available locomotives
     * @return a list of formatted strings representing each locomotive's attributes
     */
    private List<String> formatLocomotivesForDisplay(List<LocomotiveModel> locomotives) {
        return locomotives.stream()
                .map(m -> String.format("%s | Year: %d | Price: %.2f | Top speed: %.2f| Maintenance: %d| Fuel Cost: %d | MiType: %s",
                        m.getName(), m.getStartYear(), m.getAcquisitionPrice(), m.getTopSpeed(), m.getMaintenance(), m.getFuelCoast()
                        ,m.getType()))
                .collect(Collectors.toList());
    }


}