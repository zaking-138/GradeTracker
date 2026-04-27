package database_tests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;


public class UsersTableTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:test.db");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL CHECK(role IN ('ADMIN', 'TEACHER', 'STUDENT'))
                )
            """);

            stmt.execute("DELETE FROM users");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void insertUser() throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, "john");
            psmt.setString(2, "123456");
            psmt.setString(3, "STUDENT");
            int affectedRows = psmt.executeUpdate();
            assertEquals(1, affectedRows);
        }

        String selectUser = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement psmt = connection.prepareStatement(selectUser)) {
            psmt.setString(1, "john");
            ResultSet rs = psmt.executeQuery();

            assertTrue(rs.next());
            assertEquals("john", rs.getString("username"));
            assertEquals("123456", rs.getString("password"));
            assertEquals("STUDENT", rs.getString("role"));
        }
    }

    @Test
    void updateUser() throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, "bob");
            psmt.setString(2, "ABCDEFG");
            psmt.setString(3, "STUDENT");
            psmt.executeUpdate();
        }

        String updateUser = "UPDATE users SET password = ?, role = ? WHERE username = ?";
        try (PreparedStatement psmt = connection.prepareStatement(updateUser)) {
            psmt.setString(1, "ZZZ");
            psmt.setString(2, "ADMIN");
            psmt.setString(3, "bob");
            int affectedRows = psmt.executeUpdate();
            assertEquals(1, affectedRows);
        }

        String selectUser = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement psmt = connection.prepareStatement(selectUser)) {
            psmt.setString(1, "bob");
            ResultSet rs = psmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("ZZZ", rs.getString("password"));
            assertEquals("ADMIN", rs.getString("role"));
        }
    }

    @Test
    void deleteUser() throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, "larry");
            psmt.setString(2, "password");
            psmt.setString(3, "TEACHER");
            psmt.executeUpdate();
        }

        String deleteUser = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement psmt = connection.prepareStatement(deleteUser)) {
            psmt.setString(1, "larry");
            int affectedRows = psmt.executeUpdate();
            assertEquals(1, affectedRows);
        }

        String selectUser = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement psmt = connection.prepareStatement(selectUser)) {
            psmt.setString(1, "larry");
            ResultSet rs = psmt.executeQuery();

            assertFalse(rs.next());
        }
    }
}
