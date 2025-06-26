package lab6.ui.controllers;

import common.builders.UserBuilder;
import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lab6.ui.AppManager;

public class RegController {
    @FXML
    private Button regButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passField;
    @FXML
    private Label messageLabel;

    public void reg(ActionEvent actionEvent) {
        User user = new UserBuilder().buildUser(nameField.getText(), passField.getText());
        Response response = AppManager.getInstance().authManager.regUser(user);
        this.messageLabel.setText(response.message());
        if (response.type() == ResponseType.AUTH){
            AppManager.getInstance().sceneManager.switchScene("Main");
        } else{
            this.messageLabel.setText(response.message());
            this.messageLabel.setVisible(true);
        }
    }
}
