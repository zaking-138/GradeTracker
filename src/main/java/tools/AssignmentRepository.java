package tools;

import courses.Assignment;
import database.DatabaseManager;
import users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Vartan
 * <br>
 * created:
 * @since 0.1.0
 */

public class AssignmentRepository {
    private final DatabaseManager db = DatabaseManager.getInstance();
    private static AssignmentRepository instance;
    private final List<AssignmentListObserver> observers = new ArrayList<>();

    public static AssignmentRepository getInstance() {
        if (instance == null) {
            instance = new AssignmentRepository();
        }
        return instance;
    }

    public void addObserver(AssignmentListObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(AssignmentListObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(int course_id) {
        List<Assignment> current = db.getAssignmentsByCourse(course_id);
        for (AssignmentListObserver o : observers) {
            o.onAssignmentsChanged(current);
        }
    }

    public void refresh(int course_id) {
        notifyObservers(course_id);
    }

    public void add(String title, String password, double maxPoints, String dueDate, int course_id) {
        db.insertAssignment(title, password, maxPoints, dueDate, course_id);
        notifyObservers(course_id);
    }

    public void delete(int assignmentID, int course_id) {
        db.deleteAssignment(assignmentID);
        notifyObservers(course_id);
    }

    public void update(String title, String description, double maxPoints, String dueDate, int course_id) {
        db.updateAssignment(title, description, maxPoints, dueDate, course_id);
        notifyObservers(course_id);
    }
}
