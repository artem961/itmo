package lab6.ui;

import common.network.Request;
import common.network.exceptions.NetworkException;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import lab6.client.AuthManager;
import lab6.client.RequestManager;
import lab6.network.NetworkManager;
import lab6.ui.utils.DataExchanger;
import lab6.ui.utils.UserSettings;
import lombok.Getter;

import java.util.Locale;
import java.util.Objects;

import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.System.exit;


public class AppManager {
    private static final Logger logger = LogManager.getLogger(AppManager.class);
    private static AppManager instance;

    @Getter
    @Setter
    private DataExchanger dataExchanger;


    @Getter
    private final UserSettings userSettings;
    private final NetworkManager networkManager;
    public final SceneManager sceneManager;
    public final AuthManager authManager;
    public final RequestManager requestManager;

    private AppManager(Stage stage) {
        NetworkManager nm = null;
        try {
            nm = new NetworkManager();
            System.out.println("Подключение с сервером установлено!");
        } catch (NetworkException e) {
            System.out.println(e.getMessage());
            exit(0);
        }
        this.networkManager = nm;


        this.userSettings = new UserSettings();
        this.sceneManager = new SceneManager(stage, userSettings);
        this.authManager = new AuthManager(networkManager);
        this.requestManager = new RequestManager(networkManager, authManager);
    }

    public static void init(Stage stage) {
        if (instance == null) {
            instance = new AppManager(stage);
        }
    }

    public static AppManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppManager должен быть проинициализирован.");
        }
        return instance;
    }

    public void changeLanguage(Locale locale) {
        this.userSettings.setLocale(locale);
        sceneManager.updateScene();
    }
}
