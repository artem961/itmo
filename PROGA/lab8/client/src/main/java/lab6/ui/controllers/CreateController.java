package lab6.ui.controllers;

import common.builders.BuildException;
import common.builders.FlatBuilder;
import common.collection.exceptions.ValidationException;
import common.collection.models.*;
import common.network.Request;
import common.network.Response;
import common.network.enums.ResponseType;
import common.network.exceptions.NetworkException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lab6.ui.AppManager;

public class CreateController {
    private Flat flat;

    @FXML
    private TextField name;
    @FXML
    private TextField x;
    @FXML
    private TextField y;
    @FXML
    private TextField area;
    @FXML
    private TextField numOfRooms;
    @FXML
    private TextField height;
    @FXML
    private TextField transport;
    @FXML
    private TextField furnish;
    @FXML
    private TextField houseName;
    @FXML
    private TextField year;
    @FXML
    private TextField numberOfFlats;
    @FXML
    private Label messageLabel;


    public void create(ActionEvent actionEvent) {

        try {
            String nameF = name.getText();
            Coordinates coordinates = new Coordinates(Float.valueOf(x.getText()), Double.valueOf(y.getText()));
            Float area1 = Float.parseFloat(area.getText());
            Integer numOfRooms1 = Integer.parseInt(numOfRooms.getText());
            Long height1 = Long.parseLong(height.getText());
            Transport transport1 = Transport.valueOf(transport.getText().toUpperCase());

            Flat flat1 = new Flat(nameF,
                    coordinates,
                    area1,
                    numOfRooms1,
                    height1,
                    transport1,
                    null);

            if (!furnish.getText().equals("")) {
                flat1.setFurnish(Furnish.valueOf(furnish.getText().toUpperCase()));
            }
            if (!houseName.getText().equals("")) {
                flat1.setHouse(new House(houseName.getText(),
                        Integer.parseInt(year.getText()),
                        Long.parseLong(numberOfFlats.getText())));
                flat1.getHouse().validate();
            }

            flat1.validate();
            flat1.getCoordinates().validate();

            Request request = new Request("add",
                    null,
                    flat1,
                    AppManager.getInstance().authManager.getUser());
            Response response = AppManager.getInstance().requestManager.makeRequest(request);
            messageLabel.setText(response.message());
            if (response.type().equals(ResponseType.OK)) {
                closeThisWindow();
            }
        } catch (ValidationException e) {
            messageLabel.setText(e.getMessage());
        } catch (NumberFormatException e){
            messageLabel.setText("Не верный формат ввода чисел!");
        } catch (IllegalArgumentException e) {
                if (furnish.getText().isEmpty()) {
                    messageLabel.setText("Нет таких значений Transport");
                }
                if (!furnish.getText().isEmpty()) {
                    messageLabel.setText("Нет таких значений Furnish");
                }
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeThisWindow() {
        ((Stage) messageLabel.getScene().getWindow()).close();
    }
}
