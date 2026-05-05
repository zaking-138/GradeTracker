package tools;

import courses.Course;
import database.DatabaseManager;
import java.util.ArrayList;
import java.util.List;
import users.User;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class CourseRepository {
  private final DatabaseManager db = DatabaseManager.getInstance();
  private static CourseRepository instance;
  private final List<CourseListObserver> observers = new ArrayList<>();

  public static CourseRepository getInstance() {
    if (instance == null) {
      instance = new CourseRepository();
    }
    return instance;
  }

  public void addObserver(CourseListObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(CourseListObserver observer) {
    observers.remove(observer);
  }

  private void notifyObservers() {
    List<Course> current = db.getAllCourses(true);
    for (CourseListObserver o : observers) {
      o.onCoursesChanged(current);
    }
  }

  public void refresh() {
    notifyObservers();
  }

  public void add(String course_name, String course_code, int teacher_id) {
    db.insertCourse(course_name, course_code, teacher_id);
    notifyObservers();
  }

  public void delete(int course_id) {
    db.deleteCourse(course_id);
    notifyObservers();
  }

  public void update(int courseID, String courseName, String courseCode, int teacherID) {
    db.updateCourse(courseID, courseName, courseCode, teacherID);
    notifyObservers();
  }

}
