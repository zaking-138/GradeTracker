package controllers;

import static tools.Helpers.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
 * @author Sebastien Wu
 * <br>
 * created:
 * @since 0.1.0
 */
public class LoginController {
  public static Scene loginBuild(Stage stage) {
    Label header = new Label("Sign In:");
    header.setPadding(new Insets(8));

    Label username_label = new Label("Username: ");
    Label usernameErrorLabel = new Label("Invalid username! Must be of length 5 or greater.");
    usernameErrorLabel.setVisible(false);

    Label password_label = new Label("Password: ");
    Label passwordErrorLabel = new Label();
    passwordErrorLabel.setVisible(false);

    Label revealedPassword = new Label();
    revealedPassword.setVisible(false);
    HBox passwordMessageBox = new HBox(8, passwordErrorLabel, revealedPassword);
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

    Button sign_up = new Button("SIGN UP");
    Button login = new Button("LOGIN");
    HBox buttonsBox = new HBox(8, sign_up, login);
    buttonsBox.setAlignment(Pos.CENTER);

    Button showPasswordBtn = new Button("Show Password");

    showPasswordBtn.setOnAction(e -> {
      if(showPasswordBtn.getText().equals("Hide Password")){
        revealedPassword.setVisible(false);
        revealedPassword.setText("");
        showPasswordBtn.setText("Show Password");
      }else if (showPasswordBtn.getText().equals("Show Password") && !password_input.getText().isEmpty()){
        revealedPassword.setText("password: " + password_input.getText());
        revealedPassword.setVisible(true);
        showPasswordBtn.setText("Hide Password");
      }
    });

    login.setOnAction(e -> {
      String username = username_input.getText();
      String password = password_input.getText();

      SceneManager.getInstance().navigateTo(SceneType.ADMIN_DASH);
    });

    sign_up.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneType.SIGNUP));


    VBox root1 = new VBox(12,
        header,
        usernameHBox,
        usernameErrorLabel,
        passwordHBox,
        showPasswordBtn,
        passwordMessageBox,
        buttonsBox
    );
    root1.setPadding(new Insets(30));
    root1.setAlignment(Pos.CENTER);

    return getScene(root1);
  }
}