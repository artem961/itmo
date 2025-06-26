package lab6.ui.controllers;

import common.builders.UserBuilder;
import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import lab6.ui.AppManager;

public class AuthController {
    @FXML
    private Button authButton;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passField;
    @FXML
    private Label messageLabel;

    public void auth(ActionEvent actionEvent) {
        User user = new UserBuilder().buildUser(nameField.getText(), passField.getText());
        Response response = AppManager.getInstance().authManager.authUser(user);
        if (response.type() == ResponseType.AUTH){
            AppManager.getInstance().sceneManager.switchScene("Main");
        } else{
            this.messageLabel.setText(response.message());
            this.messageLabel.setVisible(true);
        }
    }
}
