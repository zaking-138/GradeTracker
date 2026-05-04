package controllers;

import static tools.Helpers.*;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import tools.SceneFactory;
import tools.SceneManager;
import tools.SceneType;
import database.DatabaseManager;

/**
 * @author Sebastien Wu
 * <br>
 * created:
 * @since 0.1.0
 */
public class LoginController {
  public static Scene loginBuild(Stage stage) {
    DatabaseManager db = DatabaseManager.getInstance();
    BorderPane base = new BorderPane();
    Label header = new Label("Sign In:");
    header.setPadding(new Insets(8));

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
    username_input.setId("usernameField");

    PasswordField password_input = new PasswordField();
    password_input.setPromptText("Password");
    password_input.setMaxWidth(500);
    HBox passwordHBox = new HBox(8, password_label, password_input);
    passwordHBox.setAlignment(Pos.CENTER);
    password_input.setId("passwordField");

    Button admin_test = new Button("ADMIN LOGIN");
    Button sign_up = new Button("SIGN UP");
    sign_up.setId("signUpButton");
    Button login = new Button("LOGIN");
    login.setId("loginButton");
    HBox buttonsBox = new HBox(8, sign_up, login);
    buttonsBox.setAlignment(Pos.CENTER);

    Button showPasswordBtn = new Button("Show Password");
    showPasswordBtn.setId("showPasswordButton");

    Label errorLbl = new Label();
    errorLbl.setStyle("-fx-text-fill: red;");
    setVisible(errorLbl, false);

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

    login.setOnAction(e -> {
      String username = username_input.getText();
      String password = password_input.getText();

      errorLbl.setText("");
      setVisible(errorLbl, false);

      if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
        errorLbl.setText("Please fill in all fields.");
        setVisible(errorLbl, true);
        return;
      }

      if (db.authenticateUser(username, password) ) {

        String role = db.getUserRole(username);
        System.out.println((String) role);
        switch (role) {
          case "ADMIN" -> SceneManager.getInstance().navigateTo(SceneType.ADMIN_DASH);
          case "STUDENT" -> SceneManager.getInstance().navigateTo(SceneType.STDNT_DASH);
          case "TEACHER" -> SceneManager.getInstance().navigateTo(SceneType.PROF_DASH);
          default -> SceneManager.getInstance().navigateTo(SceneType.LOGIN);
        }

      } else {
        errorLbl.setText("Invalid username or password.");
        setVisible(errorLbl, true);
      }
    });

    sign_up.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneType.SIGNUP, true));

    admin_test.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneType.ADMIN_DASH));


    VBox root1 = new VBox(12,
        header,
        usernameHBox,
        usernameErrorLabel,
        passwordHBox,
        errorLbl,
        showPasswordBtn,
        passwordErrorLabel,
        revealedPassword,
        buttonsBox,
        admin_test
    );
    root1.setPadding(new Insets(30));
    root1.setAlignment(Pos.CENTER);
    base.setCenter(root1);

    ArrayList<Node> interactableNodes = new ArrayList<Node>() {
      {
        add(username_input);
        add(password_input);
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

    return getScene(base);
  }
}