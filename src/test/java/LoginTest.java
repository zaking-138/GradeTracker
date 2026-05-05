import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import tools.SceneManager;
import controllers.LoginController;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class LoginTest extends ApplicationTest {

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
        sleep(1000);

        clickOn("#usernameField");
        write("test");
        sleep(800);

        clickOn("#passwordField");
        write("123456789");
        sleep(800);

        clickOn("#showPasswordButton");
        sleep(1000);

        verifyThat("password: 123456789", hasText("password: 123456789"));
        verifyThat("Hide Password", hasText("Hide Password"));
    }

    @Test
    void typeUsernameAndPasswordThenClickLogin() {
        sleep(1000);

        clickOn("#usernameField");
        write("wrongUser");
        sleep(800);

        clickOn("#passwordField");
        write("wrongPassword");
        sleep(800);

        clickOn("#loginButton");
        sleep(1000);

        verifyThat("Invalid username or password.", hasText("Invalid username or password."));
    }

    @Test
    void clickLoginWithoutFillingFields() {
        sleep(1000);

        clickOn("#loginButton");
        sleep(1000);

        verifyThat("Please fill in all fields.", hasText("Please fill in all fields."));
    }

    @Test
    void SuccessfulLogin() {
        sleep(1000);

        clickOn("#usernameField");
        write("testa");
        sleep(800);

        clickOn("#passwordField");
        write("hashcode");
        sleep(800);

        clickOn("#loginButton");
        sleep(1000);

        verifyThat("Open full userlist: ", hasText("Open full userlist: "));
    }
}