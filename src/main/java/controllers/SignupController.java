package controllers;

import static tools.Helpers.*;

import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class SignUpController {
  public static Scene signUpBuild(Stage stage) {
    BorderPane base = new BorderPane();
    Label message = new Label("Sign Up:");

    TextField username = new TextField();
    username.setPromptText("Username (5+ characters)...");
    username.setMaxWidth(500);
    Label usernameLbl = new Label("Username: ");
    HBox usernameHBox = new HBox(usernameLbl, username);
    usernameHBox.setAlignment(Pos.CENTER);

    Label usernameErrorLabel = new Label("Invalid username! Must be of length 5 or greater.");
    usernameErrorLabel.setVisible(false);
    username.setOnMouseClicked(e -> usernameErrorLabel.setVisible(false));

    PasswordField password = new PasswordField();
    password.setPromptText("Password (5+ characters)...");
    password.setMaxWidth(500);
    Label passwordLbl = new Label("Password: ");
    HBox passwordHBox = new HBox(passwordLbl, password);
    passwordHBox.setAlignment(Pos.CENTER);

    Label passwordErrorLabel = new Label();
    passwordErrorLabel.setVisible(false);
    password.setOnMouseClicked(e -> passwordErrorLabel.setVisible(false));

    PasswordField confirmPassword = new PasswordField();
    confirmPassword.setPromptText("Confirm Password...");
    confirmPassword.setMaxWidth(500);
    confirmPassword.setOnMouseClicked(e -> passwordErrorLabel.setVisible(false));
    Label confPasswordLbl = new Label("Retype Password: ");
    HBox confPasswordHBox = new HBox(confPasswordLbl, confirmPassword);
    confPasswordHBox.setAlignment(Pos.CENTER);

    Label revealedPassword = new Label();
    revealedPassword.setVisible(false);

    Button showPasswordBtn = new Button("Show Password");
    showPasswordBtn.setOnAction(e -> {
      if(showPasswordBtn.getText().equals("Hide Password")){
        revealedPassword.setVisible(false);
        revealedPassword.setText("");
        showPasswordBtn.setText("Show Password");
      }else{
        revealedPassword.setText("password: " + password.getText());
        revealedPassword.setVisible(true);
        showPasswordBtn.setText("Hide Password");
      }
    });
    Button signUpBtn = new Button("Register");
    HBox btnsBox = new HBox(showPasswordBtn, signUpBtn);
    btnsBox.setAlignment(Pos.CENTER);
    btnsBox.setSpacing(10);
    btnsBox.setPadding(new Insets(10));

    ObservableList<String> roleOptions = FXCollections.observableArrayList(
        "Student",
        "Teacher",
        "Admin"
    );
    ComboBox<String> comboBoxRoles = new ComboBox<>(roleOptions);
    comboBoxRoles.setMaxWidth(500);
    HBox roleSelectHBox = new HBox(new Label("Role: "), comboBoxRoles);
    roleSelectHBox.setAlignment(Pos.CENTER);

    Button backToLoginBtn = new Button("Return to Login");
    backToLoginBtn.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneType.LOGIN));

    VBox vbox01 = new VBox();
    vbox01.setAlignment(Pos.CENTER);
    vbox01.getChildren().addAll(message, usernameHBox, usernameErrorLabel,
        passwordHBox, passwordErrorLabel, confPasswordHBox,
        revealedPassword, roleSelectHBox, btnsBox, backToLoginBtn);

    base.setCenter(vbox01);

    signUpBtn.setOnAction(e -> {
      String grabUsername = username.getText();
      String grabPassword = password.getText();
      String confPassword = confirmPassword.getText();
      String roleSelection = comboBoxRoles.getValue();

      if(grabUsername.length() < 5){
        usernameErrorLabel.setVisible(true);
        return;
      }

      if(grabPassword.length() < 5){
        passwordErrorLabel.setVisible(true);
        passwordErrorLabel.setText("Invalid password! Must be of length 5 or greater.");
        return;
      }

      if(!grabPassword.equals(confPassword)){
        passwordErrorLabel.setVisible(true);
        passwordErrorLabel.setText("Password confirmation does not match!");
        return;
      }

      if(roleSelection == null || roleSelection.isEmpty()){
        passwordErrorLabel.setVisible(true);
        passwordErrorLabel.setText("Select a role!");
        return;
      }
      passwordErrorLabel.setText(grabUsername + " " + grabPassword + " " + confPassword + " " + roleSelection);
      passwordErrorLabel.setVisible(true);

      DatabaseManager.getInstance().insertUser(grabUsername, grabPassword, roleSelection.toUpperCase());
    });

    return new Scene(base, getScreenSize().get("w"), getScreenSize().get("h"));
    // ── TEST CODE ──────────────────────────────────────────────
    // ── ./gradlew run ──────────────────────────────────────────



    //TODO
//    return null;
  }

}
