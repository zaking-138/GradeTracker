import static tools.Helpers.getClassString;
import static tools.Helpers.getGradeString;
import static tools.Helpers.parseClasses;
import static tools.Helpers.parseGrades;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
class ZachKingTests {

  int numClasses = 0;
  String alphabet = "";
  Random random;

  @BeforeEach
  void setUp() {
    numClasses = 5;
    alphabet = "ABCDEF";
    random = new Random();
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void HelpersGradesTests() {
    final ArrayList<Character> expected = new ArrayList<>();
    for (int i = 0; i < numClasses; i++) {
      expected.add(alphabet.charAt(random.nextInt(alphabet.length())));
    }

    String gradeString = getGradeString(expected);
    ArrayList<Character> result = parseGrades(gradeString, numClasses);

    for (int i = 0; i < numClasses; i++) {
      assert (expected.get(i).equals(result.get(i)));
      System.out.println(expected.get(i) + " = " + result.get(i));
    }

  }

  @Test
  void HelpersClassCodesTests() {
    StringBuilder temp = new StringBuilder();
    final ArrayList<String> expected01 = new ArrayList<>();
    for (int i = 0; i < numClasses; i++) {
      temp.append(alphabet.charAt(random.nextInt(alphabet.length())));
      temp.append(random.nextInt(10, 100));
      expected01.add(temp.toString());
      temp.setLength(0);
    }

    String classCodeString = getClassString(expected01);
    ArrayList<String> result01 = parseClasses(classCodeString, numClasses);

    for (int i = 0; i < numClasses; i++) {
      assert (expected01.get(i).equals(result01.get(i)));
      System.out.println(expected01.get(i) + " = " + result01.get(i));
    }
  }
}
