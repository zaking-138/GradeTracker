package controllers;

import static tools.Helpers.*;

import database.DatabaseManager;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;

import java.awt.*;

import static tools.Helpers.setVisible;

/**
 * @author Adam Vartan
 * <br>
 * created:
 * @since 0.1.0
 */
public class ProfessorGradebookController {

  private static int selectedUserId;
  private static String selectedRow;

  public static void setSelectedRow(String selectedRow) {
    ProfessorGradebookController.selectedRow = selectedRow;
  }

  public static void setSelectedUserId(int selectedUserId) { ProfessorGradebookController.selectedUserId = selectedUserId;
  }

  public static Scene profGrdBkBuild(Stage stage) {
    DatabaseManager db = DatabaseManager.getInstance();

    Label title = new Label("Displaying the grade book...");
    title.setStyle("-fx-font-size: 18px;");

    ListView<String> listView = new ListView<>();

    TextField inputField = new TextField();
    ObservableList<String> fieldOptions = FXCollections.observableArrayList(
            "Username",
            "Grade",
            "Notes"
    );
    ComboBox<String> fieldSelection = new ComboBox<>(fieldOptions);
    HBox editRow = new HBox(8);
    editRow.setAlignment(Pos.CENTER);
    HBox.setHgrow(inputField, Priority.ALWAYS);
    editRow.getChildren().addAll(new Label("Select field: "),
            fieldSelection, new Label("New Value: "), inputField);
    setVisible(editRow, false);

    Button editUserBtn = new Button("Edit");
    Button removeUserBtn = new Button("Remove");
    Button addUserBtn = new Button("Add User");

    Label noSelectionErrorLbl = new Label();
    setVisible(noSelectionErrorLbl, false);
    noSelectionErrorLbl.setText("Select a valid grade entry.");
    setVisible(noSelectionErrorLbl, true);
    listView.getItems().add("Username\t\tGrade\t\tNotes");





    return null;
  }
}
