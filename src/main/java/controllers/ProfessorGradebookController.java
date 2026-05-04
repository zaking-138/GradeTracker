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
import java.util.Map;

import static tools.Helpers.setVisible;

/**
 * @author Adam Vartan
 * <br>
 * created:
 * @since 0.1.0
 */
public class ProfessorGradebookController {

  private static final Map<String, String> errorList = Map.of(
          "success", "Entry edited!",
          "no_selection", "Error: Select valid user from list!",
          "no_field_selection", "Error: Select field to edit!",
          "no_entry", "Error: New value cannot be empty!",
          "invalid_username", "Error: New username is invalid!",
          "invalid_password", "Error: New password is invalid!",
          "invalid_role", "Error: New role is invalid!"
  );

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
    listView.getItems().addAll(db.getGradesForStudent(selectedUserId));

    listView.setOnMouseClicked(e -> {
      noSelectionErrorLbl.setText(" ");
      setVisible(noSelectionErrorLbl, false);
      String selectedItem = listView.getSelectionModel().getSelectedItem();
      if(selectedItem != null && !selectedItem.isEmpty()){
        String temp = selectedItem.split("\t")[0];
        if(temp.equals("ID")){
          setSelectedUserId(0);
          setSelectedRow(null);
          inputField.setPromptText("ID - USERNAME - PASSWORD");
          return;
        }
        setSelectedUserId( Integer.parseInt(temp) );
        setSelectedRow(selectedItem);
        System.out.println("Selected user: " + selectedUserId);

        inputField.setPromptText(selectedRow);
        setVisible(editRow, true);
      }
    });

    Button backBtn = new Button("Back");
    editUserBtn.setOnAction(e -> {
      String grabField = fieldSelection.getValue();
      String grabNewValue = inputField.getText();

      if(grabField == null){
        noSelectionErrorLbl.setText(errorList.get("no_field_selection"));
        setVisible(noSelectionErrorLbl, true);
        return;
      }

      if(grabNewValue == null || grabNewValue.isEmpty()){
        noSelectionErrorLbl.setText(errorList.get("no_entry"));
        setVisible(noSelectionErrorLbl, true);
        return;
      }
      noSelectionErrorLbl.setText(errorList.get("success"));
      setVisible(noSelectionErrorLbl, true);
      System.out.println(selectedUserId + ": " + grabField + " --> " + grabNewValue);
      Map<String, String> userInfo = DatabaseManager.getInstance().getUser(selectedUserId);
      System.out.println(userInfo.get("username") + " " + userInfo.get("password") + " " + userInfo.get("role"));

      DatabaseManager.getInstance().updateUser(selectedUserId,
              grabField.equals("Username") ? grabNewValue : "",
              grabField.equals("Password") ? grabNewValue : "",
              grabField.equals("Role") ? grabNewValue : ""
      );
      listView.getItems().setAll("ID\tUsername");
      listView.getItems().addAll(db.getAllStudents());
    });

    backBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.PROF_DASH);
    });

    addUserBtn.setOnAction(e -> {
      Stage popup = new Stage();
      SceneManager.getInstance().navigateToCopy(SceneType.SIGNUP_POPUP, popup);
      popup.show();
    });

    HBox btnsRow = new HBox(8);
    btnsRow.setAlignment(Pos.CENTER);
    Pane btnsRowSpacer = new Pane();
    HBox.setHgrow(btnsRowSpacer, Priority.ALWAYS);
    Pane btnsRowSpacer01 = new Pane();
    HBox.setHgrow(btnsRowSpacer01, Priority.ALWAYS);
    setVisible(noSelectionErrorLbl, false);
    btnsRow.getChildren().addAll(editUserBtn, btnsRowSpacer, addUserBtn, btnsRowSpacer01, removeUserBtn);

    Pane vBoxSpacer = new Pane();
    VBox.setVgrow(vBoxSpacer, Priority.ALWAYS);
    VBox layout = new VBox(12, title, listView, editRow, btnsRow, noSelectionErrorLbl, vBoxSpacer, backBtn);
    layout.setPadding(new Insets(16));
    return new Scene(layout, getScreenSize().get("w"), getScreenSize().get("h"));
  }
}
