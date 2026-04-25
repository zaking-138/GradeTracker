package tools;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.stage.Screen;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class Helpers {
  public static final int CLASSCODE_LENGTH = 3;
  
  // ── Helper functions ──────────────────────────────────────────────

  /**
   * Get width & height of screen multiplied by the default scale.
   * @return a HashMap with entries "w" for width & "h" for height.
   */
   public static HashMap<String, Double> getScreenSize(){
    double defaultVal = 0.85;
    return getScreenSize(defaultVal);
  }

  /**
   * Get width & height of screen multiplied by the provided scale.
   * @return a HashMap with entries "w" for width & "h" for height.
   */
   static HashMap<String, Double> getScreenSize(Double scale){
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
   public static ArrayList<Character> parseGrades(String gradeString, int numClasses){
    assert (gradeString.length() == numClasses);
    ArrayList<Character> grades = new ArrayList<>();
    for(int i = 0; i < numClasses; i++){
      grades.add(i, gradeString.charAt(i));
    }
    return grades;
  }

  /**
   * Parse individual class codes of length 3 from a string numClasses long.
   * @param classCodeString a String of class codes using format 'A##'.
   * @param numClasses an integer that should be 1/3 the length of classCodeString.
   * @return a String array of size numClasses, containing the individual class codes.
   */
   public static ArrayList<String> parseClasses(String classCodeString, int numClasses){
    assert (classCodeString.length() == CLASSCODE_LENGTH * numClasses);
    ArrayList<String> classCodes = new ArrayList<>();
    int counter = 0;
    for (int i = 0; i < numClasses; i++){
      StringBuilder tempStr = new StringBuilder();
      int tempInt = counter;
      for (int j = tempInt; j < tempInt + CLASSCODE_LENGTH; j++) {
        tempStr.append(classCodeString.charAt(counter));
        counter++;
      }
      classCodes.add(i, tempStr.toString());
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
   public static void editGrade(ArrayList<Character> grades, int classNumber, char grade){
    assert (classNumber < grades.size());
    grades.remove(classNumber);
    grades.add(classNumber, grade);
  }

  /**
   * Change given classes array at position classNumber to be the provided class code String.
   * @param classes a String[] of class codes.
   * @param classNumber an int representing the position of the class code to edit.
   * @param classCode a String, the new class code.
   * @return the original String[].
   */
   public static void editClass(ArrayList<String> classes, int classNumber, String classCode){
//    assert (classNumber * CLASSCODE_LENGTH < classes.size());
//    assert (classCode.length() == CLASSCODE_LENGTH);
    classes.remove(classNumber);
    classes.add(classNumber, classCode);
  }

  public static void addClassToEnd(ArrayList<String> classes, String classCode){
     assert(classCode.length() == CLASSCODE_LENGTH);
     classes.add(classCode);
  }

  public static void addGradeToEnd(ArrayList<Character> grades, Character grade){
     grades.add(grade);
  }

  /**
   * For convenience, what to add into the database when editing grades.
   * @param grades a char[] of grades.
   * @return the char[] as a String.
   */
   public static String getGradeString(ArrayList<Character> grades){
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
   public static String getClassString(ArrayList<String> classes){
    StringBuilder temp = new StringBuilder();
    for(String classCode : classes){
      temp.append(classCode);
    }
    return temp.toString();
  }

}
