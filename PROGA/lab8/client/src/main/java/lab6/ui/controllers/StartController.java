package lab6.ui.controllers;

import javafx.event.ActionEvent;
import lab6.ui.AppManager;


public class StartController {
    public void auth(ActionEvent actionEvent) {
        AppManager.getInstance().sceneManager.switchScene("Auth");
    }

    public void reg(ActionEvent actionEvent) {
        AppManager.getInstance().sceneManager.switchScene("Registration");
    }
}
