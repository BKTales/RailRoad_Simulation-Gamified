package pt.ipp.isep.dei.utils;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ReadListInputController {

    @FXML
    public ListView<String> listOfItems;

    @FXML
    public Label welcomeText;

    @FXML
    public Button buttonExit;

    private InputReceiver receiver;
    private String selectedItem;

    public ReadListInputController(InputReceiver receiver) {
        this.receiver = receiver;
    }

    @FXML
    public void initialize(String welcomeMessage, String items) {
        welcomeText.setText(welcomeMessage);
        listOfItems.getItems().addAll(items.split("\n"));

        listOfItems.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    selectedItem = newValue;
                }
        );
    }

    @FXML
    protected void confirmationBtn(ActionEvent event) {
        if (selectedItem == null) {
            welcomeText.setText("Please select an item.");
        } else if (receiver != null) {
            receiver.onInputReceived(selectedItem);
        }
    }
}
