package tools;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Stage;
import database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import tools.SceneManager;
import tools.SceneType;


/**
 * @author Adam Vartan
 * <br>
 * created:
 * @since 0.1.0
 */
public class UserNotification {
    public static void addNotification(String username) {
        Stage popup = new Stage();

        Label message = new Label("Are you sure you would like to delete the user?");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            UserListRepository.getInstance().delete(username);
            popup.close();
        });

        noButton.setOnAction(e -> {
            popup.close();
        });

        HBox buttons = new HBox(10, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        VBox vbox01 = new VBox();
        Pane vBoxSpacer = new Pane();
        VBox.setVgrow(vBoxSpacer, Priority.ALWAYS);
        vbox01.getChildren().addAll(message, buttons);
        vbox01.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox01, 300, 100);

        popup.setScene(scene);
        popup.setTitle("WARNING!!!");
        popup.setAlwaysOnTop(true);
        popup.show();
    }
}
