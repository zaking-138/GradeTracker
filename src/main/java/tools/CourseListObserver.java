package tools;

import courses.Course;
import java.util.List;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public interface CourseListObserver {
  void onCoursesChanged(List<Course> courses);
}
