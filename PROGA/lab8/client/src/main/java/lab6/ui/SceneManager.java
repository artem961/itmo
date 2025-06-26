package lab6.ui;

import common.collection.models.Flat;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import lab6.ui.utils.UserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SceneManager {
    private static final Logger logger = LogManager.getLogger(SceneManager.class);
    private final UserSettings userSettings;

    private final Stage stage;
    private String currentSceneName;
    private String previousSceneName;
    private final Map<String, Parent> sceneCache = new HashMap<>();

    public SceneManager(Stage stage, UserSettings userSettings) {
        this.stage = stage;
        this.userSettings = userSettings;

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        double minWidth = primaryScreenBounds.getWidth() * 0.5;
        double minHeight = primaryScreenBounds.getHeight() * 0.6;
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);

        stage.setWidth(primaryScreenBounds.getWidth() * 0.8);
        stage.setHeight(primaryScreenBounds.getHeight() * 0.8);

        this.stage.setFullScreen(true);
        this.stage.setMaximized(true);
    }

    private void loadScene(String sceneName, boolean savePrevious, boolean searchInCache) {
        PauseTransition loadingDelay = new PauseTransition(Duration.millis(300));
        loadingDelay.setOnFinished(event -> showLoadingIndicator());


        Task<Parent> loadTask = new Task<>() {
            @Override
            protected Parent call() throws Exception {
                if (sceneCache.containsKey(sceneName) ) {
                    return sceneCache.get(sceneName);
                }

                ResourceBundle resourceBundle = AppManager.getInstance().localeManager.getBundle(
                        AppManager.getInstance().getUserSettings().getLocale());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + sceneName + ".fxml"), resourceBundle);
                Parent root = loader.load();

                sceneCache.put(sceneName, root);
                return root;
            }
        };

        loadTask.setOnSucceeded(event -> {
            loadingDelay.stop();
            Parent root = loadTask.getValue();

            if (stage.getScene() == null) {
                Scene scene = new Scene(root);
                stage.setScene(scene);
            } else {
                stage.getScene().setRoot(root);
            }

            if (!stage.isShowing()) {
                stage.show();
            }

            if (savePrevious) {
                SceneManager.this.previousSceneName = SceneManager.this.currentSceneName;
                SceneManager.this.currentSceneName = sceneName;
            }
        });

        loadTask.setOnFailed(event -> {
            loadingDelay.stop();

            Throwable exception = loadTask.getException();
            logger.error("Failed to load scene: " + sceneName, exception);
            if (stage.getScene() != null && stage.getScene().getRoot() != null) {
            } else {
                stage.getScene().setRoot(new StackPane());
            }
            showWarning("Не удалось загрузить экран: " + exception.getMessage());
        });

        loadingDelay.play();
        new Thread(loadTask).start();
    }

    public void clearCache() {
        sceneCache.clear();
    }

    private void showLoadingIndicator() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        StackPane loadingPane = new StackPane(progressIndicator);
        loadingPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");

        if (stage.getScene() == null) {
            stage.setScene(new Scene(loadingPane, 300, 200));
        } else {
            stage.getScene().setRoot(loadingPane);
        }
        if (!stage.isShowing()) {
            stage.show();
        }
    }

    public void updateScene(){
        loadScene(currentSceneName, false, true);
    }

    public void reloadScene(){
        loadScene(currentSceneName, false, false);
    }

    public void switchScene(String sceneName){
        loadScene(sceneName, true, true);
    }

    public void previousScene(){
        loadScene(previousSceneName, false, true);
    }

    public void showWarning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.initOwner(stage);
        alert.showAndWait();
    }

    public SceneManager newWindow(String sceneName){
        Stage stage1 = new Stage();
        stage1.setTitle("Новое окно");
        SceneManager sceneManager = new SceneManager(stage1, userSettings);
        sceneManager.loadScene(sceneName, false, false);
        return sceneManager;
    }

}
