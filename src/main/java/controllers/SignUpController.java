package controllers;

import static tools.Helpers.getScene;
import static tools.Helpers.setVisible;

import database.DatabaseManager;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;

import org.mindrot.jbcrypt.BCrypt;

/**
 * [Brief one-sentence desciption of what this class does.]
 *
 * @author Sebastien
 * @version 0.1.0
 * @since 22/04/2026
 */
public class SignUpController {
    public static Parent doBuild(){
        BorderPane base = new BorderPane();
        Label header = new Label("Sign Up:");
        Label username_label = new Label("Username: ");
        Label usernameErrorLabel = new Label("Invalid username! Must be of length 5 or greater.");
        setVisible(usernameErrorLabel, false);

        Label password_label = new Label("Password: ");
        Label passwordErrorLabel = new Label();
        setVisible(passwordErrorLabel, false);

        Label revealedPassword = new Label();
        setVisible(revealedPassword, false);
        HBox passwordMessageBox = new HBox(8, passwordErrorLabel, revealedPassword);
        setVisible(passwordMessageBox, false);
        passwordMessageBox.setAlignment(Pos.CENTER);

        TextField username_input = new TextField();
        username_input.setPromptText("Username");
        username_input.setMaxWidth(500);
        HBox usernameHBox = new HBox(8, username_label, username_input);
        usernameHBox.setAlignment(Pos.CENTER);

        PasswordField password_input = new PasswordField();
        password_input.setPromptText("Password");
        password_input.setMaxWidth(500);
        HBox passwordHBox = new HBox(8, password_label, password_input);
        passwordHBox.setAlignment(Pos.CENTER);

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm Password...");
        confirmPassword.setMaxWidth(500);
        confirmPassword.setOnMouseClicked(e -> {
            setVisible(passwordErrorLabel, false);
            setVisible(passwordMessageBox, false);
        });
        Label confPasswordLbl = new Label("Retype Password: ");
        HBox confPasswordHBox = new HBox(confPasswordLbl, confirmPassword);
        confPasswordHBox.setAlignment(Pos.CENTER);

        ObservableList<String> roleOptions = FXCollections.observableArrayList(
            "Student",
            "Teacher",
            "Admin"
        );
        ComboBox<String> comboBoxRoles = new ComboBox<>(roleOptions);
        comboBoxRoles.setMaxWidth(500);
        HBox roleSelectHBox = new HBox(new Label("Role: "), comboBoxRoles);
        roleSelectHBox.setAlignment(Pos.CENTER);

        username_input.setPromptText("Username");
        password_input.setPromptText("Password");

        Button sign_up = new Button("SIGN UP");
        Button login = new Button("LOGIN");
        HBox buttonsBox = new HBox(8, sign_up, login);
        buttonsBox.setAlignment(Pos.CENTER);

        Button showPasswordBtn = new Button("Show Password");
        showPasswordBtn.setOnAction(e -> {
            if(showPasswordBtn.getText().equals("Hide Password")){
                setVisible(revealedPassword, false);
                setVisible(passwordMessageBox, false);
                revealedPassword.setText("");
                showPasswordBtn.setText("Show Password");
            }else if (showPasswordBtn.getText().equals("Show Password") && !password_input.getText().isEmpty()){
                revealedPassword.setText("password: " + password_input.getText());
                setVisible(revealedPassword, true);
                setVisible(passwordMessageBox, true);
                showPasswordBtn.setText("Hide Password");
            }
        });

        sign_up.setOnAction(e -> {
            String grabUsername = username_input.getText();
            String grabPassword = password_input.getText();
            String confPassword = confirmPassword.getText();
            String roleSelection = comboBoxRoles.getValue();

            if(grabUsername.length() < 5){
                setVisible(usernameErrorLabel, true);
                return;
            }

            if(grabPassword.length() < 5){
                passwordErrorLabel.setText("Invalid password! Must be of length 5 or greater.");
                setVisible(passwordErrorLabel, true);
                setVisible(passwordMessageBox, true);
                return;
            }

            if(!grabPassword.equals(confPassword)){
                passwordErrorLabel.setText("Password confirmation does not match!");
                setVisible(passwordErrorLabel, true);
                setVisible(passwordMessageBox, true);
                return;
            }

            if(roleSelection == null || roleSelection.isEmpty()){
                passwordErrorLabel.setText("Select a role!");
                setVisible(passwordErrorLabel, true);
                setVisible(passwordMessageBox, true);
                return;
            }


            passwordErrorLabel.setText("Added user: " + grabUsername + "\npw:" + grabPassword + "\t" + confPassword + "\nrole:" + roleSelection);
            setVisible(passwordErrorLabel, true);
            setVisible(passwordMessageBox, true);

            String hashedPassword = BCrypt.hashpw(grabPassword, BCrypt.gensalt());
            DatabaseManager.getInstance().insertUser(grabUsername, hashedPassword, roleSelection.toUpperCase());
        });

        login.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneType.LOGIN, true));

        VBox root1 = new VBox(12,
            header,
            usernameHBox,
            usernameErrorLabel,
            passwordHBox,
            confPasswordHBox,
            passwordErrorLabel,
            revealedPassword,
            roleSelectHBox,
            showPasswordBtn,
            buttonsBox
        );
        root1.setPadding(new Insets(30));
        root1.setAlignment(Pos.CENTER);

        ArrayList<Node> interactableNodes = new ArrayList<Node>() {
            {
                add(username_input);
                add(password_input);
                add(confirmPassword);
                add(comboBoxRoles);
            }
        };

        ArrayList<Node> hiddenLabels = new ArrayList<Node>() {
            {
                add(usernameErrorLabel);
                add(passwordErrorLabel);
            }
        };

        for(Node n : interactableNodes){
            n.setOnMouseClicked(e -> {
                for(Node m : hiddenLabels){
                    setVisible(m, false);
                }
            });
        }

        return root1;
    }

    public static Scene signUpBuild(Stage stage) {
        return getScene(doBuild());
    }

    public static Scene signUpPopup(Stage stage) {
        return getScene(doBuild(), 0.50);
    }
}