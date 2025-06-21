package lab6.ui.controllers;

import common.network.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import lab6.ui.AppManager;
import lab6.ui.utils.Language;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
        AppManager.getInstance().sceneManager.switchScene("User");
    }
}
