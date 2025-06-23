package lab6.ui.controllers;

import common.client.CommandInfo;
import common.collection.models.Flat;
import common.collection.models.House;
import common.network.Request;
import common.network.Response;
import common.network.User;
import common.network.exceptions.NetworkException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lab6.ui.AppManager;
import lab6.ui.utils.DataExchanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class TableViewController {
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn name;
    @FXML
    private TableColumn<Flat, Float> x;
    @FXML
    private TableColumn<Flat, Double> y;
    @FXML
    private TableColumn date;
    @FXML
    private TableColumn area;
    @FXML
    private TableColumn numOfRooms;
    @FXML
    private TableColumn height;
    @FXML
    private TableColumn transport;
    @FXML
    private TableColumn furnish;
    @FXML
    private TableColumn<Flat, String> houseName;
    @FXML
    private TableColumn<Flat, Integer> year;
    @FXML
    private TableColumn<Flat, Long> numberOfFlats;
    @FXML
    private TableColumn userId;
    @FXML
    private CheckBox onlyMine;
    @FXML
    private ComboBox commandsComboBox;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField commandArgs;

    @FXML
    private TableView table;

    private ObservableList<Flat> data = FXCollections.observableArrayList();
    private List<CommandInfo> commands;

    public void initialize() {
        //region InitTable
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        date.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        area.setCellValueFactory(new PropertyValueFactory<>("area"));
        numOfRooms.setCellValueFactory(new PropertyValueFactory<>("numberOfRooms"));
        height.setCellValueFactory(new PropertyValueFactory<>("height"));
        furnish.setCellValueFactory(new PropertyValueFactory<>("furnish"));
        transport.setCellValueFactory(new PropertyValueFactory<>("transport"));
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));

        x.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCoordinates().getX()));
        y.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCoordinates().getY()));

        houseName.setCellValueFactory(cellData -> {
            House house = cellData.getValue().getHouse();
            return new SimpleObjectProperty<>(house == null ? null : house.getName());
        });
        year.setCellValueFactory(cellData -> {
            House house = cellData.getValue().getHouse();
            return new SimpleObjectProperty<>(house == null ? null : house.getYear());
        });
        numberOfFlats.setCellValueFactory(cellData -> {
            House house = cellData.getValue().getHouse();
            return new SimpleObjectProperty<>(house == null ? null : house.getNumberOfFlatsOnFloor());
        });
        table.setItems(data);

        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Flat flat = (Flat) table.getSelectionModel().getSelectedItem();
                    if (flat.getUserId() != AppManager.getInstance().authManager.getUser().id()) {
                        messageLabel.setText("text chto nelzya");
                    } else {
                        AppManager.getInstance().setDataExchanger(new DataExchanger(flat));
                        AppManager.getInstance().sceneManager.newWindow("ItemChange");
                    }
                }
            }
        });

        updateTable();
        //endregion

        try {
            Response response = AppManager.getInstance().requestManager.makeRequest(new Request(
                    "get_commands",
                    null,
                    null,
                    AppManager.getInstance().authManager.getUser()));
            commands = (List<CommandInfo>) response.collection();
            commands = commands.stream()
                    .filter(commandInfo -> commandInfo.clientAvailable())
                    .toList();
            commandsComboBox.getItems().addAll(commands.stream()
                    .map(CommandInfo::name)
                    .toList());
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }

    }


    public void onlyMine(ActionEvent actionEvent) {
        if (onlyMine.isSelected()) {
            data.removeIf(flat -> flat.getUserId() != AppManager.getInstance().authManager.getUser().id());
        } else {
            updateTable();
        }
    }

    private void updateTable() {
        try {
            Response response = AppManager.getInstance().requestManager.parseAndMake("show");

            Collection<? extends Flat> flats = (Collection<? extends Flat>) response.collection();
            table.getItems().clear();
            data.addAll(flats);
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
        }
    }

    public void executeCommand(ActionEvent actionEvent) {
        if (commandsComboBox.getValue() == null) {
            return;
        }
        String commandName = commandsComboBox.getValue().toString()
                .trim()
                .replace("\t", " ")
                .split("\\s+")[0];
        String[] args = commandArgs.getText()
                .trim()
                .replace("\t", " ")
                .split("\\s+");
        try {
            Response response = AppManager.getInstance().requestManager.makeRequest(
                    new Request(commandName,
                            commandArgs.getText().trim().equals("") ? null : args,
                            null,
                            AppManager.getInstance().authManager.getUser())
            );
            switch (response.type()){
                case OK:
                    data.clear();
                    data.addAll((Collection<? extends Flat>) response.collection());
                    messageLabel.setText(response.message());
                    break;
                case INPUT_FLAT:
                    AppManager.getInstance().sceneManager.newWindow("Create");
                    break;
                case EXCEPTION:
                    AppManager.getInstance().sceneManager.showWarning(response.message());
                    break;
            }
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }
}
