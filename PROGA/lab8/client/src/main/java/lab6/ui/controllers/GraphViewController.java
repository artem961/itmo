package lab6.ui.controllers;

import common.collection.models.Flat;
import common.network.Response;
import common.network.User;
import common.network.exceptions.NetworkException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lab6.ui.AppManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class GraphViewController {
    private static final Color MY_FLAT_COLOR = Color.web("#4CAF50");
    private static final Color OTHER_FLAT_COLOR = Color.web("#E53935");
    private static final Color BORDER_COLOR = Color.web("#212121", 0.8);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font FLAT_FONT = Font.font("Roboto", FontWeight.BOLD, 14);
    private static final double FONT_SIZE_TO_CIRCLE_RATIO = 3.0;
    private static final double MIN_FONT_SIZE = 9.0;
    private static final double MAX_FONT_SIZE = 40.0;
    private static final double TEXT_PADDING_RATIO = 0.9;

    private double scale = 1.0;
    private double offsetX = 0.0;
    private double offsetY = 0.0;

    private double lastMouseX;
    private double lastMouseY;

    @FXML
    private Canvas canvas;

    @FXML
    private StackPane graphWrap;

    private GraphicsContext gc;
    private Collection<? extends Flat> flats = new ArrayList<>();

    @FXML
    public void initialize() {
        refresh();
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(graphWrap.widthProperty());
        canvas.heightProperty().bind(graphWrap.heightProperty());

        canvas.widthProperty().addListener(evt -> update());
        canvas.heightProperty().addListener(evt -> update());

        canvas.setOnScroll(event -> {
            double zoomFactor = event.getDeltaY() > 0 ? 1.1 : 1 / 1.1;

            double mouseX = event.getX();
            double mouseY = event.getY();

            offsetX = mouseX - (mouseX - offsetX) * zoomFactor;
            offsetY = mouseY - (mouseY - offsetY) * zoomFactor;
            scale *= zoomFactor;

            update();
            event.consume();
        });

        canvas.setOnMousePressed(event -> {
            lastMouseX = event.getX();
            lastMouseY = event.getY();
        });

        canvas.setOnMouseDragged(event -> {
            offsetX += event.getX() - lastMouseX;
            offsetY += event.getY() - lastMouseY;
            lastMouseX = event.getX();
            lastMouseY = event.getY();
            update();
        });

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handleClick(event);
            }
        });

        update();
    }

    private void drawFlat(Flat flat, User currentUser) {
        double size = Math.max(15, flat.getArea() / 10.0);
        double radius = size / 2.0;
        double x = flat.getCoordinates().getX();
        double y = flat.getCoordinates().getY();
        double centerX = x + radius;
        double centerY = y + radius;

        gc.setFill(flat.getUserId() == currentUser.id() ? MY_FLAT_COLOR : OTHER_FLAT_COLOR);
        gc.fillOval(x, y, size, size);
        gc.setStroke(BORDER_COLOR);
        gc.setLineWidth(Math.max(1.5, size / 25.0));
        gc.strokeOval(x, y, size, size);

        String originalText = flat.getName();
        String textToDraw = originalText;

        double idealFontSize = size / FONT_SIZE_TO_CIRCLE_RATIO;
        double finalFontSize = Math.min(MAX_FONT_SIZE, Math.max(MIN_FONT_SIZE, idealFontSize));

        Text textNode = new Text(originalText);
        boolean fits = false;
        while (finalFontSize >= MIN_FONT_SIZE) {
            textNode.setFont(Font.font(FLAT_FONT.getFamily(), FontWeight.BOLD, finalFontSize));
            if (textNode.getLayoutBounds().getWidth() < size * TEXT_PADDING_RATIO) {
                fits = true;
                break;
            }
            finalFontSize--;
        }

        if (!fits) {
            finalFontSize = MIN_FONT_SIZE;
            String baseText = originalText;

            while (baseText.length() > 0) {
                textToDraw = baseText + "...";
                textNode.setText(textToDraw);
                textNode.setFont(Font.font(FLAT_FONT.getFamily(), FontWeight.BOLD, finalFontSize));

                if (textNode.getLayoutBounds().getWidth() < size * TEXT_PADDING_RATIO) {
                    break;
                }

                baseText = baseText.substring(0, baseText.length() - 1);
            }

            if (baseText.length() == 0) textToDraw = "";
        }

        if (!textToDraw.isEmpty()) {
            gc.setFont(Font.font(FLAT_FONT.getFamily(), FontWeight.BOLD, finalFontSize));
            gc.setFill(TEXT_COLOR);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(textToDraw, centerX, centerY);
        }
    }

    @FXML
    private void update() {
        if (gc == null) return;
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        gc.clearRect(0, 0, width, height);

        gc.save();
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);



        if (flats != null) {
            User user = AppManager.getInstance().authManager.getUser();
            flats.forEach(flat -> drawFlat(flat, user));
        }
        gc.restore();
    }

    private void refresh() {
        try {
            Response response = AppManager.getInstance().requestManager.parseAndMake("show");

            flats = (Collection<? extends Flat>) response.collection();
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
        }
    }

    private void handleClick(MouseEvent event){
        double x = (event.getX() - offsetX) / scale;
        double y = (event.getY() - offsetY) / scale;

        List<Flat> reversedFlats = new ArrayList<>(flats);
        Collections.reverse(reversedFlats);

        for (Flat flat : reversedFlats) {
            double size = flat.getArea() / 10;
            double flatCenterX = flat.getCoordinates().getX() + size/2;
            double flatCenterY = flat.getCoordinates().getY() + size/2;

            if (Math.abs(x - flatCenterX) <= size/2 && Math.abs(y - flatCenterY) <= size/2) {
                showInfo(flat);
                break;
            }
        }
    }

    private void showInfo(Flat flat){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация об объекте");
        alert.setHeaderText("Квартира ID: " + flat.getId() + ", \"" + flat.getName() + "\"");

        String content = flat.toString();
        alert.setContentText(content);
        alert.getDialogPane().setMinWidth(400);
        alert.showAndWait();
    }
}
