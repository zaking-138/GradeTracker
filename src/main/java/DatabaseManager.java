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

    public void close() throws SQLException {
        if (!connection.isClosed()){
            connection.close();
        }
    }
}