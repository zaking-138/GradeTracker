package controllers;

import static tools.Helpers.getScreenSize;

import database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
public class AdminUserlistController {
  public static Scene adminUserlistBuild(Stage stage) {
    DatabaseManager db = DatabaseManager.getInstance();

    Label title = new Label("Displaying all users...");
    title.setStyle("-fx-font-size: 18px;");

    ListView<String> listView = new ListView<>();

    TextField inputField = new TextField();
//    inputField.setPromptText("New todo...");
    Button addBtn  = new Button("Add");
    Button backBtn = new Button("Back");

//    addBtn.setOnAction(e -> {
//      String text = inputField.getText().trim();
//      if (!text.isEmpty()) {
//        db.insertTodo(text);
//        listView.getItems().setAll(db.getAllNames());  // refresh list
//        inputField.clear();
//      }
//    });
    backBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.LOGIN);
    });

    HBox inputRow = new HBox(8, inputField, addBtn);
    HBox.setHgrow(inputField, Priority.ALWAYS);
    VBox layout = new VBox(12, title, listView, inputRow, backBtn);
    layout.setPadding(new Insets(16));
    return new Scene(layout, getScreenSize().get("w"), getScreenSize().get("h"));
  }
}
