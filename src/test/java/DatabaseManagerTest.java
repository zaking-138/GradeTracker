import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:test_grade_tracker.db");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL CHECK(role IN ('ADMIN', 'TEACHER', 'STUDENT'))
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS courses (
                    course_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    course_name TEXT NOT NULL,
                    course_code TEXT NOT NULL UNIQUE,
                    teacher_id INTEGER NOT NULL,
                    FOREIGN KEY (teacher_id) REFERENCES users(user_id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                )
            """);

            stmt.execute("""
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
                )
            """);

            stmt.execute("""
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
                )
            """);

            stmt.execute("DELETE FROM grades");
            stmt.execute("DELETE FROM assignments");
            stmt.execute("DELETE FROM courses");
            stmt.execute("DELETE FROM users");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private int getUserId(String username) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("user_id") : -1;
        }
    }

    private int getCourseId(String courseCode) throws SQLException {
        String sql = "SELECT course_id FROM courses WHERE course_code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("course_id") : -1;
        }
    }

    private int getAssignmentId(String title) throws SQLException {
        String sql = "SELECT assignment_id FROM assignments WHERE title = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("assignment_id") : -1;
        }
    }

    @Test
    void insertUserTest() throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "alice");
            pstmt.setString(2, "pass123");
            pstmt.setString(3, "STUDENT");
            pstmt.executeUpdate();
        }

        assertTrue(getUserId("alice") > 0);
    }

    @Test
    void updateUserTest() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "bob");
            pstmt.setString(2, "pass123");
            pstmt.setString(3, "STUDENT");
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE users SET role = ? WHERE username = ?")) {
            pstmt.setString(1, "TEACHER");
            pstmt.setString(2, "bob");
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT role FROM users WHERE username = ?")) {
            pstmt.setString(1, "bob");
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("TEACHER", rs.getString("role"));
        }
    }

    @Test
    void deleteUserTest() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "charlie");
            pstmt.setString(2, "pass123");
            pstmt.setString(3, "STUDENT");
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "DELETE FROM users WHERE username = ?")) {
            pstmt.setString(1, "charlie");
            pstmt.executeUpdate();
        }

        assertEquals(-1, getUserId("charlie"));
    }

    @Test
    void insertCourseTest() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "teacher1");
            pstmt.setString(2, "pass123");
            pstmt.setString(3, "TEACHER");
            pstmt.executeUpdate();
        }

        int teacherId = getUserId("teacher1");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO courses (course_name, course_code, teacher_id) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "Database Systems");
            pstmt.setString(2, "CST338");
            pstmt.setInt(3, teacherId);
            pstmt.executeUpdate();
        }

        assertTrue(getCourseId("CST338") > 0);
    }

    @Test
    void updateCourseTest() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "teacher2");
            pstmt.setString(2, "pass123");
            pstmt.setString(3, "TEACHER");
            pstmt.executeUpdate();
        }

        int teacherId = getUserId("teacher2");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO courses (course_name, course_code, teacher_id) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "Old Course");
            pstmt.setString(2, "OLD101");
            pstmt.setInt(3, teacherId);
            pstmt.executeUpdate();
        }

        int courseId = getCourseId("OLD101");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE courses SET course_name = ? WHERE course_id = ?")) {
            pstmt.setString(1, "New Course");
            pstmt.setInt(2, courseId);
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT course_name FROM courses WHERE course_id = ?")) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("New Course", rs.getString("course_name"));
        }
    }

    @Test
    void deleteCourseTest() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "teacher3");
            pstmt.setString(2, "pass123");
            pstmt.setString(3, "TEACHER");
            pstmt.executeUpdate();
        }

        int teacherId = getUserId("teacher3");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO courses (course_name, course_code, teacher_id) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "Delete Me");
            pstmt.setString(2, "DEL101");
            pstmt.setInt(3, teacherId);
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "DELETE FROM courses WHERE course_code = ?")) {
            pstmt.setString(1, "DEL101");
            pstmt.executeUpdate();
        }

        assertEquals(-1, getCourseId("DEL101"));
    }

    @Test
    void insertAssignmentTest() throws SQLException {
        seedTeacherAndCourse();

        int courseId = getCourseId("CST338");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO assignments (title, description, max_points, due_date, course_id) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, "Project 1");
            pstmt.setString(2, "Build the app");
            pstmt.setDouble(3, 100.0);
            pstmt.setString(4, "2026-05-01");
            pstmt.setInt(5, courseId);
            pstmt.executeUpdate();
        }

        assertTrue(getAssignmentId("Project 1") > 0);
    }

    @Test
    void updateAssignmentTest() throws SQLException {
        seedTeacherAndCourse();
        int courseId = getCourseId("CST338");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO assignments (title, description, max_points, due_date, course_id) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, "Project 2");
            pstmt.setString(2, "Old description");
            pstmt.setDouble(3, 100.0);
            pstmt.setString(4, "2026-05-01");
            pstmt.setInt(5, courseId);
            pstmt.executeUpdate();
        }

        int assignmentId = getAssignmentId("Project 2");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE assignments SET title = ? WHERE assignment_id = ?")) {
            pstmt.setString(1, "Project 2 Updated");
            pstmt.setInt(2, assignmentId);
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT title FROM assignments WHERE assignment_id = ?")) {
            pstmt.setInt(1, assignmentId);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("Project 2 Updated", rs.getString("title"));
        }
    }

    @Test
    void deleteAssignmentTest() throws SQLException {
        seedTeacherAndCourse();
        int courseId = getCourseId("CST338");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO assignments (title, description, max_points, due_date, course_id) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, "Delete Assignment");
            pstmt.setString(2, "Temp");
            pstmt.setDouble(3, 50.0);
            pstmt.setString(4, "2026-05-01");
            pstmt.setInt(5, courseId);
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "DELETE FROM assignments WHERE title = ?")) {
            pstmt.setString(1, "Delete Assignment");
            pstmt.executeUpdate();
        }

        assertEquals(-1, getAssignmentId("Delete Assignment"));
    }

    @Test
    void insertGradeTest() throws SQLException {
        seedFullGradeData();

        int studentId = getUserId("student1");
        int assignmentId = getAssignmentId("Homework 1");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO grades (student_id, assignment_id, score, feedback) VALUES (?, ?, ?, ?)")) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, assignmentId);
            pstmt.setDouble(3, 95.0);
            pstmt.setString(4, "Great job");
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT score FROM grades WHERE student_id = ? AND assignment_id = ?")) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, assignmentId);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(95.0, rs.getDouble("score"));
        }
    }

    @Test
    void updateGradeTest() throws SQLException {
        seedFullGradeData();

        int studentId = getUserId("student1");
        int assignmentId = getAssignmentId("Homework 1");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO grades (student_id, assignment_id, score, feedback) VALUES (?, ?, ?, ?)")) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, assignmentId);
            pstmt.setDouble(3, 80.0);
            pstmt.setString(4, "Needs work");
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE grades SET score = ?, feedback = ? WHERE student_id = ? AND assignment_id = ?")) {
            pstmt.setDouble(1, 90.0);
            pstmt.setString(2, "Improved");
            pstmt.setInt(3, studentId);
            pstmt.setInt(4, assignmentId);
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT score, feedback FROM grades WHERE student_id = ? AND assignment_id = ?")) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, assignmentId);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(90.0, rs.getDouble("score"));
            assertEquals("Improved", rs.getString("feedback"));
        }
    }

    @Test
    void deleteGradeTest() throws SQLException {
        seedFullGradeData();

        int studentId = getUserId("student1");
        int assignmentId = getAssignmentId("Homework 1");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO grades (student_id, assignment_id, score, feedback) VALUES (?, ?, ?, ?)")) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, assignmentId);
            pstmt.setDouble(3, 88.0);
            pstmt.setString(4, "Nice");
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "DELETE FROM grades WHERE student_id = ? AND assignment_id = ?")) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, assignmentId);
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM grades WHERE student_id = ? AND assignment_id = ?")) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, assignmentId);
            ResultSet rs = pstmt.executeQuery();
            assertFalse(rs.next());
        }
    }

    private void seedTeacherAndCourse() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "teacher_seed");
            pstmt.setString(2, "pass123");
            pstmt.setString(3, "TEACHER");
            pstmt.executeUpdate();
        }

        int teacherId = getUserId("teacher_seed");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO courses (course_name, course_code, teacher_id) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "Database Systems");
            pstmt.setString(2, "CST338");
            pstmt.setInt(3, teacherId);
            pstmt.executeUpdate();
        }
    }

    private void seedFullGradeData() throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "teacher_seed");
            pstmt.setString(2, "pass123");
            pstmt.setString(3, "TEACHER");
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "student1");
            pstmt.setString(2, "pass123");
            pstmt.setString(3, "STUDENT");
            pstmt.executeUpdate();
        }

        int teacherId = getUserId("teacher_seed");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO courses (course_name, course_code, teacher_id) VALUES (?, ?, ?)")) {
            pstmt.setString(1, "Database Systems");
            pstmt.setString(2, "CST338");
            pstmt.setInt(3, teacherId);
            pstmt.executeUpdate();
        }

        int courseId = getCourseId("CST338");

        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO assignments (title, description, max_points, due_date, course_id) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, "Homework 1");
            pstmt.setString(2, "SQL practice");
            pstmt.setDouble(3, 100.0);
            pstmt.setString(4, "2026-05-10");
            pstmt.setInt(5, courseId);
            pstmt.executeUpdate();
        }
    }
}