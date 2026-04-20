package tools;

import java.util.Arrays;
import java.util.HashMap;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Zachary King
 * <br>
 * created: 04/18/2026
 * @since 0.1.0
 */
public class SceneFactory {
  public static final int CLASSCODE_LENGTH = 3;

  public static Scene create(SceneType type, Stage stage){
    return switch (type){
      case LOGIN -> loginBuild(stage);

      case ADMIN_DASH -> adminDashBuild(stage);
      case ADMIN_USERLIST -> adminUserlistBuild(stage);

      case PROF_DASH -> profDashBuild(stage);
      case PROF_USERLIST -> profUserlistBuild(stage);
      case PROF_GRDBK -> profGrdBkBuild(stage);

      case STDNT_DASH -> stdntDashBuild(stage);
      case STDNT_GRDBK -> stdntGrdBkBuild(stage);
    };
  }


  // ── Helper functions ──────────────────────────────────────────────

  /**
   * Get width & height of screen multiplied by the default scale.
   * @return a HashMap with entries "w" for width & "h" for height.
   */
  private static HashMap<String, Double> getScreenSize(){
    double defaultVal = 0.85;
    return getScreenSize(defaultVal);
  }

  /**
   * Get width & height of screen multiplied by the provided scale.
   * @return a HashMap with entries "w" for width & "h" for height.
   */
  private static HashMap<String, Double> getScreenSize(Double scale){
    double width = Screen.getPrimary().getBounds().getWidth() * scale;
    double height = Screen.getPrimary().getBounds().getHeight() * scale;
    HashMap<String, Double> rtrnMap = new HashMap<>();
    rtrnMap.put("w", width);
    rtrnMap.put("h", height);
    return rtrnMap;
  }

  /**
   * Parse individual characters from a string of grades numClasses long.
   * @param gradeString a String of uppercase letters returned from the database.
   * @param numClasses an integer that should equal the length of the String.
   * @return a char array of size numClasses, containing the individual grades.
   */
  private static char[] parseGrades(String gradeString, int numClasses){
    assert (gradeString.length() == numClasses);
    char[] grades = new char[numClasses];
    for(int i = 0; i < numClasses; i++){
      grades[i] = gradeString.charAt(i);
    }
    return grades;
  }

  /**
   * Parse individual class codes of length 3 from a string numClasses long.
   * @param classCodeString a String of class codes using format 'A##'.
   * @param numClasses an integer that should be 1/3 the length of classCodeString.
   * @return a String array of size numClasses, containing the individual class codes.
   */
  private static String[] parseClasses(String classCodeString, int numClasses){
    assert (classCodeString.length() == CLASSCODE_LENGTH * numClasses);
    String[] classCodes = new String[numClasses];
    int counter = 0;
    for (int i = 0; i < numClasses; i++){
      StringBuilder tempStr = new StringBuilder();
      int tempInt = counter;
      for (int j = tempInt; j < tempInt + CLASSCODE_LENGTH; j++) {
        tempStr.append(classCodeString.charAt(counter));
        counter++;
      }
      classCodes[i] = tempStr.toString();
    }
    return classCodes;
  }

  /**
   * Change given grade array at position classNumber to be the provided grade character.
   * @param grades a char[] of grades.
   * @param classNumber an int representing the position of the grade to edit.
   * @param grade a char, the new grade.
   * @return the original char[].
   */
  private static char[] editGrade(char[] grades, int classNumber, char grade){
    assert (classNumber < grades.length);
    grades[classNumber] = grade;
    return grades;
  }

  /**
   * Change given classes array at position classNumber to be the provided class code String.
   * @param classes a String[] of class codes.
   * @param classNumber an int representing the position of the class code to edit.
   * @param classCode a String, the new class code.
   * @return the original String[].
   */
  private static String[] editClass(String[] classes, int classNumber, String classCode){
    assert (classNumber * CLASSCODE_LENGTH < classes.length);
    assert (classCode.length() == CLASSCODE_LENGTH);
    classes[classNumber] = classCode;
    return classes;
  }

  /**
   * For convenience, what to add into the database when editing grades.
   * @param grades a char[] of grades.
   * @return the char[] as a String.
   */
  private static String getGradeString(char[] grades){
    StringBuilder temp = new StringBuilder();
    for(char grade : grades){
      temp.append(grade);
    }
    return temp.toString();
  }

  /**
   * For convenience, what to add into the database when editing classes.
   * @param classes a String[] of classes.
   * @return the String[] as a String.
   */
  private static String getClassString(String[] classes){
    StringBuilder temp = new StringBuilder();
    for(String classCode : classes){
      temp.append(classCode);
    }
    return temp.toString();
  }


  // ── Scene builders ──────────────────────────────────────────────

  private static Scene loginBuild(Stage stage) {

    // ── TEST CODE ──────────────────────────────────────────────
    // ── DELETE IF NEEDED ───────────────────────────────────────
    char[] mes1 = parseGrades("ABDC", "ABDC".length());
    String stringMes1 = getGradeString(mes1);
    editGrade(mes1, 2, 'F');
    String stringMes2 = getGradeString(mes1);

    String[] classes1 = parseClasses("M12N34L56", "M12N34L56".length()/3);
    String stringClasses1 = getClassString(classes1);
    editClass(classes1, 1, "J69");
    String stringClasses2 = getClassString(classes1);

    BorderPane base = new BorderPane();
    Text message = new Text("Sign In:\n[username]\n[password]\n\n[[login]]\n"
        + stringMes1 + "-->" + stringMes2 + "\n\n"
        + stringClasses1 + "-->" + stringClasses2);
    message.setFont(new Font(20));
    message.setTextAlignment(TextAlignment.CENTER);
    base.setCenter(message);
    return new Scene(base, getScreenSize().get("w"), getScreenSize().get("h"));
    // ── TEST CODE ──────────────────────────────────────────────
    // ── ./gradlew run ──────────────────────────────────────────


    //TODO
//    return null;
  }

  private static Scene adminDashBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene adminUserlistBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene profDashBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene profUserlistBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene profGrdBkBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene stdntDashBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene stdntGrdBkBuild(Stage stage) {
    //TODO
    return null;
  }

}
