package controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;

/**
 * [Brief one-sentence desciption of what this class does.]
 *
 * @author Sebastien
 * @version 0.1.0
 * @since 22/04/2026
 */
public class SignupController {
    public static Scene signUpBuild(Stage stage) {
        Label username_label = new Label("Username: ");
        Label password_label = new Label("Password: ");
        Label class_label = new Label("Class: ");

        TextField username_input = new TextField();
        TextField class_input = new TextField();
        PasswordField password_input = new PasswordField();

        username_input.setPromptText("Username");
        class_input.setPromptText("Class");
        password_input.setPromptText("Password");

        Button sign_up = new Button("SIGN UP");
        Button login = new Button("LOGIN");

        sign_up.setOnAction(e -> {
            String username = username_input.getText();
            String password = password_input.getText();


        });

        login.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneType.LOGIN));

        VBox root1 = new VBox(12,
                username_label,
                username_input,
                class_label,
                class_input,
                password_label,
                password_input,
                login,
                sign_up
        );
        root1.setPadding(new Insets(30));
        root1.setAlignment(Pos.CENTER);

        Scene signup_scene = new Scene(root1);
        return signup_scene;
    }
}
