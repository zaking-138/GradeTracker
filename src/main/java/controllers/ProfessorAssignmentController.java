package controllers;

import static tools.Helpers.getScene;
import static tools.Helpers.setVisible;

import courses.Assignment;
import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;
import tools.Session;

public class ProfessorAssignmentController {

    public static Scene profAssignmentBuild(Stage stage) {
        DatabaseManager db = DatabaseManager.getInstance();
        int teacherId = Session.getCurrentUserId();

        Label title = new Label("Create Assignment");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label instructions = new Label("Create a new assignment for one of your courses.");

        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.setPromptText("Select a course");
        courseBox.setPrefWidth(300);
        courseBox.setItems(db.getCoursesByTeacherObservable(teacherId));

        TextField titleField = new TextField();
        titleField.setPromptText("Assignment title");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Assignment description");
        descriptionField.setPrefRowCount(3);

        TextField maxPointsField = new TextField();
        maxPointsField.setPromptText("Max points");

        TextField dueDateField = new TextField();
        dueDateField.setPromptText("Due date (YYYY-MM-DD)");

        Label status = new Label();
        status.setStyle("-fx-text-fill: red;");
        setVisible(status, false);

        ListView<String> assignmentsList = new ListView<>();
        assignmentsList.setPrefHeight(250);

        Button addBtn = new Button("Add Assignment");
        Button refreshBtn = new Button("Refresh Assignments");
        Button backBtn = new Button("Back");

        HBox buttonRow = new HBox(10, addBtn, refreshBtn, backBtn);
        buttonRow.setAlignment(Pos.CENTER_LEFT);
//        AssignmentRepository.getInstance().addObserver(assignments -> {
//            assignmentsList.getItems().setAll(assignments);
//        });

        Runnable refreshAssignments = () -> {
            String selectedCourse = courseBox.getValue();
            System.out.println(selectedCourse);
            if (selectedCourse == null || selectedCourse.isBlank()) {
                assignmentsList.getItems().setAll("Select a course to view assignments.");
                return;
            }

            try {
                int courseId = Integer.parseInt(selectedCourse.split("\\|")[0].trim());
                ObservableList<String> assignments = db.getAssignmentsByCourseObservable(courseId);

                if (assignments == null || assignments.isEmpty()) {
                    assignmentsList.getItems().setAll("No assignments for this course.");
                } else {
                    assignmentsList.setItems(assignments);
                }
            } catch (Exception ex) {
                assignmentsList.getItems().setAll("Could not load assignments.");
            }
        };

        addBtn.setOnAction(e -> {
            refreshAssignments.run();
            status.setText("");
            status.setStyle("-fx-text-fill: red;");
            setVisible(status, false);

            String selectedCourse = courseBox.getValue();
            String assignmentTitle = titleField.getText();
            String description = descriptionField.getText();
            String maxPointsText = maxPointsField.getText();
            String dueDate = dueDateField.getText();

            if (selectedCourse == null || selectedCourse.isBlank()) {
                status.setText("Please select a course.");
                setVisible(status, true);
                return;
            }

            if (assignmentTitle == null || assignmentTitle.isBlank()) {
                status.setText("Please enter an assignment title.");
                setVisible(status, true);
                return;
            }

            if (maxPointsText == null || maxPointsText.isBlank()) {
                status.setText("Please enter max points.");
                setVisible(status, true);
                return;
            }

            if (dueDate == null || dueDate.isBlank()) {
                status.setText("Please enter a due date.");
                setVisible(status, true);
                return;
            }

            double maxPoints;
            try {
                maxPoints = Double.parseDouble(maxPointsText.trim());
                if (maxPoints <= 0) {
                    status.setText("Max points must be greater than 0.");
                    setVisible(status, true);
                    return;
                }
            } catch (NumberFormatException ex) {
                status.setText("Max points must be a valid number.");
                setVisible(status, true);
                return;
            }

            int courseId;
            try {
                courseId = Integer.parseInt(selectedCourse.split("\\|")[0].trim());
            } catch (Exception ex) {
                status.setText("Could not read selected course ID.");
                setVisible(status, true);
                return;
            }

            db.insertAssignment(assignmentTitle, description, maxPoints, dueDate, courseId);

            status.setStyle("-fx-text-fill: green;");
            status.setText("Assignment added successfully.");
            setVisible(status, true);

            titleField.clear();
            descriptionField.clear();
            maxPointsField.clear();
            dueDateField.clear();
            refreshAssignments.run();
        });

        refreshBtn.setOnAction(e -> refreshAssignments.run());

        backBtn.setOnAction(e ->
                SceneManager.getInstance().navigateTo(SceneType.PROF_DASH, true)
        );

        VBox layout = new VBox(
                12,
                title,
                instructions,
                new Label("Course:"),
                courseBox,
                new Label("Title:"),
                titleField,
                new Label("Description:"),
                descriptionField,
                new Label("Max Points:"),
                maxPointsField,
                new Label("Due Date:"),
                dueDateField,
                buttonRow,
                status,
                new Label("Assignments for selected course:"),
                assignmentsList
        );

        layout.setPadding(new Insets(16));
        VBox.setVgrow(assignmentsList, Priority.ALWAYS);

        return getScene(layout);
    }
}