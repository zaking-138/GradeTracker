package users;

public class Student extends User {

    public Student(int userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }
}
