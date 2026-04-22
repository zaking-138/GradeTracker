package tools;

import static tools.Helpers.*;

import java.util.Arrays;
import java.util.HashMap;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Zachary King
 * <br>
 * created: 04/18/2026
 * @since 0.1.0
 */
public class SceneFactory {


  public static Scene create(SceneType type, Stage stage){
    return switch (type){
      case LOGIN -> loginBuild(stage);

      case ADMIN_DASH -> adminDashBuild(stage);
      case ADMIN_USERLIST -> adminUserlistBuild(stage);

      case PROF_DASH -> profDashBuild(stage);
      case PROF_USERLIST -> profUserlistBuild(stage);
      case PROF_GRDBK -> profGrdBkBuild(stage);

      case STDNT_DASH -> stdntDashBuild(stage);
      case STDNT_GRDBK -> stdntGrdBkBuild(stage);
    };
  }

  // ── Scene builders ──────────────────────────────────────────────

  private static Scene loginBuild(Stage stage) {

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

  private static Scene adminDashBuild(Stage stage) {
    BorderPane base = new BorderPane();

    Button logoutBtn = new Button("Logout");

    Label welcomeLbl = new Label("Welcome, {admin name}!");

    Label userListBtnLbl = new Label("Open full userlist: ");
    Button userListBtn = new Button("Userlist");
    HBox hbox01 = new HBox();
    hbox01.getChildren().addAll(userListBtnLbl, userListBtn);
    hbox01.setAlignment(Pos.CENTER);

    VBox vbox01 = new VBox();
    vbox01.getChildren().addAll(welcomeLbl, hbox01);
    vbox01.setAlignment(Pos.CENTER);

    base.setTop(logoutBtn);
    base.setCenter(vbox01);

    return new Scene(base, getScreenSize().get("w"), getScreenSize().get("h"));
  }

  private static Scene adminUserlistBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene profDashBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene profUserlistBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene profGrdBkBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene stdntDashBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene stdntGrdBkBuild(Stage stage) {
    //TODO
    return null;
  }

}
