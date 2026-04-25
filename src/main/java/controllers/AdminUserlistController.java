package controllers;

import static tools.Helpers.getScreenSize;

import database.DatabaseManager;
import java.util.HashMap;
import java.util.Map;
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

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class AdminUserlistController {

  private static int selectedUserId;
  private static String selectedRow;

  /**
   * "success", "no_selection", "no_field_selection", "no_entry", "invalid_username", "invalid_password", "invalid_role"
   */
  private static final Map<String, String> errorList = Map.of(
          "success", "Entry edited!",
      "no_selection", "Error: Select valid user from list!",
      "no_field_selection", "Error: Select field to edit!",
      "no_entry", "Error: New value cannot be empty!",
      "invalid_username", "Error: New username is invalid!",
      "invalid_password", "Error: New password is invalid!",
      "invalid_role", "Error: New role is invalid!"
  );

  public static void setSelectedRow(String selectedRow) {
    AdminUserlistController.selectedRow = selectedRow;
  }

  public static void setSelectedUserId(int selectedUserId) {
    AdminUserlistController.selectedUserId = selectedUserId;
  }

  public static Scene adminUserlistBuild(Stage stage) {
    DatabaseManager db = DatabaseManager.getInstance();

    Label title = new Label("Displaying all users...");
    title.setStyle("-fx-font-size: 18px;");

    ListView<String> listView = new ListView<>();

    TextField inputField = new TextField();
    ObservableList<String> fieldOptions = FXCollections.observableArrayList(
        "Username",
        "Password",
        "Role"
    );
    ComboBox<String> fieldSelection = new ComboBox<>(fieldOptions);
    HBox editRow = new HBox(8);
    editRow.setAlignment(Pos.CENTER);
    HBox.setHgrow(inputField, Priority.ALWAYS);
    editRow.getChildren().addAll(new Label("Select field: "),
        fieldSelection, new Label("New Value: "), inputField);
    editRow.setVisible(false);

    Button editUserBtn = new Button("Edit");
    Button removeUserBtn = new Button("Remove");
    Label noSelectionErrorLbl = new Label(errorList.get("no_selection"));
//    Button addBtn  = new Button("Add");
    listView.getItems().add("ID\tUsername\t\tRole\t\tPassword");
    listView.getItems().addAll(db.getAllUsers());

    listView.setOnMouseClicked(e -> {
      noSelectionErrorLbl.setText(" ");
      noSelectionErrorLbl.setVisible(false);
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
        editRow.setVisible(true);
      }
    });

    Button backBtn = new Button("Back");
    editUserBtn.setOnAction(e -> {
      String grabField = fieldSelection.getValue();
      String grabNewValue = inputField.getText();

      if(grabField == null){
        noSelectionErrorLbl.setText(errorList.get("no_field_selection"));
        noSelectionErrorLbl.setVisible(true);
        return;
      }

      if(grabNewValue == null || grabNewValue.isEmpty()){
        noSelectionErrorLbl.setText(errorList.get("no_entry"));
        noSelectionErrorLbl.setVisible(true);
        return;
      }
      noSelectionErrorLbl.setText(errorList.get("success"));
      noSelectionErrorLbl.setVisible(true);
      System.out.println(selectedUserId + ": " + grabField + " --> " + grabNewValue);
      Map<String, String> userInfo = DatabaseManager.getInstance().getUser(selectedUserId);
      System.out.println(userInfo.get("username") + " " + userInfo.get("password") + " " + userInfo.get("role"));

      DatabaseManager.getInstance().updateUser(selectedUserId,
          grabField.equals("Username") ? grabNewValue : "",
          grabField.equals("Password") ? grabNewValue : "",
          grabField.equals("Role") ? grabNewValue : ""
          );
      listView.getItems().setAll("ID\tUsername\t\tRole\t\tPassword");
      listView.getItems().addAll(db.getAllUsers());
    });

    backBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.ADMIN_DASH);
    });

    HBox btnsRow = new HBox(8);
    btnsRow.setAlignment(Pos.CENTER);
    Pane btnsRowSpacer = new Pane();
    HBox.setHgrow(btnsRowSpacer, Priority.ALWAYS);
    Pane btnsRowSpacer01 = new Pane();
    HBox.setHgrow(btnsRowSpacer01, Priority.ALWAYS);
    noSelectionErrorLbl.setVisible(false);
    btnsRow.getChildren().addAll(editUserBtn, btnsRowSpacer, noSelectionErrorLbl, btnsRowSpacer01, removeUserBtn);

//    HBox.setHgrow(removeUserBtn, Priority.ALWAYS);
    Pane vBoxSpacer = new Pane();
    VBox.setVgrow(vBoxSpacer, Priority.ALWAYS);
    VBox layout = new VBox(12, title, listView, btnsRow, editRow, vBoxSpacer, backBtn);
    layout.setPadding(new Insets(16));
    return new Scene(layout, getScreenSize().get("w"), getScreenSize().get("h"));
  }
}
