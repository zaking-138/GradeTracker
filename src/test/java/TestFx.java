import controllers.LoginController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import tools.SceneManager;
import tools.SceneType;

import java.lang.reflect.Field;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

/**
 * TestFX tests for the main scene transitions in the application.
 *
 * This class is intentionally inspired by LoginTest and SignupTest: it starts
 * on the login scene, clicks visible UI buttons, then verifies that the target
 * scene is displayed.
 */
public class TestFx extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        resetSceneManager();
        SceneManager.init(stage);

        Scene scene = LoginController.loginBuild(stage);
        stage.setScene(scene);
        stage.setTitle("TestFX Scene Transitions");
        stage.show();
        stage.toFront();
    }

    private void resetSceneManager() throws Exception {
        Field instance = SceneManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    private void goTo(SceneType sceneType) {
        interact(() -> SceneManager.getInstance().navigateTo(sceneType, true));
        sleep(300);
    }

    @Test
    void loginToSignupThenBackToLogin() {
        clickOn("#signUpButton");
        verifyThat("Sign Up:", hasText("Sign Up:"));

        clickOn("#loginButton");
        verifyThat("Sign In:", hasText("Sign In:"));
    }


    @Test
    void adminDashboardToUserlistThenBack() {
        goTo(SceneType.ADMIN_DASH);

        clickOn("Userlist");
        verifyThat("Back", hasText("Back"));

        clickOn("Back");
        verifyThat("Open full userlist: ", hasText("Open full userlist: "));
    }

    @Test
    void adminDashboardToLoginUsingLogout() {
        goTo(SceneType.ADMIN_DASH);

        clickOn("Logout");
        verifyThat("Sign In:", hasText("Sign In:"));
    }

    @Test
    void signupToStudentDashboard() {
        clickOn("#signUpButton");

        clickOn("#usernameField");
        write("student" + System.currentTimeMillis());

        clickOn("#passwordField");
        write("hashcode");

        clickOn("#confirmPasswordField");
        write("hashcode");

        clickOn("#rolesButton");
        clickOn("Student");

        clickOn("#signUpButton");
        verifyThat("Student Dashboard", hasText("Student Dashboard"));
    }

    @Test
    void signupToProfessorDashboard() {
        clickOn("#signUpButton");

        clickOn("#usernameField");
        write("teacher" + System.currentTimeMillis());

        clickOn("#passwordField");
        write("hashcode");

        clickOn("#confirmPasswordField");
        write("hashcode");

        clickOn("#rolesButton");
        clickOn("Teacher");

        clickOn("#signUpButton");
        verifyThat("Welcome, Professor!", hasText("Welcome, Professor!"));
    }

    @Test
    void signupToAdminDashboard() {
        clickOn("#signUpButton");

        clickOn("#usernameField");
        write("admin" + System.currentTimeMillis());

        clickOn("#passwordField");
        write("hashcode");

        clickOn("#confirmPasswordField");
        write("hashcode");

        clickOn("#rolesButton");
        clickOn("Admin");

        clickOn("#signUpButton");
        verifyThat("Open full userlist: ", hasText("Open full userlist: "));
    }

    @Test
    void professorDashboardToStudentListThenBack() {
        goTo(SceneType.PROF_DASH);

        clickOn("Student list");
        verifyThat("Displaying your students...", hasText("Displaying your students..."));

        clickOn("Back");
        verifyThat("Welcome, Professor!", hasText("Welcome, Professor!"));
    }

    @Test
    void professorDashboardToGradebookThenBack() {
        goTo(SceneType.PROF_DASH);

        clickOn("Grade book");
        verifyThat("Professor Gradebook", hasText("Professor Gradebook"));

        clickOn("Back");
        verifyThat("Welcome, Professor!", hasText("Welcome, Professor!"));
    }

    @Test
    void professorDashboardToAssignmentsThenBack() {
        goTo(SceneType.PROF_DASH);

        clickOn("Assignments");
        verifyThat("Create Assignment", hasText("Create Assignment"));

        clickOn("Back");
        verifyThat("Welcome, Professor!", hasText("Welcome, Professor!"));
    }

    @Test
    void professorDashboardToLoginUsingLogout() {
        goTo(SceneType.PROF_DASH);

        clickOn("Logout");
        verifyThat("Sign In:", hasText("Sign In:"));
    }

    @Test
    void studentDashboardToGradebookThenBack() {
        goTo(SceneType.STDNT_DASH);

        clickOn("Open Grades");
        verifyThat("Your Gradebook", hasText("Your Gradebook"));

        clickOn("Back");
        verifyThat("Student Dashboard", hasText("Student Dashboard"));
    }

    @Test
    void studentDashboardToLoginUsingLogout() {
        goTo(SceneType.STDNT_DASH);

        clickOn("Logout");
        verifyThat("Sign In:", hasText("Sign In:"));
    }
    
}
