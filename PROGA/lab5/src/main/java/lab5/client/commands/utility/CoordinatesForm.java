package lab5.client.commands.utility;

import lab5.client.console.Console;
import lab5.collection.models.Coordinates;

import java.io.IOException;

public class CoordinatesForm extends Form {
    public CoordinatesForm(Console console) {
        super(console);
    }

    @Override
    public Coordinates run() throws IOException {
        try {
            Coordinates coordinates = new Coordinates(inputX(), inputY());
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
                return Float.valueOf(input);
            } catch (NumberFormatException e) {
                console.writeln("Введите дробное или целое число!");
                return inputX();
            }
        }
        return null;
    }

    private double inputY() throws IOException {
        String input;
        while ((input = console.read("Введите Y: ")) != null) {
            try {
                return (double) Double.valueOf(input);
            } catch (NumberFormatException e) {
                console.writeln("Введите дробное или целое число!");
                return inputY();
            }
        }
        return 0d;
    }
}
