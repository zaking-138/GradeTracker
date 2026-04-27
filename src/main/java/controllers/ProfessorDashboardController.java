package controllers;

import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import static tools.Helpers.setVisible;

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
    DatabaseManager db = DatabaseManager.getInstance();

    Label title = new Label("Displaying your students...");
    title.setStyle("-fx-font-size: 18px;");

    ListView<String> listView = new ListView<>();

    TextField inputField = new TextField();
    ObservableList<String> fieldOptions = FXCollections.observableArrayList(
            "ID",
            "Username",
            "User_ID"
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
    listView.getItems().add("ID\t\tUsername\t\tUser_ID");

    return null;
  }

}
