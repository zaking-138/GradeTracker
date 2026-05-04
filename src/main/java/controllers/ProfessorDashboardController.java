package controllers;

import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import tools.SceneManager;
import tools.SceneType;

import static tools.Helpers.*;

/**
 * @author Adam Vartan
 * <br>
 * created:
 * @since 0.1.0
 */
public class ProfessorDashboardController {

  private static int selectedUserId;
  private static String selectedRow;

  public static void setSelectedRow(String selectedRow) {
    ProfessorDashboardController.selectedRow = selectedRow;
  }

  public static void setSelectedUserId(int selectedUserId) { ProfessorDashboardController.selectedUserId = selectedUserId;
  }

  public static Scene profDashBuild(Stage stage) {
    BorderPane base = new BorderPane();

    Button logoutBtn = new Button("Logout");
    logoutBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.LOGIN, true);
    });

    Label title = new Label("Welcome, Professor!");
    title.setStyle("-fx-font-size: 18px;");

    Label userListBtnLbl = new Label("Open student list: ");
    Button userListBtn = new Button("Student list");
    userListBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.PROF_USERLIST, true);
    });

    Label gradeListBtnLbl = new Label("Open grade book: ");
    Button gradeListBtn = new Button("Grade book");
    gradeListBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.PROF_GRDBK, true);
    });


    HBox hbox01 = new HBox();
    hbox01.getChildren().addAll(userListBtnLbl, userListBtn, gradeListBtnLbl, gradeListBtn);
    hbox01.setAlignment(Pos.CENTER);

    VBox vbox01 = new VBox();
    Pane vBoxSpacer = new Pane();
    VBox.setVgrow(vBoxSpacer, Priority.ALWAYS);
    vbox01.getChildren().addAll(title, hbox01);
    vbox01.setAlignment(Pos.CENTER);

    base.setCenter(vbox01);
    base.setBottom(logoutBtn);
    BorderPane.setMargin(logoutBtn, new Insets(16));

    return getScene(base);
  }

}
