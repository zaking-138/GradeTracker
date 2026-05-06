

import static org.junit.jupiter.api.Assertions.*;
import tools.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SessionTest {

    @BeforeEach
    void resetSession() {
        Session.clear();
    }

    @Test
    void setCurrentUser_storesAllValuesCorrectly() {
        Session.setCurrentUser(42, "alice", "STUDENT");

        assertEquals(42, Session.getCurrentUserId());
        assertEquals("alice", Session.getCurrentUsername());
        assertEquals("STUDENT", Session.getCurrentRole());
    }

    @Test
    void clear_resetsSessionValues() {
        Session.setCurrentUser(99, "bob", "TEACHER");

        Session.clear();

        assertEquals(-1, Session.getCurrentUserId());
        assertNull(Session.getCurrentUsername());
        assertNull(Session.getCurrentRole());
    }

    @Test
    void setCurrentUser_overwritesPreviousValues() {
        Session.setCurrentUser(1, "firstUser", "ADMIN");
        Session.setCurrentUser(2, "secondUser", "STUDENT");

        assertEquals(2, Session.getCurrentUserId());
        assertEquals("secondUser", Session.getCurrentUsername());
        assertEquals("STUDENT", Session.getCurrentRole());
    }
}