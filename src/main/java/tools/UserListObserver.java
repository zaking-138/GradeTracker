package tools;

import java.util.List;
import users.User;

/**
 * @author Adam Vartan
 * <br>
 * created:
 * @since 0.1.0
 */
public interface UserListObserver {
    void onUsersChanged(List<User> todos);
}
