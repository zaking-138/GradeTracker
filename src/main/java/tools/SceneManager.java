package tools;

import java.util.EnumMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Zachary King
 * <br>
 * created:
 * @since 0.1.0
 */
public class SceneManager {
  private static SceneManager instance;

  private final Stage stage;
  private final Map<SceneType, Scene> cache =
      new EnumMap<>(SceneType.class);

  public static void init(Stage stage){
    if (instance == null) instance = new SceneManager(stage);
  }

  public SceneManager(Stage stage) {
    this.stage = stage;
  }

  public static SceneManager getInstance(){
    if(instance == null){
      throw new IllegalStateException("SceneManager not initialized.");
    }
    return instance;
  }

  public void navigateTo(SceneType type){
    if(type.equals(SceneType.LOGIN)){
      cache.remove(type);
    }
    Scene scene = cache.computeIfAbsent(type,
        t -> SceneFactory.create(t, stage));
    stage.setScene(scene);
  }
}
