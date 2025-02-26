package lab5.client.commands.forms;

import lab5.client.console.Console;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.House;

import java.io.IOException;

public class HouseForm extends Form {
    public HouseForm(Console console) {
        super(console);
    }

    @Override
    public House run() {
        console.writeln("");
        console.writeln("=== Создание дома ===");
        try {
            House house = new House(inputName(), inputYear(), inputNumberOfFlatsOnFloor());
            house.validate();
            console.writeln("");
            return house;
        } catch (Exception e) {
            console.writeln(e.getMessage());
            return run();
        }
    }

    private String inputName() {
        String input;
        while ((input = console.read("Введите название: ")) != null) {
            try {
                if (input.equals("")) input = null;
                House.ValidateName(input);
                return input;
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputName();
            }
        }
        return null;
    }

    private int inputYear() {
        String input;
        while ((input = console.read("Введите год постройки: ")) != null) {
            try {
                House.ValidateYear((int) Integer.valueOf(input));
                return (int) Integer.valueOf(input);
            } catch (NumberFormatException e) {
                console.writeln("Введите целое число!");
                return inputYear();
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputYear();
            }
        }
        return 0;
    }

    private Long inputNumberOfFlatsOnFloor() {
        String input;
        while ((input = console.read("Введите количество квартир на этаже: ")) != null) {
            try {
                House.ValidateNumberOfFlatsOnFloor(Long.valueOf(input));
                return Long.valueOf(input);
            } catch (NumberFormatException e) {
                console.writeln("Введите целое число!");
                return inputNumberOfFlatsOnFloor();
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputNumberOfFlatsOnFloor();
            }
        }
        return null;
    }
}
