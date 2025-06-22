package lab6.ui.controllers;

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

public class ItemChangeController {
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


    @FXML
    public void initialize() {
        this.flat = (Flat) AppManager.getInstance().getDataExchanger().data();
        name.setText(flat.getName());
        x.setText(String.valueOf(flat.getCoordinates().getX()));
        y.setText(String.valueOf(flat.getCoordinates().getY()));
        area.setText(String.valueOf(flat.getArea()));
        numOfRooms.setText(String.valueOf(flat.getNumberOfRooms()));
        height.setText(String.valueOf(flat.getHeight()));
        transport.setText(String.valueOf(flat.getTransport()));

        if (flat.getFurnish() != null) {
            furnish.setText(String.valueOf(flat.getFurnish()));
        }
        if (flat.getHouse() != null) {
            houseName.setText(String.valueOf(flat.getHouse().getName()));
            year.setText(String.valueOf(flat.getHouse().getYear()));
            numberOfFlats.setText(String.valueOf(flat.getHouse().getNumberOfFlatsOnFloor()));
        }
    }


    public void change(ActionEvent actionEvent) {
        try {
            Flat flat1 = new Flat(name.getText(),
                    new Coordinates(Float.valueOf(x.getText()), Double.valueOf(y.getText())),
                    Float.valueOf(area.getText()),
                    Integer.valueOf(numOfRooms.getText()),
                    Long.valueOf(height.getText()),
                    Transport.valueOf(transport.getText()),
                    null);
            if (!furnish.getText().equals("")) {
                flat1.setFurnish(Furnish.valueOf(furnish.getText()));
            }
            if (!houseName.getText().equals("")) {
                flat1.setHouse(new House(houseName.getText(),
                        Integer.valueOf(year.getText()),
                        Long.valueOf(numberOfFlats.getText())));
                flat1.getHouse().validate();
            }
            flat1.setId(flat.getId());
            flat1.setUserId(flat.getUserId());

            flat1.validate();
            flat1.getCoordinates().validate();

            Request request = new Request("update",
                    new String[]{flat.getId().toString()},
                    flat1,
                    AppManager.getInstance().authManager.getUser());
            System.out.println(request);
            Response response = AppManager.getInstance().requestManager.makeRequest(request);
            System.out.println(response);
            messageLabel.setText(response.message());
            if (response.type().equals(ResponseType.OK)) {
                ((Stage) messageLabel.getScene().getWindow()).close();
            }
        } catch (ValidationException e) {
            messageLabel.setText(e.getMessage());
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(ActionEvent actionEvent) {
        try {
            Response response = AppManager.getInstance().requestManager.parseAndMake("remove_by_id " + flat.getId());
            messageLabel.setText(response.message());
            if (response.type().equals(ResponseType.OK)) {
                ((Stage) messageLabel.getScene().getWindow()).close();
            }
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }
}
