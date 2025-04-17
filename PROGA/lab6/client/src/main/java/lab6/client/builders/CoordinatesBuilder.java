package lab6.client.builders;


import common.client.console.Console;
import common.collection.exceptions.ValidationException;
import common.collection.models.Coordinates;

public class CoordinatesBuilder extends DefaultConsoleBuilder {
    public CoordinatesBuilder(Console console) {
        super(console);
    }

    @Override
    public Coordinates build(){
        console.writeln("");
        console.writeln("=== Создание координат ===");
        try {
            Coordinates coordinates = new Coordinates(inputX(), inputY());
            console.writeln("");
            return coordinates;
        } catch (Exception e) {
            console.writeln(e.getMessage());
            return build();
        }
    }

    private Float inputX() {
        Float input = inputFloat("Введите X: ");
        try {
            Coordinates.Validator.validateX(input);
            return input;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputX();
        }
    }

    private double inputY() {
        double input = inputDouble("Введите Y: ");
        try {
            Coordinates.Validator.validateY(input);
            return input;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputY();
        }
    }
}