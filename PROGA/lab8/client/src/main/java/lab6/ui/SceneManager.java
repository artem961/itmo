package lab6.ui;

import common.collection.models.Flat;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lab6.ui.utils.UserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ResourceBundle;

public class SceneManager {
    private static final Logger logger = LogManager.getLogger(SceneManager.class);
    private final UserSettings userSettings;

    private final Stage stage;
    private String currentSceneName;
    private String previousSceneName;

    public SceneManager(Stage stage, UserSettings userSettings) {
        this.stage = stage;
        this.userSettings = userSettings;
    }

    private void loadScene(String sceneName, boolean savePrevious) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n.locale_" + userSettings.getLocale().getLanguage(),
                userSettings.getLocale());
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/" + sceneName + ".fxml"), resourceBundle);
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

        if (savePrevious) {
            this.previousSceneName = this.currentSceneName;
            this.currentSceneName = sceneName;
        }
    }

    public void updateScene(){
        loadScene(currentSceneName, false);
    }

    public void switchScene(String sceneName){
        loadScene(sceneName, true);
    }

    public void previousScene(){
        loadScene(previousSceneName, true);
    }

    public void showWarning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }

    public SceneManager newWindow(String sceneName){
        Stage stage1 = new Stage();
        stage1.setTitle("Новое окно");
        SceneManager sceneManager = new SceneManager(stage1, userSettings);
        sceneManager.loadScene(sceneName, false);
        return sceneManager;
    }

}
