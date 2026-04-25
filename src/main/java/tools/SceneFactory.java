package tools;

import static controllers.AdminDashboardController.*;
import static controllers.AdminUserlistController.adminUserlistBuild;
import static controllers.LoginController.*;
import static controllers.SignupController.*;
import static controllers.ProfessorDashboardController.profDashBuild;
import static controllers.ProfessorGradebookController.profGrdBkBuild;
import static controllers.ProfessorUserlistController.profUserlistBuild;
import static controllers.SignUpController.signUpBuild;
import static controllers.StudentDashboardController.stdntDashBuild;
import static controllers.StudentGradebookController.stdntGrdBkBuild;
import static tools.Helpers.*;

import java.util.Arrays;
import java.util.HashMap;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Zachary King
 * <br>
 * created: 04/18/2026
 * @since 0.1.0
 */
public class SceneFactory {


  public static Scene create(SceneType type, Stage stage){
    return switch (type){
      case LOGIN -> loginBuild(stage);
      case SIGNUP -> signUpBuild(stage);

      case SIGN_UP -> signUpBuild(stage);
      case ADMIN_DASH -> adminDashBuild(stage);
      case ADMIN_USERLIST -> adminUserlistBuild(stage);

      case PROF_DASH -> profDashBuild(stage);
      case PROF_USERLIST -> profUserlistBuild(stage);
      case PROF_GRDBK -> profGrdBkBuild(stage);

      case STDNT_DASH -> stdntDashBuild(stage);
      case STDNT_GRDBK -> stdntGrdBkBuild(stage);
    };
  }

  // ── Scene builders ──────────────────────────────────────────────

}
