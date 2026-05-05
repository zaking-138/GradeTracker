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
import tools.Grades;
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

    Label title = new Label("Professor Gradebook");
    title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");

    Label instructions = new Label("Enter assignment ID, load grades, then select a student and update score/feedback");

    Label assignment = new Label("Assignment ID:");
    TextField assignmentId = new TextField();
    assignmentId.setPromptText("Enter assignment ID");
    assignmentId.setMaxWidth(120);

    Button loadGrades = new Button("Load Grades");
    HBox loadRow = new HBox(10, assignment,  assignmentId, loadGrades);
    loadRow.setAlignment(Pos.CENTER_LEFT);

    ListView <String> list = new ListView<>();
    list.setPrefHeight(300);

    Label student = new Label("Student:");
    ComboBox<String> studentBox = new ComboBox<>();
    studentBox.setPromptText("Select student");
    studentBox.setItems(FXCollections.observableArrayList(db.getAllStudents()));
    studentBox.setPrefWidth(220);

    HBox studentRow = new HBox(10, student, studentBox);
    studentRow.setAlignment(Pos.CENTER_LEFT);

    Label score = new Label("Score:");
    TextField scoreField = new TextField();
    scoreField.setPromptText("Enter score");
    scoreField.setMaxWidth(120);

    Label feedback = new Label("Feedback:");
    TextField feedbackField = new TextField();
    feedbackField.setPromptText("Enter feedback");
    HBox.setHgrow(feedbackField, Priority.ALWAYS);

    HBox editRow = new HBox(10, score, scoreField, feedback,  feedbackField);
    editRow.setAlignment(Pos.CENTER_LEFT);

    Label status = new Label();
    status.setStyle("-fx-text-fill: red");
    setVisible(status, false);

    Button save = new Button("Save");
    Button clear =  new Button("Clear");
    Button back = new Button("Back");

    HBox buttonRow = new HBox(10, save, clear, back);
    buttonRow.setAlignment(Pos.CENTER_LEFT);

    loadGrades.setOnAction(e -> {
            status.setText("");
            setVisible(status, true);
            String assignmentText = assignmentId.getText();

            if (assignmentText.isBlank() || assignmentText == null) {
              status.setText("Please enter assignment ID");
              setVisible(status, true);
              return;
            }

            int assignId;
            try{
              assignId = Integer.parseInt(assignmentText.trim());
            }catch(NumberFormatException ex){
              status.setText("Assignment ID is invalid!");
              setVisible(status, true);
              return;
            }
            ObservableList<String> displayRow = FXCollections.observableArrayList();

            for(String row : db.getGradesForAssignment(assignId)){
              String [] values = row.split("\\|");
              if(values.length >= 2){}
              try{
                String username = values[0].trim();
                double scoretem = Double.parseDouble(values[1].replace("Score:", "").trim());
                String feed = values[2].replace("Feedback:", "").trim();

                String formatted = username +
                        " | Score: " + scoretem +
                        " | Feedback: " + feed;
                displayRow.add(formatted);
              }catch(Exception ex){
                displayRow.add(row);
              }
            }
            if(displayRow.isEmpty()){
              displayRow.add("No grade entries found for Assignment ID: " + assignId);
            }

            list.setItems(displayRow);});

      save.setOnAction(e -> {
            status.setText("");
            setVisible(status, false);

            String assignText = assignmentId.getText();
            String selectedStudent = studentBox.getValue();
            String scoreText = scoreField.getText();
            String feedbackText = feedbackField.getText();

            if (assignText == null || assignText.isBlank()) {
              status.setText("Enter an Assignment ID first");
              setVisible(status, true);
              return;
            }

            if (selectedStudent == null || selectedStudent.isBlank()) {
              status.setText("Select an Student");
              setVisible(status, true);
              return;
            }

            if (scoreText == null || scoreText.isBlank()) {
              status.setText("Enter score");
              setVisible(status, true);
              return;
            }

            int assign;
            int studentId;
            double scoren;

            try{
              assign = Integer.parseInt(assignText.trim());
            }catch(NumberFormatException ex){
              status.setText("Assignment ID is invalid!");
              setVisible(status, true);
              return;
            }

            try{
              studentId = Integer.parseInt(selectedStudent.split("-")[0].trim());
            }catch(Exception ex){
              status.setText("Could not parse selected student!");
              setVisible(status, true);
              return;
            }

            try{
              scoren = Double.parseDouble(scoreText.trim());
            }catch(NumberFormatException ex){
              status.setText("Score is invalid!");
              setVisible(status, true);
              return;
            }


            if (!Grades.isValidScore(scoren)) {
              status.setText("Score is invalid!");
              setVisible(status, true);
              return;
            }

            boolean exists = false;

            for (String row : db.getGradesForAssignment(assign)){
              if(row.startsWith(selectedStudent.substring(selectedStudent.indexOf('|') + 1).trim())){
                exists = true;
                break;
              }
            }

            if(exists){
              db.updateGrades(studentId, assign, scoren, feedbackText);
            }else {
              db.insertGrades(studentId, assign, scoren, feedbackText);
            }

            status.setStyle("-fx-text-fill: green");
            status.setText("Assignment ID: " + assign);
            setVisible(status, true);

            ObservableList<String> refreshRow = FXCollections.observableArrayList();
            for(String row : db.getGradesForAssignment(assign)){
              String [] values = row.split("\\|");
              if(values.length >= 3){
                try{
                  String username = values[0].trim();
                  double scoretem = Double.parseDouble(values[1].replace("Score:", "").trim());
                  String feed = values[2].replace("Feedback:", "").trim();

                  String formatted = username +
                          " | Score: " + scoretem +
                          " | Feedback: " + feed;

                  refreshRow.add(formatted);
                }catch(Exception ex){
                  refreshRow.add(row);
                }
              }else {
                refreshRow.add(row);
              }
            }

            if(refreshRow.isEmpty()){
              refreshRow.add("No grade entries found for Assignment ID: " + assign);
            }

            list.setItems(refreshRow);
            }
            );

      clear.setOnAction(e -> {
        assignmentId.clear();
        studentBox.getSelectionModel().clearSelection();
        scoreField.clear();
        feedbackField.clear();
        list.getItems().clear();
        status.setText("");
        status.setStyle("-fx-text-fill: red");
        setVisible(status, false);
      });

      back.setOnAction(e -> {
        SceneManager.getInstance().navigateTo(SceneType.PROF_DASH);
      });

      VBox layout = new VBox(12,
              title,
              instructions,
              loadRow,
              list,
              studentRow,
              editRow,
              buttonRow,
              status);

      layout.setPadding(new Insets(16));

      return getScene(layout);


    /*ListView<String> listView = new ListView<>();

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

    String scoreText = inputField.getText();

    double score;
    try{
      score = Double.parseDouble(scoreText);
    }catch(NumberFormatException e){
      noSelectionErrorLbl.setText("Invalid score value.");
      setVisible(noSelectionErrorLbl, true);
    }

    if (!Grades.isValidScore(score)) {
      noSelectionErrorLbl.setText("Invalid score value.");
      setVisible(noSelectionErrorLbl, true);
    }

    db.updateGrades(studentId, assignmentId, score, feedback);

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
    return new Scene(layout, getScreenSize().get("w"), getScreenSize().get("h"));*/
  }
}
