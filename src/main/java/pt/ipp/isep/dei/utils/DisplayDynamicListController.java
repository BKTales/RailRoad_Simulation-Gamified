package pt.ipp.isep.dei.utils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class DisplayDynamicListController {

    @FXML
    private Button btn;

    @FXML
    private Button confirmButton;

    @FXML
    private Button selectButton;

    @FXML
    private ListView<String> listView;

    @FXML
    private Button closeButton;

    @FXML
    private Label messageLabel;

    private InputReceiver receiver;

    private DisplayListControllerInterface callback;

    public DisplayDynamicListController() {
    }


    public void setReceiver(InputReceiver receiver) {
        this.receiver = receiver;
    }

    public void initializeWithMessage(String message) {
        messageLabel.setText(message);
    }

    public void setItems(List<String> items) {
        listView.getItems().setAll(items);
    }

    public void initialize(List<String> items) {
        listView.getItems().setAll(items);
    }

    @FXML
    private void onCloseButtonClicked() {
        closeButton.getScene().getWindow().hide();
    }
    public void setCallback(DisplayListControllerInterface callback) {
        this.callback = callback;
    }


    @FXML
    private void onSelectButtonClicked() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && callback != null) {
            callback.onItemSelected(selectedIndex);
            //closeButton.getScene().getWindow().hide();
        } else {
            messageLabel.setText("Please select an item before confirming.");
        }
    }

}
