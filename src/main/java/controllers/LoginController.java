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