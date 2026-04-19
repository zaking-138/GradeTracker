package tools;

import java.util.HashMap;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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

      case ADMIN_DASH -> adminDashBuild(stage);
      case ADMIN_USERLIST -> adminUserlistBuild(stage);

      case PROF_DASH -> profDashBuild(stage);
      case PROF_USERLIST -> profUserlistBuild(stage);
      case PROF_GRDBK -> profGrdBkBuild(stage);

      case STDNT_DASH -> stdntDashBuild(stage);
      case STDNT_GRDBK -> stdntGrdBkBuild(stage);
    };
  }


  // ── Helper functions ──────────────────────────────────────────────

  public static HashMap<String, Double> getScreenSize(){
    double defaultVal = 0.85;
    return getScreenSize(defaultVal);
  }

  private static HashMap<String, Double> getScreenSize(Double scale){
    double width = Screen.getPrimary().getBounds().getWidth() * scale;
    double height = Screen.getPrimary().getBounds().getHeight() * scale;
    HashMap<String, Double> rtrnMap = new HashMap<>();
    rtrnMap.put("w", width);
    rtrnMap.put("h", height);
    return rtrnMap;
  }


  // ── Scene builders ──────────────────────────────────────────────

  private static Scene loginBuild(Stage stage) {
    BorderPane base = new BorderPane();
    Text message = new Text("Sign In:\n[username]\n[password]\n\n[[login]]");
    message.setFont(new Font(20));
    message.setTextAlignment(TextAlignment.CENTER);
    base.setCenter(message);
    return new Scene(base, getScreenSize().get("w"), getScreenSize().get("h"));

    //TODO
//    return null;
  }

  private static Scene adminDashBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene adminUserlistBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene profDashBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene profUserlistBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene profGrdBkBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene stdntDashBuild(Stage stage) {
    //TODO
    return null;
  }

  private static Scene stdntGrdBkBuild(Stage stage) {
    //TODO
    return null;
  }

}
