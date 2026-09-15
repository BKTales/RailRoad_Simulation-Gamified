package pt.ipp.isep.dei.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class ReadStringInputController {
    private String message;
    private InputReceiver receiver;

    public TextField input_field;
    public Label inputAsked;
    private String suggestedText = null;


    public ReadStringInputController(InputReceiver receiver) {
        this.receiver = receiver;
    }
    
    public void initialize(String message) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/Eastwood.ttf"), 30);

        this.message = message;
        inputAsked.setText(message);
        if (suggestedText != null && !suggestedText.isEmpty()) {
            input_field.setText(suggestedText);
        }
    }

    public void confirmationButton(ActionEvent actionEvent) {
        String input = input_field.getText().trim();

        if (input.isEmpty())
            inputAsked.setText("Input is empty!\n" + message);
        else
            receiver.onInputReceived(input);
    }

    public void setSuggestedText(String suggestedText) {
        this.suggestedText = suggestedText;
    }

}
