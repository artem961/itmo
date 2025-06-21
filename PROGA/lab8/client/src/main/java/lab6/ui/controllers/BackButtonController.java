package lab6.ui.controllers;

import javafx.event.ActionEvent;
import lab6.ui.AppManager;

public class BackButtonController {
    public void back(ActionEvent actionEvent) {
        AppManager.getInstance().sceneManager.previousScene();
    }
}
