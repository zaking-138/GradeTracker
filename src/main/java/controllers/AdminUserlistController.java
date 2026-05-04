package controllers;

import static tools.Helpers.*;

import database.DatabaseManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;
import users.User;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class AdminUserlistController {

  private static int selectedUserId = -1;
  private static String selectedRow = null;
  private static final int MAX_LENGTH = 20;
  private static final int SPACER_SPACE = MAX_LENGTH + 5;

  private static final String TOP_ROW = "ID" + makeSpacer("ID".length())
      + "Username" + makeSpacer("Username".length())
      + "Password" + makeSpacer("Password".length())
      + "Role";

  public static String makeSpacer(int minusLength){
    int i = 0;
    StringBuilder temp = new StringBuilder();
    while(i < SPACER_SPACE - minusLength){
      temp.append(" ");
      i++;
    }
    return temp.toString();
  }

  public static List<String> makeList() {
    List<String> list = new ArrayList<>();
    var temp = DatabaseManager.getInstance().getAllUsers(true);
    for(User u : temp){
      StringBuilder tempStrBldr = new StringBuilder();
      tempStrBldr.append(u.getUserId()).append(makeSpacer(String.valueOf(u.getUserId()).length()));

      int tempLength = MAX_LENGTH - u.getUsername().length();
      if(tempLength < 0){
        String tempStr = u.getUsername().substring(0, 18) + "...";
        tempStrBldr.append(tempStr).append(makeSpacer(tempStr.length()));
      }else{
        tempStrBldr.append(u.getUsername());
        tempStrBldr.append(makeSpacer(u.getUsername().length()));
      }
      tempLength = MAX_LENGTH - u.getPassword().length();
      if(tempLength < 0){
        String tempStr = u.getPassword().substring(0, 18) + "...";
        tempStrBldr.append(tempStr).append(makeSpacer(tempStr.length()));
      }else{
        tempStrBldr.append(u.getPassword());
        tempStrBldr.append(makeSpacer(u.getPassword().length()));
      }
      tempStrBldr.append(u.getRole());
      //        System.out.println(tempString);
      list.add(tempStrBldr.toString());
    }
    return list;
  }

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
    setVisible(editRow, false);

    Button editUserBtn = new Button("Edit");
    Button removeUserBtn = new Button("Remove");
    Button addUserBtn = new Button("Add User");
    Button backBtn = new Button("Back");
    Label noSelectionErrorLbl = new Label(errorList.get("no_selection"));
//    Button addBtn  = new Button("Add");
    listView.setStyle("-fx-font-family: monospace; -fx-font-weight: bold");
    listView.getItems().setAll(TOP_ROW);
    listView.getItems().addAll(makeList());

    listView.setOnMouseClicked(e -> {
      noSelectionErrorLbl.setText(" ");
      setVisible(noSelectionErrorLbl, false);
      String selectedItem = listView.getSelectionModel().getSelectedItem();
      if(selectedItem != null && !selectedItem.isEmpty()){
        try{
          Integer.parseInt(String.valueOf(selectedItem.charAt(0)));
        } catch (Exception ex) {
          setSelectedUserId(-1);
          setSelectedRow(null);
          inputField.setPromptText("ID - USERNAME - PASSWORD");
          return;
        }
        StringBuilder temp = new StringBuilder();
        int i = 0;
        while(true){
          try{
            temp.append(Integer.parseInt(
                String.valueOf(selectedItem.charAt(i))
            ));
            i++;
          } catch (NumberFormatException ex) {
            break;
          }
        }
        setSelectedUserId(Integer.parseInt(temp.toString()));
        setSelectedRow(selectedItem);
        System.out.println("Selected user: " + selectedUserId);

        inputField.setPromptText(selectedRow);
        setVisible(editRow, true);
        return;
      }
      setSelectedUserId(-1);
      setSelectedRow(null);
      inputField.setPromptText("ID - USERNAME - PASSWORD");
    });

    editUserBtn.setOnAction(e -> {
      String grabField = fieldSelection.getValue();
      String grabNewValue = inputField.getText();
      inputField.setText("");

      if(selectedUserId == -1){
        noSelectionErrorLbl.setText(errorList.get("no_selection"));
        setVisible(noSelectionErrorLbl, true);
        return;
      }

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
      listView.getItems().setAll(TOP_ROW);
      listView.getItems().addAll(makeList());
    });

    backBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.ADMIN_DASH);
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
    btnsRow.getChildren().addAll(editUserBtn, noSelectionErrorLbl, btnsRowSpacer, addUserBtn, btnsRowSpacer01, removeUserBtn);

//    HBox.setHgrow(removeUserBtn, Priority.ALWAYS);
    Pane vBoxSpacer = new Pane();
    VBox.setVgrow(vBoxSpacer, Priority.ALWAYS);
    VBox layout = new VBox(12, title, listView, editRow, btnsRow, vBoxSpacer, backBtn);
    layout.setPadding(new Insets(16));
    return new Scene(layout, getScreenSize().get("w"), getScreenSize().get("h"));
  }
}
