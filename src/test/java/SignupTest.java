import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import tools.SceneManager;
import controllers.LoginController;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

/**
 * [Brief one-sentence desciption of what this class does.]
 *
 * @author Sebastien
 * @version 0.1.0
 * @since 04/05/2026
 */
public class SignupTest extends ApplicationTest{
    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);

        Scene scene = LoginController.loginBuild(stage);
        stage.setScene(scene);
        stage.setTitle("TestFX Login Page");
        stage.show();
        stage.toFront();
    }

    @Test
    void typeUsernameAndPasswordThenShowPassword() {

        clickOn("#usernameField");
        write("test");

        clickOn("#passwordField");
        write("123456789");

        clickOn("#showPasswordButton");

        verifyThat("password: 123456789", hasText("password: 123456789"));
        verifyThat("Hide Password", hasText("Hide Password"));
    }

    @Test
    void typeUsernameAndPasswordThenClickSignUp() {
        clickOn("#signUpButton");

        clickOn("#usernameField");
        write("wrongUser");

        clickOn("#passwordField");
        write("wrongPassword");

        clickOn("#confirmPasswordField");
        write("wrongPassword");

        clickOn("#signUpButton");

        verifyThat("Select a role!", hasText("Select a role!"));
    }

    @Test
    void clickSignUpWithoutFillingFields() {
        clickOn("#signUpButton");

        clickOn("#signUpButton");

        verifyThat("Invalid username! Must be of length 5 or greater.", hasText("Invalid username! Must be of length 5 or greater."));
    }

    @Test
    void SuccessfulSignUp() {
        clickOn("#signUpButton");

        clickOn("#usernameField");
        write("tests");

        clickOn("#passwordField");
        write("hashcode");

        clickOn("#confirmPasswordField");
        write("hashcode");

        clickOn("#rolesButton");
        clickOn("Student");

        clickOn("#signUpButton");
        sleep(1000);
        verifyThat("Student Dashboard", hasText("Student Dashboard"));
        sleep(1000);
    }
}