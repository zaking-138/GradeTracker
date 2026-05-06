package database;

import courses.Assignment;
import courses.Course;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Manages the SQLite connection and all CRUD operations for the items table.
 *
 * One instance is created in Main.java and passed to SceneFactory.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import users.Admin;
import users.Student;
import users.Teacher;
import users.User;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:grade_tracker.db";
    private Connection connection;
    private static DatabaseManager instance;
    private static String currentUser = null;

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String currentUser) {
        DatabaseManager.currentUser = currentUser;
    }

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected.");

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }

            createUsersTable();
            createCoursesTable();
            createAssignmentsTable();
            createGradesTable();

        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL CHECK(role IN ('ADMIN', 'TEACHER', 'STUDENT'))
                );
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("createUsersTable failed: " + e.getMessage());
        }
    }

    private void createCoursesTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS courses (
                    course_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    course_name TEXT NOT NULL,
                    course_code TEXT NOT NULL UNIQUE,
                    teacher_id INTEGER NOT NULL,
                    FOREIGN KEY (teacher_id) REFERENCES users(user_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("createCoursesTable failed: " + e.getMessage());
        }
    }

    private void createAssignmentsTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS assignments (
                    assignment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    description TEXT,
                    max_points REAL NOT NULL,
                    due_date TEXT,
                    course_id INTEGER NOT NULL,
                    FOREIGN KEY (course_id) REFERENCES courses(course_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("createAssignmentsTable failed: " + e.getMessage());
        }
    }

    private void createGradesTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS grades (
                    grade_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id INTEGER NOT NULL,
                    assignment_id INTEGER NOT NULL,
                    score REAL NOT NULL,
                    feedback TEXT,
                    UNIQUE(student_id, assignment_id),
                    FOREIGN KEY (student_id) REFERENCES users(user_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE,
                    FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("createGradesTable failed: " + e.getMessage());
        }
    }

    public void insertUser(String username, String password, String role){
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("insertItem failed: " + e.getMessage());
        }
    }

    public void insertCourse(String course_name, String course_code, int teacher_id){
        String sql = "INSERT INTO courses (course_name, course_code, teacher_id) VALUES (?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, course_name);
            pstmt.setString(2, course_code);
            pstmt.setInt(3, teacher_id);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("insertCourse failed: " + e.getMessage());
        }
    }

    public void insertGrades(int student_id, int assignment_id, double score, String feedback){
        String sql = "INSERT INTO grades (student_id, assignment_id, score, feedback) VALUES (?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, student_id);
            pstmt.setInt(2, assignment_id);
            pstmt.setDouble(3, score);
            pstmt.setString(4, feedback);
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("insertGrades failed: " + e.getMessage());
        }
    }

    public void insertAssignment(String title, String description, double maxpoints, String duedate, int course_id){
        String sql = "INSERT INTO assignments (title, description, max_points, due_date, course_id) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setDouble(3, maxpoints);
            pstmt.setString(4, duedate);
            pstmt.setInt(5, course_id);
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.printf("insertAssignment failed: " + e.getMessage());
        }
    }

    public void deleteUser(String username){
        String sql = "DELETE FROM users WHERE username = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("deleteUser failed: " + e.getMessage());
        }
    }

    public void deleteCourse(int course_id){
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, course_id);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("deleteCourse failed: " + e.getMessage());
        }
    }

    public void deleteGrades(int student_id, int assignment_id){
        String sql = "DELETE FROM grades WHERE student_id = ? AND assignment_id = ?";
        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setInt(1, student_id);
            psmt.setInt(2, assignment_id);
            psmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("deleteGrades failed: " + e.getMessage());
        }
    }

    public void deleteAssignment(int assignment_id){
        String sql = "DELETE FROM assignments WHERE assignment_id = ?";
        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setInt(1, assignment_id);
            psmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("deleteAssignment failed: " + e.getMessage());
        }
    }

    public void updateAssignment(String title, String description, double maxpoints, String duedate, int course_id){
        String sql = "UPDATE assignments SET title = ?, description = ?, max_points = ?, due_date = ? WHERE course_id = ?";
        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setString(1, title);
            psmt.setString(2, description);
            psmt.setDouble(3, maxpoints);
            psmt.setString(4, duedate);
            psmt.setInt(5, course_id);
            psmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("updateAssignment failed: " + e.getMessage());
        }
    }

    public void updateGrades(int student_id, int assignment_id, double score, String feedback){
        String sql = "UPDATE grades SET score = ?, feedback = ? WHERE student_id = ? AND assignment_id = ?";
        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setDouble(1, score);
            psmt.setString(2, feedback);
            psmt.setInt(3, student_id);
            psmt.setInt(4, assignment_id);
            psmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("updateGrades failed: " + e.getMessage());
        }
    }

    public void updateCourse(int course_id, String course_name, String course_code, int teacher_id){
        var temp = getAllCourses(true);
        Course oldInfo = null;
        for(Course c : temp){
            if (c.getCourseId() == course_id){
                oldInfo = c;
                break;
            }
        }
        String sql = "UPDATE courses SET course_name = ?, course_code = ?, teacher_id = ? WHERE course_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            if(!course_name.isEmpty()){
                pstmt.setString(1, course_name);
            }else{
                pstmt.setString(1, oldInfo.getCourseName());
            }
            if(!course_code.isEmpty()){
                pstmt.setString(2, course_code);
            }else{
                pstmt.setString(2, oldInfo.getCourseCode());
            }
            if(teacher_id >= 0){
                pstmt.setInt(3, teacher_id);
            }else{
                pstmt.setInt(3, oldInfo.getTeacherId());
            }
            pstmt.setInt(4, course_id);
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("updateCourse failed: " + e.getMessage());
        }
    }

    public void deleteUser(int user_id) {
        Map<String, String> oldInfo = getUser(user_id);
        deleteUser(oldInfo.get("username"));
    }

    public void updateUser(int user_id, String username, String password, String role){
        Map<String, String> oldInfo = getUser(user_id);
        String sql =  "UPDATE users SET username = ?, password = ?, role = ? WHERE user_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            if(!username.isEmpty()){
                pstmt.setString(1, username);
            }else{
                pstmt.setString(1, oldInfo.get("username"));
            }
            if (!password.isEmpty()) {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                pstmt.setString(2, hashedPassword);
            } else {
                pstmt.setString(2, oldInfo.get("password"));
            }
            if(!role.isEmpty()){
                pstmt.setString(3, role);
            }else{
                pstmt.setString(3, oldInfo.get("role"));
            }
            pstmt.setInt(4, user_id);
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("updateUser failed: " + e.getMessage());
        }
    }

    public boolean validateUser(String username, String password){
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";
        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setString(1, username);
            psmt.setString(2, password);
            ResultSet rs = psmt.executeQuery();
            return rs.next();
        }
        catch (SQLException e){
            System.out.println("validateUser failed: " + e.getMessage());
            return false;
        }
    }

    public String getUserRole(String username){
        String sql = "SELECT role FROM users WHERE username = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        }
        catch (SQLException e){
            System.out.println("getUserRole failed: " + e.getMessage());
        }
        return null;
    }

    public List<String> getAllCourses() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT course_code, course_name FROM courses ORDER BY course_code";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getString("course_code") + " - " + rs.getString("course_name");
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("getAllCourses failed: " + e.getMessage());
        }
        return list;
    }

    public List<String> getCoursesByTeacher(int teacher_id) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT course_code, course_name FROM courses WHERE teacher_id = ? ORDER BY course_code";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, teacher_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getString("course_code") + " - " + rs.getString("course_name");
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("getCoursesByTeacher failed: " + e.getMessage());
        }
        return list;
    }

    public List<String> getAssignmentsByCourse(int course_id) {
        List<String> list = new ArrayList<>();
        String sql = " SELECT title, due_date, max_points FROM assignments WHERE course_id = ? ORDER BY due_date ";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, course_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getString("title") + " | Due: " + rs.getString("due_date") + " | Max Points: " + rs.getDouble("max_points");
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("getAssignmentsByCourse failed: " + e.getMessage());
        }
        return list;
    }

    public List<Assignment> getAssignmentsByCourse(int course_id, boolean asAssignmentObj) {
        List<Assignment> list = new ArrayList<>();
        String sql = " SELECT assignment_id, title, due_date, max_points, course_id, description " +
                "FROM assignments WHERE course_id = ? ORDER BY due_date ";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, course_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Assignment(rs.getInt("assignment_id"), rs.getString("title"),
                        rs.getString("description"), rs.getInt("max_points"),
                        rs.getInt("due_date"), rs.getInt("course_id")));
            }
        } catch (SQLException e) {
            System.out.println("getAssignmentsByCourse failed: " + e.getMessage());
        }
        return list;
    }

    public List<String> getGradesForStudent(int student_id) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT assignments.title, grades.score, grades.feedback FROM grades JOIN assignments ON grades.assignment_id = assignments.assignment_id WHERE grades.student_id = ? ORDER BY assignments.title";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, student_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getString("title") + " | Score: " + rs.getDouble("score") + " | Feedback: " + rs.getString("feedback");
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("getGradesForStudent failed: " + e.getMessage());
        }
        return list;
    }

    public List<String> getGradesForAssignment(int assignment_id) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT users.username, grades.score, grades.feedback FROM grades JOIN users ON grades.student_id = users.user_id WHERE grades.assignment_id = ? ORDER BY users.username";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, assignment_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getString("username") + " | Score: " + rs.getDouble("score") + " | Feedback: " + rs.getString("feedback");
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("getGradesForAssignment failed: " + e.getMessage());
        }
        return list;
    }

    public List<String> getAllStudents() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT user_id, username FROM users WHERE role = 'STUDENT' ORDER BY username";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getInt("user_id") + " - " + rs.getString("username");
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("getAllStudents failed: " + e.getMessage());
        }
        return list;
    }

    public ObservableList<String> getCoursesByTeacherObservable(int teacher_id){
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT course_id, course_code, course_name FROM courses WHERE teacher_id = ? ORDER BY course_code";
        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setInt(1, teacher_id);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                String row = rs.getInt("course_id") + " | "
                        + rs.getString("course_code") + " - "
                        + rs.getString("course_name");
                list.add(row);
            }
        } catch (SQLException e){
            System.out.println("getCoursesByTeacherObservable failed: " + e.getMessage());
        }
        return list;
    }

    public ObservableList<String> getAssignmentsByCourseObservable(int course_id){
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT title, description, max_points, due_date FROM assignments WHERE course_id = ? ORDER BY due_date";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, course_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getString("title") +
                        " | Description: " + rs.getString("description") +
                        " | Max Points: " + rs.getDouble("max_points") +
                        " | Due: " + rs.getDate("due_date");
                list.add(row);
            }
        }catch (SQLException e){
            System.out.println("getAssignmentsByCourse failed: " + e.getMessage());
        }
        return list;
    }

    public ObservableList<String> getGradesByStudentObservable(int student_id){
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT assignments.title, grades.score, grades.feedback FROM grades JOIN assignments ON grades.assignment_id = assignments.assignment_id WHERE grades.student_id = ? ORDER BY assignments.due_date";
        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            psmt.setInt(1, student_id);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                String row = rs.getString("title") +
                        " | Score: " + rs.getDouble("score") + " | Feedback: " +
                        rs.getString("feedback");
                list.add(row);
            }
        }catch (SQLException e){
            System.out.println("getAssignmentsByStudent failed: " + e.getMessage());
        }
        return list;
    }

    public ObservableList<String> getGradesByAssignmentObservable(int assignment_id){
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT users.username, grades.score, grades.feedback FROM grades JOIN users ON grades.student_id = users.user_id WHERE grades.assignment_id = ? ORDER BY users.username";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, assignment_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getString("username") +
                        " | Score: " + rs.getDouble("score") + " | Feedback: " +
                        rs.getString("feedback");
                list.add(row);
            }
        }catch (SQLException e){
            System.out.println("getAssignmentsByAssignment failed: " + e.getMessage());
        }
        return list;
    }

    public int getUserIDByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        catch(SQLException e){
            System.out.println("getUserIDByUsername failed: " + e.getMessage());
        }
        return -1;
    }

    public ObservableList<String> getAllCoursesObservable(){
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT course_id, course_code,  course_name FROM courses ORDER BY course_code";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getInt("course_id") + " | " +
                        rs.getString("course_code") + " | " +
                        rs.getString("course_name");
                list.add(row);
            }
        }
        catch (SQLException e){
            System.out.println("getAllCoursesObservable failed: " + e.getMessage());
        }
        return list;
    }

    public Map<String, String> getUser(int user_id) {
        Map<String, String> userInfo = new HashMap<>();
        String sql = "SELECT username, password, role FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            userInfo.put("username", rs.getString("username"));
            userInfo.put("password", rs.getString("password"));
            userInfo.put("role", rs.getString("role"));
        } catch (SQLException e) {
            System.out.println("getAllStudents failed: " + e.getMessage());
        }
        return userInfo;
    }

    public boolean authenticateUser(String username, String plainPassword) {
        String sql = "SELECT password FROM users WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                return BCrypt.checkpw(plainPassword, storedHashedPassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> getAllUsers() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT user_id, username, password, role FROM users ORDER BY user_id";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String row = rs.getInt("user_id") + "\t" + rs.getString("username")
                    + "  \t" + rs.getString("role") + "\t\t" + rs.getString("password");
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("getAllUsers failed: " + e.getMessage());
        }
        return list;
    }

    public List<User> getAllUsers(boolean asUserObject) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, username, password, role FROM users ORDER BY user_id";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                switch(rs.getString("role")){
                    case "STUDENT":
                        list.add(new Student(rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password")
                        ));
                        break;
                    case "TEACHER":
                        list.add(new Teacher(rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password")
                        ));
                        break;
                    case "ADMIN":
                        list.add(new Admin(rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password")
                        ));
                        break;
                    default:
                        System.out.println("WHAT THE HELL");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println("getAllUsers failed: " + e.getMessage());
        }
        return list;
    }

    public List<Course> getAllCourses(boolean asCourseObject) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT course_id, course_name, course_code, teacher_id FROM courses ORDER BY course_id";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Course(rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getString("course_code"),
                    rs.getInt("teacher_id")
                ));

            }
        } catch (SQLException e) {
            System.out.println("getAllCourses as course objects failed: " + e.getMessage());
        }
        return list;
    }


    public void close() throws SQLException {
        try{
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}