package pt.ipp.isep.dei.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class DisplayMessageController {
    private String message;
    private InputReceiver receiver;

    public Label messageLabel;

    public DisplayMessageController(InputReceiver receiver) {
        this.receiver = receiver;
    }

    public void initialize(String message) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/Eastwood.ttf"), 30);

        this.message = message;
        messageLabel.setText(message);
    }

    public void confirmationButton(ActionEvent actionEvent) {
        try {
            receiver.onInputReceived(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
