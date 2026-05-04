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
public class StudentDashboardController {

  private static int selectedUserId;
  private static String selectedRow;

  public static void setSelectedRow(String selectedRow) {
    StudentDashboardController.selectedRow = selectedRow;
  }

  public static void setSelectedUserId(int selectedUserId) { StudentDashboardController.selectedUserId = selectedUserId;
  }

  public static Scene stdntDashBuild(Stage stage) {
    DatabaseManager db = DatabaseManager.getInstance();
    int studentId = Session.getCurrentUserId();
    String username = Session.getCurrentUsername();

    BorderPane borderPane = new BorderPane();

    Label title = new Label("Student Dashboard");
    title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

    Label welcome = new Label("Welcome, " + (username != null ? username : "Student" + "!"));

    TabPane  tabPane = new TabPane();

    ListView<String> gradesList = new ListView<>();
    if (studentId == -1) {
      gradesList.setItems(db.getGradesByStudentObservable(studentId));
    }
    if (gradesList.getItems().isEmpty()) {
      gradesList.getItems().add("No grades available.");
    }
    Tab gradesTab = new Tab("Grades", gradesList);
    gradesTab.setClosable(false);

    ListView<String> coursesList = new ListView<>();
    coursesList.setItems(db.getAllCoursesObservable());
    if (coursesList.getItems().isEmpty()) {
      coursesList.getItems().add("No courses available.");
    }
    Tab coursesTab = new Tab("Courses", coursesList);
    coursesTab.setClosable(false);

    ListView<String> assignmentsList = new ListView<>();
    assignmentsList.getItems().add("Select a course from the Course tab.");
    Tab assignmentsTab = new Tab("Assignments", assignmentsList);
    assignmentsTab.setClosable(false);

    coursesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null &&  !newValue.equals("|")) {
        try{
          int courseId = Integer.parseInt(newValue.split("\\|")[0].trim());
          ObservableList<String> assignments = db.getAssignmentsByCourseObservable(courseId);

          if (assignments.isEmpty()) {
            assignmentsList.getItems().add("No assignments for this course.");
          }else {
            assignmentsList.setItems(assignments);
          }
          tabPane.getSelectionModel().select(assignmentsTab);
        }catch (NumberFormatException e){
          assignmentsList.getItems().setAll("Could not load assignments");
        }
      }
    });

    tabPane.getTabs().addAll(gradesTab, coursesTab, assignmentsTab);

    Button openGradesButton = new Button("Open Grades");
    openGradesButton.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.STDNT_GRDBK, true);
    });

    Button logoutButton = new Button("Logout");
    logoutButton.setOnAction(e -> {
      Session.clear();
      SceneManager.getInstance().navigateTo(SceneType.LOGIN, true);
    });

    VBox content = new VBox(12, title, welcome, tabPane, openGradesButton, logoutButton);
    content.setPadding(new Insets(16));

    borderPane.setCenter(content);

    return getScene(borderPane);



  }

}
