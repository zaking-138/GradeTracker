import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the SQLite connection and all CRUD operations for the items table.
 *
 * One instance is created in Main.java and passed to SceneFactory.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:grade_tracker.db";
    private Connection connection;
    private static DatabaseManager instance;

    static void main() {
        DatabaseManager.getInstance();
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

    public void insertCourse(String course_name, String course_code, Integer teacher_id){
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

    public void insertGrades(String student_id, String assignment_id, Integer score, String feedback){
        String sql = "INSERT INTO grades (student_id, assignment_id, score, feedback) VALUES (?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, student_id);
            pstmt.setString(2, assignment_id);
            pstmt.setInt(3, score);
            pstmt.setString(4, feedback);
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("insertGrades failed: " + e.getMessage());
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

    public void deleteCourse(String course_id){
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, course_id);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("deleteCourse failed: " + e.getMessage());
        }
    }

    public void deleteGrades(String student_id, String assignment_id){
        String sql = "DELETE FROM grades WHERE student_id = ? AND assignment_id = ?";
        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, student_id);
            psmt.setString(2, assignment_id);
            psmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("deleteGrades failed: " + e.getMessage());
        }
    }

    /**
     * Could be a possible observable list for the return here
     * @return
     */
    public List<String> getAllCourses(){
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_code";
        try(PreparedStatement pstm = connection.prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("course_code"));
            }
        }catch (SQLException e){
            System.out.println("getAllCourses failed: " + e.getMessage());
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