import static javafx.application.Application.launch;

import database.DatabaseManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import tools.SceneManager;
import tools.SceneType;

/**
 * @author Zachary King
 * <br>
 * created: 04/18/2026
 * @since 0.1.0
 */
public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    SceneManager.init(stage);
    stage.setTitle("Grade Tracker 5000");
    SceneManager.getInstance().navigateTo(SceneType.LOGIN);
    stage.show();
  }

  @Override
  public void stop() throws SQLException {
    DatabaseManager.getInstance().close();
  }
}
