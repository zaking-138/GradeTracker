package users;

public class Teacher extends User {

    public Teacher(int userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public String getRole() {
        return "TEACHER";
    }
}
