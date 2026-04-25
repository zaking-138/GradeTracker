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

    BorderPane base = new BorderPane();
    Text message = new Text("Sign In:\n[username]\n[password]\n\n[[login]]\n"
            + stringMes1 + "-->" + stringMes2 + "\n\n"
            + stringClasses1 + "-->" + stringClasses2);
    message.setFont(new Font(20));
    message.setTextAlignment(TextAlignment.CENTER);
    base.setCenter(message);
    //return new Scene(base, getScreenSize().get("w"), getScreenSize().get("h"));
    // ── TEST CODE ──────────────────────────────────────────────
    // ── ./gradlew run ──────────────────────────────────────────


    //TODO
    Label username_label = new Label("Username: ");
    Label password_label = new Label("Password: ");

    TextField username_input = new TextField();
    username_input.setPromptText("Username");
    PasswordField password_input = new PasswordField();
    password_input.setPromptText("Password");

    Button sign_up = new Button("SIGN UP");
    Button login = new Button("LOGIN");


    login.setOnAction(e -> {
      String username = username_input.getText();
      String password = password_input.getText();


    });

    sign_up.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneType.SIGNUP));


    VBox root1 = new VBox(12,
            username_label,
            username_input,
            password_label,
            password_input,
            login,
            sign_up
    );
    root1.setPadding(new Insets(30));
    root1.setAlignment(Pos.CENTER);

    Scene login_scene = new Scene(root1);
    return login_scene;
  }
}
