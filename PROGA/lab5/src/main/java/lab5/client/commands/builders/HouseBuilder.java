package lab5.client.commands.builders;

import lab5.client.console.Console;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.House;

public class HouseBuilder extends DefaultConsoleBuilder {
    public HouseBuilder(Console console) {
        super(console);
    }

    @Override
    public House build() {
        console.writeln("");
        console.writeln("=== Создание дома ===");
        try {
            House house = new House(inputName(), inputYear(), inputNumberOfFlatsOnFloor());
            house.validate();
            console.writeln("");
            return house;
        } catch (Exception e) {
            console.writeln(e.getMessage());
            return build();
        }
    }

    private String inputName() {
        String input = inputString("Введите название: ").trim();
        try {
            House.Validator.validateName(input);
            return input;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputName();
        }
    }

    private int inputYear() {
        int input = inputInteger("Введите год постройки: ");
        try {
            House.Validator.validateYear(input);
            return input;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputYear();
        }
    }

    private Long inputNumberOfFlatsOnFloor() {
        Long input = inputLong("Введите количество квартир на этаже: ");
        try {
            House.Validator.validateNumberOfFlatsOnFloor(input);
            return input;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputNumberOfFlatsOnFloor();
        }
    }
}
