/**
 * Test for Login/SignUp scene
 *
 * @author Sebastien
 * @version 0.1.0
 * @since 26/04/2026
 */



import controllers.LoginController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.FutureTask;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    @BeforeAll
    static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already started
        }
    }

    private <T> T runOnFxThread(java.util.concurrent.Callable<T> callable) throws Exception {
        FutureTask<T> task = new FutureTask<>(callable);
        Platform.runLater(task);
        return task.get();
    }

    @Test
    void shouldCreateLoginScene() throws Exception {
        Scene scene = runOnFxThread(() -> LoginController.loginBuild(new Stage()));

        assertNotNull(scene);
        assertNotNull(scene.getRoot());
        assertTrue(scene.getRoot() instanceof VBox);
    }

    @Test
    void shouldContainExpectedFieldsAndButtons() throws Exception {
        Scene scene = runOnFxThread(() -> LoginController.loginBuild(new Stage()));
        VBox root = (VBox) scene.getRoot();

        assertEquals(6, root.getChildren().size());

        assertTrue(root.getChildren().get(0) instanceof Label);
        assertTrue(root.getChildren().get(1) instanceof TextField);
        assertTrue(root.getChildren().get(2) instanceof Label);
        assertTrue(root.getChildren().get(3) instanceof PasswordField);
        assertTrue(root.getChildren().get(4) instanceof Button);
        assertTrue(root.getChildren().get(5) instanceof Button);

        Label usernameLabel = (Label) root.getChildren().get(0);
        TextField usernameInput = (TextField) root.getChildren().get(1);
        Label passwordLabel = (Label) root.getChildren().get(2);
        PasswordField passwordInput = (PasswordField) root.getChildren().get(3);
        Button loginButton = (Button) root.getChildren().get(4);
        Button signUpButton = (Button) root.getChildren().get(5);

        assertEquals("Username: ", usernameLabel.getText());
        assertEquals("Username", usernameInput.getPromptText());
        assertEquals("Password: ", passwordLabel.getText());
        assertEquals("Password", passwordInput.getPromptText());
        assertEquals("LOGIN", loginButton.getText());
        assertEquals("SIGN UP", signUpButton.getText());
    }

    @Test
    void loginButtonShouldNotThrow() throws Exception {
        runOnFxThread(() -> {
            Scene scene = LoginController.loginBuild(new Stage());
            VBox root = (VBox) scene.getRoot();

            TextField usernameInput = (TextField) root.getChildren().get(1);
            PasswordField passwordInput = (PasswordField) root.getChildren().get(3);
            Button loginButton = (Button) root.getChildren().get(4);

            usernameInput.setText("testUser");
            passwordInput.setText("testPass");

            assertDoesNotThrow(loginButton::fire);
            return null;
        });
    }
}