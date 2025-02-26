package lab5.client.commands.forms;

import lab5.client.console.Console;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.Coordinates;

import java.io.IOException;

public class CoordinatesForm extends Form {
    public CoordinatesForm(Console console) {
        super(console);
    }

    @Override
    public Coordinates run(){
        console.writeln("");
        console.writeln("=== Создание координат ===");
        try {
            Coordinates coordinates = new Coordinates(inputX(), inputY());
            console.writeln("");
            return coordinates;
        } catch (Exception e) {
            console.writeln(e.getMessage());
            return run();
        }
    }

    private Float inputX() throws IOException {
        String input;
        while ((input = console.read("Введите X: ")) != null) {
            try {
                Coordinates.ValidateX(Float.valueOf(input));
                return Float.valueOf(input);
            } catch (NumberFormatException e) {
                console.writeln("Введите дробное или целое число!");
                return inputX();
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputX();
            }
        }
        return null;
    }

    private double inputY() throws IOException {
        String input;
        while ((input = console.read("Введите Y: ")) != null) {
            try {
                Coordinates.ValidateY((double) Double.valueOf(input));
                return (double) Double.valueOf(input);
            } catch (NumberFormatException e) {
                console.writeln("Введите дробное или целое число!");
                return inputY();
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputY();
            }
        }
        return 0d;
    }
}