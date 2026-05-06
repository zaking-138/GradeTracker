package controllers;

import static database.DatabaseManager.getCurrentUser;
import static tools.Helpers.getScene;
import static tools.Helpers.getScreenSize;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;
import tools.Session;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class AdminDashboardController {
  public static Scene adminDashBuild(Stage stage) {
    BorderPane base = new BorderPane();

    Button logoutBtn = new Button("Logout");
    logoutBtn.setOnAction(e -> {
      Session.clear();
      SceneManager.getInstance().navigateTo(SceneType.LOGIN, true);
    });

    Label welcomeLbl = new Label("Welcome, " + Session.getCurrentUsername() + "!");

    Label userListBtnLbl = new Label("Open full userlist: ");
    Button userListBtn = new Button("Userlist");
    userListBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.ADMIN_USERLIST, true);
    });

    HBox hbox01 = new HBox();
    hbox01.getChildren().addAll(userListBtnLbl, userListBtn);
    hbox01.setAlignment(Pos.CENTER);

    VBox vbox01 = new VBox();
    Pane vBoxSpacer = new Pane();
    VBox.setVgrow(vBoxSpacer, Priority.ALWAYS);
    vbox01.getChildren().addAll(welcomeLbl, hbox01);
    vbox01.setAlignment(Pos.CENTER);

    base.setCenter(vbox01);
    base.setBottom(logoutBtn);
    BorderPane.setMargin(logoutBtn, new Insets(16));

    return getScene(base);
  }
}
