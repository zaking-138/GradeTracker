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
    // ── TEST CODE ──────────────────────────────────────────────
    // ── DELETE IF NEEDED ───────────────────────────────────────
//    char[] mes1 = parseGrades("ABDC", "ABDC".length());
//    String stringMes1 = getGradeString(mes1);
//    editGrade(mes1, 2, 'F');
//    String stringMes2 = getGradeString(mes1);
//
//    String[] classes1 = parseClasses("M12N34L56", "M12N34L56".length()/3);
//    String stringClasses1 = getClassString(classes1);
//    editClass(classes1, 1, "J69");
//    String stringClasses2 = getClassString(classes1);

    BorderPane base = new BorderPane();
    Label message = new Label("Sign In:");

    TextField username = new TextField();
    username.setPromptText("Username...");
    username.setMaxWidth(500);

    PasswordField password = new PasswordField();
    password.setPromptText("Password...");
    password.setMaxWidth(500);

    Button showPasswordBtn = new Button("Hide");
    Button loginBtn = new Button("Login");
//    message.setFont(new Font(20));
//    message.setTextAlignment(TextAlignment.CENTER);
//    move.setOnAction(e -> {
//      SceneManager.getInstance().navigateTo(SceneType.ADMIN_USERLIST);
//    });
    HBox btnsBox = new HBox(showPasswordBtn, loginBtn);
    btnsBox.setAlignment(Pos.CENTER);

    VBox vbox01 = new VBox();
    vbox01.setAlignment(Pos.CENTER);
    vbox01.getChildren().addAll(message, username, password, btnsBox);

//    Label blank00 = new Label();
//    TextField blank01 = new TextField();
//    Button blank03 = new Button();
//    VBox vbox00 = new VBox();
//    vbox00.setAlignment(Pos.CENTER_LEFT);
//    vbox00.getChildren().addAll(blank00, blank01, showPasswordBtn, blank03);

//    HBox hbox00 = new HBox(vbox01, vbox00);

    base.setCenter(vbox01);

    return new Scene(base, getScreenSize().get("w"), getScreenSize().get("h"));
    // ── TEST CODE ──────────────────────────────────────────────
    // ── ./gradlew run ──────────────────────────────────────────



    //TODO
//    return null;
  }
}
