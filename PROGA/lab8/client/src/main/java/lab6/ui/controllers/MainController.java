package lab6.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import lab6.ui.AppManager;

public class MainController {
    @FXML
    private TabPane tabPane;


    public void update(MouseEvent mouseEvent) {
        AppManager.getInstance().sceneManager.reloadScene();
    }
}
