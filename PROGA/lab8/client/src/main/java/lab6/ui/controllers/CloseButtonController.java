package lab6.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CloseButtonController {
    @FXML
    private Button closeButton;

    public void close(ActionEvent actionEvent) {
        ((Stage) closeButton.getScene().getWindow()).close();
    }
}
