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
        String sql = "UPDATE courses SET course_name = ?, course_code = ?, teacher_id = ? WHERE course_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, course_name);
            pstmt.setString(2, course_code);
            pstmt.setInt(3, teacher_id);
            pstmt.setInt(4, course_id);
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("updateCourse failed: " + e.getMessage());
        }
    }

    public void updateUser(int user_id, String username, String password, String role){
        String sql =  "UPDATE users SET username = ?, password = ?, role = ? WHERE user_id = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
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

    public List<String> getCoursesByTeacher(int teacherId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT course_code, course_name FROM courses WHERE teacher_id = ? ORDER BY course_code";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, teacherId);
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

    public List<String> getAssignmentsByCourse(int courseId) {
        List<String> list = new ArrayList<>();
        String sql = " SELECT title, due_date, max_points FROM assignments WHERE course_id = ? ORDER BY due_date ";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
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

    public List<String> getGradesForStudent(int studentId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT assignments.title, grades.score, grades.feedback FROM grades JOIN assignments ON grades.assignment_id = assignments.assignment_id WHERE grades.student_id = ? ORDER BY assignments.title";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
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

    public List<String> getGradesForAssignment(int assignmentId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT users.username, grades.score, grades.feedback FROM grades JOIN users ON grades.student_id = users.user_id WHERE grades.assignment_id = ? ORDER BY users.username";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, assignmentId);
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