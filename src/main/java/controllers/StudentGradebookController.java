package controllers;

import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;
import tools.Session;

import static tools.Helpers.getScene;
import static tools.Helpers.setVisible;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class StudentGradebookController {

  private static int selectedUserId;
  private static String selectedRow;

  public static void setSelectedRow(String selectedRow) {
    StudentGradebookController.selectedRow = selectedRow;
  }

  public static void setSelectedUserId(int selectedUserId) { StudentGradebookController.selectedUserId = selectedUserId;
  }

  public static Scene stdntGrdBkBuild(Stage stage) {
    DatabaseManager db = DatabaseManager.getInstance();
    int studentId = Session.getCurrentUserId();

    BorderPane borderPane = new BorderPane();

    Label title = new Label("Student Gradebook");
    title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold");

    ListView<String> gradesList = new ListView<>();
    if (studentId == -1) {
      gradesList.setItems(db.getGradesByStudentObservable(studentId));
    }
    if (gradesList.getItems().isEmpty()) {
      gradesList.getItems().add("No grades available");
    }

    Button backButton = new Button("Back to Dashboard");
    backButton.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.STDNT_DASH);
    });

    VBox content = new VBox(12, title, gradesList, backButton);
    content.setPadding(new Insets(16));

    borderPane.setCenter(content);
    return getScene(borderPane);
  }

}
