package controllers;

import static tools.Helpers.getScene;

import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.Grades;
import tools.SceneManager;
import tools.SceneType;
import tools.Session;

public class StudentGradebookController {

  public static Scene stdntGrdBkBuild(Stage stage) {
    DatabaseManager db = DatabaseManager.getInstance();
    int studentId = Session.getCurrentUserId();

    BorderPane base = new BorderPane();

    Label title = new Label("Your Gradebook");
    title.setStyle("-fx-font-size: 18px;");

    ListView<String> listView = new ListView<>();
    ObservableList<String> displayRows = FXCollections.observableArrayList();

    for (String row : db.getGradesForStudent(studentId)) {
      String[] parts = row.split("\\|");
      if (parts.length >= 3) {
        String titlePart = parts[0].trim();
        double score = Double.parseDouble(parts[1].replace("Score:", "").trim());
        double maxScore = Double.parseDouble(parts[2].replace("Score:", "").trim());
        String feedback = parts[2].replace("Feedback:", "").trim();

        displayRows.add(Grades.toStringGrade(titlePart, score, maxScore,feedback));
      } else {
        displayRows.add(row);
      }
    }

    if (displayRows.isEmpty()) {
      displayRows.add("No grades available.");
    }

    listView.setItems(displayRows);

    Button backBtn = new Button("Back");
    backBtn.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneType.STDNT_DASH, true));

    VBox content = new VBox(12, title, listView, backBtn);
    content.setPadding(new Insets(16));

    base.setCenter(content);
    return getScene(base);
  }
}