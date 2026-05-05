package tools;

import courses.Assignment;

import java.util.List;

/**
 * @author Adam Vartan
 * <br>
 * created:
 * @since 0.1.0
 */
public interface AssignmentListObserver {
    void onAssignmentsChanged(List<Assignment> assignments);
}
