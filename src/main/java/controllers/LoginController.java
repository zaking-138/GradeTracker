package controllers;

import static tools.Helpers.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class LoginController {
  public static Scene loginBuild(Stage stage) {

    BorderPane base = new BorderPane();
    Label message = new Label("Login:");

    TextField username = new TextField();
    username.setPromptText("Username (5+ characters)...");
    username.setMaxWidth(500);
    Label usernameLbl = new Label("Username: ");
    HBox usernameHBox = new HBox(usernameLbl, username);
    usernameHBox.setAlignment(Pos.CENTER);

    Label usernameErrorLabel = new Label("Invalid username! Must be of length 5 or greater.");
    usernameErrorLabel.setVisible(false);

    PasswordField password = new PasswordField();
    password.setPromptText("Password (5+ characters)...");
    password.setMaxWidth(500);
    Label passwordLbl = new Label("Password: ");
    HBox passwordHBox = new HBox(passwordLbl, password);
    passwordHBox.setAlignment(Pos.CENTER);

    Label passwordErrorLabel = new Label();
    passwordErrorLabel.setVisible(false);

    Label revealedPassword = new Label();
    revealedPassword.setVisible(false);

    Button showPasswordBtn = new Button("Show Password");
    showPasswordBtn.setOnAction(e -> {
      if(showPasswordBtn.getText().equals("Hide Password")){
        revealedPassword.setVisible(false);
        revealedPassword.setText("");
        showPasswordBtn.setText("Show Password");
      }else if (showPasswordBtn.getText().equals("Show Password") && !password.getText().isEmpty()){
        revealedPassword.setText("password: " + password.getText());
        revealedPassword.setVisible(true);
        showPasswordBtn.setText("Hide Password");
      }
    });
    Button loginBtn = new Button("Login");
    Button registerBtn = new Button("Register");
    registerBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.SIGN_UP);
    });

    HBox btnsBox = new HBox(showPasswordBtn, loginBtn);
    btnsBox.setAlignment(Pos.CENTER);
    btnsBox.setSpacing(5);
    btnsBox.setPadding(new Insets(5));

    VBox vbox01 = new VBox();
    vbox01.setAlignment(Pos.CENTER);
    vbox01.getChildren().addAll(message, usernameHBox, usernameErrorLabel,
        passwordHBox, passwordErrorLabel, revealedPassword, btnsBox, registerBtn);

    base.setCenter(vbox01);

    loginBtn.setOnAction(e -> {

      SceneManager.getInstance().navigateTo(SceneType.ADMIN_DASH);
    });

    return new Scene(base, getScreenSize().get("w"), getScreenSize().get("h"));
    // ── TEST CODE ──────────────────────────────────────────────
    // ── ./gradlew run ──────────────────────────────────────────



    //TODO
//    return null;
  }
}
