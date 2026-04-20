import static javafx.application.Application.launch;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tools.SceneFactory;
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

    stage.setTitle("Grade Tracker 5000");

    stage.setScene(SceneFactory.create(SceneType.LOGIN, stage));
    stage.show();
  }
}
