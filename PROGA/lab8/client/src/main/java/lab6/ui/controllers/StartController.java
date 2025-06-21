package lab6.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import lab6.ui.AppManager;
import lab6.ui.utils.Language;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class StartController {
    public void auth(ActionEvent actionEvent) {
        AppManager.getInstance().sceneManager.switchScene("Auth");
    }

    public void reg(ActionEvent actionEvent) {
        AppManager.getInstance().sceneManager.switchScene("Registration");
    }
}
