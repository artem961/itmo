package lab6.ui.controllers;

import common.network.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import lab6.ui.AppManager;


public class HeaderController{
    @FXML
    private Label userLabel;

    @FXML
    public void initialize() {
        User user = AppManager.getInstance().authManager.getUser();
        String name = "None";
        if (user != null) name = user.name();
        userLabel.setText(name);
    }

    public void user(MouseEvent mouseEvent) {
        if (AppManager.getInstance().authManager.isAuth()) {
            AppManager.getInstance().sceneManager.switchScene("User");
        } else {
            AppManager.getInstance().sceneManager.switchScene("Start");
        }
    }
}
