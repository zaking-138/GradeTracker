package courses;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class Assignment {
  private int assignmentId;
  private String title;
  private String description;
  private int maxPoints;
  private int dueDate;
  private int courseId;

  public Assignment(int assignmentId, String title, String description, int maxPoints, int dueDate,
      int courseId) {
    this.assignmentId = assignmentId;
    this.title = title;
    this.description = description;
    this.maxPoints = maxPoints;
    this.dueDate = dueDate;
    this.courseId = courseId;
  }

  public int getAssignmentId() {
    return assignmentId;
  }

  public void setAssignmentId(int assignmentId) {
    this.assignmentId = assignmentId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getMaxPoints() {
    return maxPoints;
  }

  public void setMaxPoints(int maxPoints) {
    this.maxPoints = maxPoints;
  }

  public int getDueDate() {
    return dueDate;
  }

  public void setDueDate(int dueDate) {
    this.dueDate = dueDate;
  }

  public int getCourseId() {
    return courseId;
  }

  public void setCourseId(int courseId) {
    this.courseId = courseId;
  }
}
