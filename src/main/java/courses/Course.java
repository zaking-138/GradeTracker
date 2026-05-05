package courses;

import static tools.Helpers.getScene;
import static tools.Helpers.setVisible;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import tools.CourseRepository;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class Course {
  private int courseId;
  private String courseName;
  private String courseCode;
  private int teacherId;

  public static Scene courseAddBuild(Stage popupStage){
    Label header = new Label("Add new course:"), errorLbl = new Label();
    setVisible(errorLbl, false);
    TextField courseNameInput = new TextField(),
        courseCodeInput = new TextField(), teacherIDInput = new TextField();
    Button addBtn = new Button("Add"), doneBtn = new Button("Done");
    addBtn.setAlignment(Pos.BASELINE_RIGHT);

    GridPane gridPane = new GridPane();
    gridPane.setHgap(6);
    gridPane.setVgap(12);
    GridPane.setHalignment(addBtn, HPos.RIGHT);
    gridPane.setAlignment(Pos.CENTER);
    gridPane.addRow(0, new Label("Course Name:"), courseNameInput);
    gridPane.addRow(1, new Label("Course Code:"), courseCodeInput);
    gridPane.addRow(2, new Label("Teacher ID:"), teacherIDInput);
    gridPane.addRow(3, doneBtn, addBtn);

    BorderPane base = new BorderPane();
    errorLbl.setAlignment(Pos.CENTER);
    base.setCenter(gridPane);

    doneBtn.setOnAction(e -> {
      popupStage.close();
    });

    addBtn.setOnAction(e ->{
      String courseName = courseNameInput.getText().strip();
      if(courseName.isEmpty()){
        errorLbl.setText("Please enter a course name!");
        setVisible(errorLbl, true);
        return;
      }
      String courseCode = courseCodeInput.getText().strip();
      if(courseCode.length() != 3){
        errorLbl.setText("Please enter a 3 character course code! (format: A##)");
        setVisible(errorLbl, true);
        return;
      }
      int teacherID;
      try{
        teacherID = Integer.parseInt(teacherIDInput.getText().strip());
      } catch (NumberFormatException ex) {
        errorLbl.setText("Enter valid integer teacher ID!");
        setVisible(errorLbl, true);
        return;
      }
      CourseRepository.getInstance().add(courseName, courseCode, teacherID);
      errorLbl.setText("Added course: " + courseName);
      setVisible(errorLbl, true);

      courseNameInput.clear();
      courseCodeInput.clear();
      teacherIDInput.clear();
    });
    popupStage.setAlwaysOnTop(true);
    return getScene(base, 0.30);
  }

  public Course(int courseId, String courseName, String courseCode, int teacherId) {
    this.courseId = courseId;
    this.courseName = courseName;
    this.courseCode = courseCode;
    this.teacherId = teacherId;
  }

  public int getCourseId() {
    return courseId;
  }

  public void setCourseId(int courseId) {
    this.courseId = courseId;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public void setCourseCode(String courseCode) {
    this.courseCode = courseCode;
  }

  public int getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(int teacherId) {
    this.teacherId = teacherId;
  }
}
