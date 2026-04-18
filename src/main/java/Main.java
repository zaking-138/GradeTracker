import static javafx.application.Application.launch;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    double scale = 0.85;

    stage.setTitle("Grade Tracker 5000");
    BorderPane base = new BorderPane();
    base.setCenter(new Text("Hello!"));
    double width = Screen.getPrimary().getBounds().getWidth() * scale;
    double height = Screen.getPrimary().getBounds().getHeight() * scale;
    stage.setScene(new Scene(base, width, height));
//    stage.setScene(SceneFactory.create(SceneType.LOGIN, stage));
    stage.show();
  }
}
