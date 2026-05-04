package tools;

public class Session {
    private static int currentUserId = -1;
    private static String currentUsername;
    private static String currentRole;

    public static void setCurrentUser(int userId, String username, String role) {
        currentUserId = userId;
        currentUsername = username;
        currentRole = role;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }
    public static String getCurrentUsername() {
        return currentUsername;
    }
    public static String getCurrentRole() {
        return currentRole;
    }

    public static void clear(){
        currentUserId = -1;
        currentUsername = null;
        currentRole = null;
    }
}
