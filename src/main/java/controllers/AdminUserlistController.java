package controllers;

import static tools.Helpers.*;
import static tools.UserNotification.addNotification;

import courses.Course;
import database.DatabaseManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.CourseRepository;
import tools.SceneManager;
import tools.SceneType;
import tools.UserListRepository;
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
  private static int selectedCourseId = -1;
  private static String selectedCourseRow = null;
  private static final int MAX_LENGTH = 20;
  private static final int SPACER_SPACE = MAX_LENGTH + 5;

  private static final String TOP_ROW_USER = "ID" + makeSpacer("ID".length())
          + "Username" + makeSpacer("Username".length())
          + "Password" + makeSpacer("Password".length())
          + "Role";

  private static final String TOP_ROW_COURSES = "ID" + makeSpacer("ID".length())
          + "Course Name" + makeSpacer("Course Name".length())
          + "Code" + makeSpacer("Code".length())
          + "Teacher ID" + makeSpacer("Teacher ID".length());
  private static void resetUserVars(){
    setSelectedRow(null);
    setSelectedUserId(-1);
  }

  private static void resetCourseVars(){
    setSelectedCourseRow(null);
    setSelectedCourseId(-1);
  }

  public static String makeSpacer(int minusLength) {
    int i = 0;
    StringBuilder temp = new StringBuilder();
    while (i < SPACER_SPACE - minusLength) {
      temp.append(" ");
      i++;
    }
    return temp.toString();
  }

  public static List<String> makeCourseList(List<Course> courseList){
    List<String> list = new ArrayList<>();
//    var temp = DatabaseManager.getInstance().getAllCourses(true);
    for(Course c : courseList){
      StringBuilder tempStrBldr = new StringBuilder();
      tempStrBldr.append(c.getCourseId()).append(makeSpacer(String.valueOf(c.getCourseId()).length()));
      int tempLength = MAX_LENGTH - c.getCourseName().length();
      if (tempLength < 0) {
        String tempStr = c.getCourseName().substring(0, 18) + "...";
        tempStrBldr.append(tempStr).append(makeSpacer(tempStr.length()));
      } else {
        tempStrBldr.append(c.getCourseName()).append(makeSpacer(c.getCourseName().length()));
      }
      tempStrBldr.append(c.getCourseCode()).append(makeSpacer(c.getCourseCode().length()));
      tempStrBldr.append(c.getTeacherId()).append(makeSpacer(c.getTeacherId()));
      list.add(tempStrBldr.toString());
    }
    return list;
  }

  public static List<String> makeList(List<User> userList) {
    List<String> list = new ArrayList<>();
//    var temp = DatabaseManager.getInstance().getAllUsers(true);
    for (User u : userList) {
      StringBuilder tempStrBldr = new StringBuilder();
      tempStrBldr.append(u.getUserId()).append(makeSpacer(String.valueOf(u.getUserId()).length()));

      int tempLength = MAX_LENGTH - u.getUsername().length();
      if (tempLength < 0) {
        String tempStr = u.getUsername().substring(0, 18) + "...";
        tempStrBldr.append(tempStr).append(makeSpacer(tempStr.length()));
      } else {
        tempStrBldr.append(u.getUsername());
        tempStrBldr.append(makeSpacer(u.getUsername().length()));
      }
      tempLength = MAX_LENGTH - u.getPassword().length();
      if (tempLength < 0) {
        String tempStr = u.getPassword().substring(0, 18) + "...";
        tempStrBldr.append(tempStr).append(makeSpacer(tempStr.length()));
      } else {
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
   * "success", "no_selection", "no_field_selection", "no_entry", "invalid_username",
   * "invalid_password", "invalid_role"
   */
  private static final Map<String, String> errorList = Map.of(
          "success", "Entry edited!",
          "no_selection", "Error: Select valid item from list!",
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

  public static int getSelectedCourseId() {
    return selectedCourseId;
  }

  public static void setSelectedCourseId(int selectedCourseId) {
    AdminUserlistController.selectedCourseId = selectedCourseId;
  }

  public static String getSelectedCourseRow() {
    return selectedCourseRow;
  }

  public static void setSelectedCourseRow(String selectedCourseRow) {
    AdminUserlistController.selectedCourseRow = selectedCourseRow;
  }

  public static void grabCourseID(String selectedItem, TextField inputField, HBox editRow){
    try {
      Integer.parseInt(String.valueOf(selectedItem.charAt(0)));
    } catch (Exception ex) {
      setSelectedCourseId(-1);
      setSelectedCourseRow(null);
      inputField.setPromptText("New Value");
      return;
    }
    StringBuilder temp = new StringBuilder();
    int i = 0;
    while (true) {
      try {
        temp.append(Integer.parseInt(String.valueOf(selectedItem.charAt(i))));
        i++;
      } catch (NumberFormatException ex) {
        break;
      }
    }
    if (!temp.isEmpty()) {
      setSelectedCourseId(Integer.parseInt(temp.toString()));
      setSelectedCourseRow(selectedItem);
      System.out.println("Selected course: " + selectedCourseId);
      inputField.setPromptText(selectedCourseRow);
      setVisible(editRow, true);
      return;
    }
    setSelectedUserId(-1);
    setSelectedRow(null);
    inputField.setPromptText("New Value");
  }

  public static void grabUserID(String selectedItem, TextField inputField, HBox editRow) {
    try {
      Integer.parseInt(String.valueOf(selectedItem.charAt(0)));
    } catch (Exception ex) {
      setSelectedUserId(-1);
      setSelectedRow(null);
      inputField.setPromptText("New Value");
      return;
    }
    StringBuilder temp = new StringBuilder();
    int i = 0;
    while (true) {
      try {
        temp.append(Integer.parseInt(String.valueOf(selectedItem.charAt(i))));
        i++;
      } catch (NumberFormatException ex) {
        break;
      }
    }
    if (!temp.isEmpty()) {
      setSelectedUserId(Integer.parseInt(temp.toString()));
      setSelectedRow(selectedItem);
      System.out.println("Selected user: " + selectedUserId);
      inputField.setPromptText(selectedRow);
      setVisible(editRow, true);
      return;
    }
    setSelectedUserId(-1);
    setSelectedRow(null);
    inputField.setPromptText("New Value");
  }

  public static Scene adminUserlistBuild(Stage stage) {
    DatabaseManager db = DatabaseManager.getInstance();
    UserListRepository repo = UserListRepository.getInstance();

    final String titleStrUsers = "GradeTracker 5000: User List";
    final String instructionsStrUsers = "Select a user from the list to edit / remove, or add a new one.";
    final String titleStrCourses = "GradeTracker 5000: Course List";
    final String instructionsStrCourses = "Select a course from the list to edit / remove, or add a new one.";

    Label title = new Label(titleStrUsers);
    Label instructions = new Label(instructionsStrUsers);
    title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");

    TabPane tabPane = new TabPane();
    ListView<String> listView = new ListView<>();
    listView.setStyle("-fx-font-family: monospace; -fx-font-weight: bold");
    Tab usersTab = new Tab("Users", listView);
    usersTab.setClosable(false);

    ListView<String> courseListView = new ListView<>();
    courseListView.setStyle("-fx-font-family: monospace; -fx-font-weight: bold");
    Tab coursesTab = new Tab("Courses", courseListView);
    coursesTab.setClosable(false);

    repo.addObserver(userList -> {
      listView.getItems().setAll(TOP_ROW_USER);
      listView.getItems().addAll(makeList(userList));
    });
    repo.refresh();

    CourseRepository.getInstance().addObserver(courseList -> {
      courseListView.getItems().setAll(TOP_ROW_COURSES);
      courseListView.getItems().addAll(makeCourseList(courseList));
    });
    CourseRepository.getInstance().refresh();

    TextField inputField = new TextField();
    ObservableList<String> fieldOptionsCourses = FXCollections.observableArrayList(
            "Course Name",
            "Course Code",
            "Teacher ID"
    );
    ObservableList<String> fieldOptions = FXCollections.observableArrayList(
            "Username",
            "Password",
            "Role"
    );
    ComboBox<String> fieldSelection = new ComboBox<>();
    fieldSelection.setItems(fieldOptions);
    HBox editRow = new HBox(8);
    editRow.setAlignment(Pos.CENTER);
    HBox.setHgrow(inputField, Priority.ALWAYS);
    editRow.getChildren().addAll(new Label("Select field: "),
            fieldSelection, new Label("New Value: "), inputField);
    setVisible(editRow, false);

    Button editUserBtn = new Button("Edit");
    Button editCourseBtn = new Button("Edit");
    setVisible(editCourseBtn, false);

    Button removeUserBtn = new Button("Remove");

    Button addUserBtn = new Button("Add User");
    Button addCourseBtn = new Button("Add Course");
    setVisible(addCourseBtn, false);

    Button backBtn = new Button("Back");

    Label noSelectionErrorLbl = new Label(errorList.get("no_selection"));

    tabPane.setOnMouseClicked(e -> {
      var temp = tabPane.getSelectionModel().getSelectedItem();
      switch (temp.getText()){
        case "Users":
          title.setText(titleStrUsers);
          instructions.setText(instructionsStrUsers);
          inputField.setPromptText(" ");
          setVisible(editUserBtn, true);
          setVisible(addUserBtn, true);
          setVisible(editCourseBtn, false);
          setVisible(addCourseBtn, false);
          fieldSelection.setItems(fieldOptions);
          break;
        case "Courses":
          title.setText(titleStrCourses);
          instructions.setText(instructionsStrCourses);
          inputField.setPromptText(" ");
          setVisible(editUserBtn, false);
          setVisible(addUserBtn, false);
          setVisible(editCourseBtn, true);
          setVisible(addCourseBtn, true);
          fieldSelection.setItems(fieldOptionsCourses);
          break;
        default:
          break;
      }
    });

    courseListView.setOnMouseClicked(e -> {
      noSelectionErrorLbl.setText(" ");
      setVisible(noSelectionErrorLbl, false);
      String selectedItem = courseListView.getSelectionModel().getSelectedItem();
      System.out.println(selectedItem);
      if (selectedItem != null && !selectedItem.isEmpty()) {
        grabCourseID(selectedItem, inputField, editRow);
      }
    });

    listView.setOnMouseClicked(e -> {
      noSelectionErrorLbl.setText(" ");
      setVisible(noSelectionErrorLbl, false);
      String selectedItem = listView.getSelectionModel().getSelectedItem();
      if (selectedItem != null && !selectedItem.isEmpty()) {
        grabUserID(selectedItem, inputField, editRow);
      }
    });

    editUserBtn.setOnAction(e -> {
      String grabField = fieldSelection.getValue();
      String grabNewValue = inputField.getText();
      inputField.clear();

      if (selectedUserId == -1) {
        noSelectionErrorLbl.setText(errorList.get("no_selection"));
        setVisible(noSelectionErrorLbl, true);
        return;
      }

      if (grabField == null) {
        noSelectionErrorLbl.setText(errorList.get("no_field_selection"));
        setVisible(noSelectionErrorLbl, true);
        return;
      }

      if (grabNewValue == null || grabNewValue.isEmpty()) {
        noSelectionErrorLbl.setText(errorList.get("no_entry"));
        setVisible(noSelectionErrorLbl, true);
        return;
      }
      noSelectionErrorLbl.setText(errorList.get("success"));
      repo.update(selectedUserId,
              grabField.equals("Username") ? grabNewValue : "",
              grabField.equals("Password") ? grabNewValue : "",
              grabField.equals("Role") ? grabNewValue : ""
      );
      inputField.setPromptText("");
      resetUserVars();
    });

    editCourseBtn.setOnAction(e -> {
      String grabField = fieldSelection.getValue();
      String grabNewValue = inputField.getText();
      inputField.clear();

      if (selectedCourseId == -1) {
        noSelectionErrorLbl.setText(errorList.get("no_selection"));
        setVisible(noSelectionErrorLbl, true);
        return;
      }

      if (grabField == null) {
        noSelectionErrorLbl.setText(errorList.get("no_field_selection"));
        setVisible(noSelectionErrorLbl, true);
        return;
      }

      if (grabNewValue == null || grabNewValue.isEmpty()) {
        noSelectionErrorLbl.setText(errorList.get("no_entry"));
        setVisible(noSelectionErrorLbl, true);
        return;
      }
      noSelectionErrorLbl.setText(errorList.get("success"));
      setVisible(noSelectionErrorLbl, true);
      System.out.println(selectedCourseId + ": " + grabField + " --> " + grabNewValue);
      CourseRepository.getInstance().update(selectedCourseId,
              grabField.equals("Course Name") ? grabNewValue : "",
              grabField.equals("Course Code") ? grabNewValue : "",
              grabField.equals("Teacher ID") ? Integer.parseInt(grabNewValue.strip()) : -1
      );
      inputField.setPromptText("");
      resetCourseVars();
    });

    backBtn.setOnAction(e -> {
      SceneManager.getInstance().navigateTo(SceneType.ADMIN_DASH);
    });

    addUserBtn.setOnAction(e -> {
      Stage popup = new Stage();
      SceneManager.getInstance().navigateToCopy(SceneType.SIGNUP_POPUP, popup);
      popup.setAlwaysOnTop(true);
      popup.show();
    });

    addCourseBtn.setOnAction(e -> {
      Stage popup = new Stage();
      Scene temp = Course.courseAddBuild(popup);
      popup.setScene(temp);
      popup.setAlwaysOnTop(true);
      popup.show();
    });

    removeUserBtn.setOnAction(e -> {
      if(selectedUserId != -1) {
        addNotification(repo.getUserInfo(selectedUserId));
      }
    });

    HBox btnsRow = new HBox(8);
    btnsRow.setAlignment(Pos.CENTER);
    Pane btnsRowSpacer = new Pane();
    HBox.setHgrow(btnsRowSpacer, Priority.ALWAYS);
    Pane btnsRowSpacer01 = new Pane();
    HBox.setHgrow(btnsRowSpacer01, Priority.ALWAYS);
    setVisible(noSelectionErrorLbl, false);
    btnsRow.getChildren()
            .addAll(editUserBtn, editCourseBtn, noSelectionErrorLbl, btnsRowSpacer,
                    addUserBtn, addCourseBtn, btnsRowSpacer01, removeUserBtn
            );
    tabPane.getTabs().addAll(usersTab, coursesTab);
    Pane vBoxSpacer = new Pane();
    VBox.setVgrow(vBoxSpacer, Priority.ALWAYS);
    VBox layout = new VBox(12, title, instructions, tabPane, editRow, btnsRow, vBoxSpacer, backBtn);
    layout.setPadding(new Insets(16));
    return new Scene(layout, getScreenSize().get("w"), getScreenSize().get("h"));
  }
}







