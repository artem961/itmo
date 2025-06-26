package lab6.ui.controllers;

import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import lab6.ui.AppManager;

import java.util.Optional;


public class UserController {
    private User user;

    @FXML
    public Label userName;

    @FXML
    public void initialize(){
        this.user = AppManager.getInstance().authManager.getUser();
        if (AppManager.getInstance().authManager.isAuth()){
            userName.setText(user.name());
        }
    }

    public void out(ActionEvent actionEvent) {
        AppManager.getInstance().authManager.logOut();
        AppManager.getInstance().sceneManager.switchScene("Start");
    }

    public void deleteAccount(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "uveren?", ButtonType.YES, ButtonType.NO);
        alert.initOwner(userName.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Response response = AppManager.getInstance().authManager.deleteUser();
            if (response.type().equals(ResponseType.AUTH)){
                AppManager.getInstance().sceneManager.switchScene("Start");
            }
        }
    }
}
