package tools;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseManager;
/**
 * @author Adam Vartan
 * <br>
 * created:
 * @since 0.1.0
 */

public class UserListRepository {
    private final DatabaseManager db = DatabaseManager.getInstance();
    private final List<UserListObserver> observers = new ArrayList<>();

    public void addObserver(UserListObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(UserListObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for(UserListObserver o : observers) {
            o.refresh();
        }
    }

    public void add(String title, String password, String role) {
        db.insertUser(title, password, role);
        notifyObservers();
    }

    public void delete(String title) {
        db.deleteUser(title);
        notifyObservers();
    }

    public void update(int user_id, String title, String password, String role) {
        db.updateUser(user_id, title, password, role);
        notifyObservers();
    }
}
