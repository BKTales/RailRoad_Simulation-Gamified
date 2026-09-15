package pt.ipp.isep.dei.us011;

import javafx.application.Platform;
import pt.ipp.isep.dei.application.Authentication.App;
import pt.ipp.isep.dei.utils.*;
import pt.ipp.isep.dei.application.UserMenus.PlayerMenu;
import java.util.List;

/**
 * UI class responsible for displaying all purchased trains to the player.
 * <p>
 * It retrieves the list of purchased trains from the {@link ListAllTrainsController},
 * and shows them using a dynamic JavaFX list overlay with blurred background.
 * If there are no trains, a message is displayed instead.
 * </p>
 */
public class ListAllTrainsUI {

    private final ListAllTrainsController controller = new ListAllTrainsController();

    /**
     * Runs the user interface logic to display the list of purchased trains.
     * <p>
     * If no trains are available, a message dialog is shown to the user.
     * Otherwise, a dynamic list overlay is displayed with the train details.
     * Once the user closes the list view, the main player menu is reopened.
     * </p>
     */

    public void run() {
        List<String> trains = controller.getListOfAllTrains();

        if (trains == null || trains.isEmpty()) {
            try {
                pt.ipp.isep.dei.utils.Utils.displayReturnPlayer("There are no trains purchased yet.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DisplayDynamicList2.displayList(trains, new DisplayListControllerInterface(){
                @Override
                public void initialize(List<String> list) {
                }

                @Override
                public void onItemSelected(int index) {
                    Platform.runLater(() -> {
                        try {
                            PlayerMenu playerMenu = new PlayerMenu();
                            playerMenu.start(App.getPrimaryStage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
    }
}

