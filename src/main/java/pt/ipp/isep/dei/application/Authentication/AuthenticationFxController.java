package pt.ipp.isep.dei.application.Authentication;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import pt.ipp.isep.dei.domain.user.UserRoleDTO;

import java.util.List;

public class AuthenticationFxController {

    public ImageView backgroundImage;
    public AnchorPane anchorPane;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    @FXML
    private VBox roleActionsContainer;

    private AuthenticationController controller = new AuthenticationController();

    private int loginAttemptsLeft = 3;

    @FXML
    private void initialize() {
        Font font = Font.loadFont(getClass().getResourceAsStream("/Fonts/Eastwood.ttf"), 30);
        loginButton.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both email and password.");
            return;
        }

        boolean success = controller.doLogin(email, password);

        if (success) {
            messageLabel.setText("Login successful!");
            loginAttemptsLeft = 3;
            UserRoleDTO currentRole = controller.getCurrentUserRole();
            if (currentRole == null) {
                messageLabel.setText("No role assigned to user. Logging out.");
                doLogout();
                return;
            }
            showRoleUI(currentRole);
        } else {
            loginAttemptsLeft--;
            if (loginAttemptsLeft > 0) {
                messageLabel.setText("Invalid email or password. Attempts left: " + loginAttemptsLeft);
            } else {
                messageLabel.setText("Too many failed attempts. Please wait 1 minute...");
                loginButton.setDisable(true);
                PauseTransition pause = new PauseTransition(Duration.minutes(1));
                pause.setOnFinished(event -> {
                    loginButton.setDisable(false);
                    loginAttemptsLeft = 3;
                    messageLabel.setText("You can try again now.");
                });
                pause.play();
            }
        }
    }

    private void showRoleUI(UserRoleDTO role) {
        roleActionsContainer.getChildren().clear();

        switch (role.getId()) {
            case "PLAYER":
                messageLabel.setText("Welcome, Player!");
                controller.getPlayerMenu();
                break;

            case "EDITOR":
                messageLabel.setText("Welcome, Editor!");
                controller.getEditorMenu();
                break;

            case "ADMIN":
                messageLabel.setText("Welcome, Admin!");
                askAddUser();
                break;

            default:
                messageLabel.setText("Unknown role: " + role.getDescription());
        }
    }

    private void askAddUser() {
        Label question = new Label("Do you want to add a new user?");
        Button yesBtn = new Button("Yes");
        Button noBtn = new Button("No");

        yesBtn.setOnAction(e -> addUserUI());
        noBtn.setOnAction(e -> {
            messageLabel.setText("No user will be added.");
            roleActionsContainer.getChildren().clear();
        });

        roleActionsContainer.getChildren().addAll(question, yesBtn, noBtn);
    }

    private void addUserUI() {
        roleActionsContainer.getChildren().clear();

        // Show UI elements for adding user
        TextField newEmail = new TextField();
        newEmail.setPromptText("Enter new user's email");

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Enter password (7 chars, 2 digits, 3 uppercase)");

        // Fetch roles from controller
        List<UserRoleDTO> roles = controller.getUserRoles();
        if (roles == null || roles.isEmpty()) {
            messageLabel.setText("No roles available.");
            return;
        }

        ComboBox<UserRoleDTO> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(roles);
        roleComboBox.setPromptText("Select user role");

        Button submitBtn = new Button("Create User");
        submitBtn.setOnAction(e -> {try {
            UserRoleDTO selectedRole = roleComboBox.getValue();
            String email = newEmail.getText().trim();
            String password = newPassword.getText().trim();

            if (selectedRole == null || email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill all fields and select a role.");
                return;
            }

            controller.addUser(email, password,
                    selectedRole.getId(), selectedRole.getDescription());

            messageLabel.setText("User created successfully.");
            roleActionsContainer.getChildren().clear();

        } catch (Exception ex) {
            messageLabel.setText("Error creating user: " + ex.getMessage());
        }
        });

        roleActionsContainer.getChildren().addAll(newEmail, newPassword, roleComboBox, submitBtn);
    }

    private void doLogout() {
        controller.doLogout();
        messageLabel.setText("Logged out.");
        loginButton.setDisable(false);
        emailField.clear();
        passwordField.clear();
        roleActionsContainer.getChildren().clear();
    }
}
