package controllers;

import static tools.Helpers.getScreenSize;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;

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
      SceneManager.getInstance().navigateTo(SceneType.LOGIN);
    });

    Label welcomeLbl = new Label("Welcome, {admin name}!");

    Label userListBtnLbl = new Label("Open full userlist: ");
    Button userListBtn = new Button("Userlist");
    userListBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.ADMIN_USERLIST);
    });

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
}
