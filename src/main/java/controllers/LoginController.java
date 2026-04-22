package controllers;

import static tools.Helpers.*;

import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    // ── TEST CODE ──────────────────────────────────────────────
    // ── DELETE IF NEEDED ───────────────────────────────────────
    char[] mes1 = parseGrades("ABDC", "ABDC".length());
    String stringMes1 = getGradeString(mes1);
    editGrade(mes1, 2, 'F');
    String stringMes2 = getGradeString(mes1);

    String[] classes1 = parseClasses("M12N34L56", "M12N34L56".length()/3);
    String stringClasses1 = getClassString(classes1);
    editClass(classes1, 1, "J69");
    String stringClasses2 = getClassString(classes1);

    BorderPane base = new BorderPane();
    Text message = new Text("Sign In:\n[username]\n[password]\n\n[[login]]\n"
        + stringMes1 + "-->" + stringMes2 + "\n\n"
        + stringClasses1 + "-->" + stringClasses2);
    message.setFont(new Font(20));
    message.setTextAlignment(TextAlignment.CENTER);

    Button move = new Button("admindash");
    move.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.ADMIN_DASH);
    });

    VBox vbox01 = new VBox();
    vbox01.getChildren().addAll(message, move);

    base.setCenter(vbox01);

    return new Scene(base, getScreenSize().get("w"), getScreenSize().get("h"));
    // ── TEST CODE ──────────────────────────────────────────────
    // ── ./gradlew run ──────────────────────────────────────────



    //TODO
//    return null;
  }
}
