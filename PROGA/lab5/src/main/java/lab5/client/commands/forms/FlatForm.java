package lab5.client.commands.forms;

import lab5.client.console.Console;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.*;

import java.io.IOException;

public class FlatForm extends Form {
    public FlatForm(Console console) {
        super(console);
    }

    @Override
    public Flat run(){
        console.writeln("");
        console.writeln("=== Создание квартиры ===");
        try {
            Flat flat = new Flat(inputName(),
                    inputCoordinates(),
                    inputArea(),
                    inputNumberOfRooms(),
                    inputHeight(),
                    inputFurnish(),
                    inputTransport(),
                    inputHouse());
            console.writeln("");
            return flat;
        } catch (Exception e) {
            console.writeln(e.getMessage());
            return run();
        }
    }

    private House inputHouse(){
        if (!console.read("Если не хотите добавлять дом, то введите пустую строку: ").equals("")) {
            try {
                House house = new HouseForm(console).run();
                Flat.ValidateHouse(house);
                return house;
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputHouse();
            }
        }
        return null;
    }

    private Transport inputTransport() {
        String input;

        console.write("Список доступных для ввода значений транспорта:");
        for (Transport transport : Transport.values()){
            console.write(" " + transport.toString() + ";");
        }
        console.writeln("");

        while ((input = console.read("Введите транспорт: ")) != null) {
            try {
                Flat.ValidateTransport(Transport.valueOf(input.toUpperCase()));
                return Transport.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                console.writeln("Введите значение из предложенного списка!");
                return inputTransport();
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputTransport();
            }
        }
        return null;
    }

    private Furnish inputFurnish() {
        String input;
        if (!console.read("Если не хотите добавлять мебель, то введите пустую строку: ").equals("")) {

            console.write("Список доступных для ввода значений мебели:");
            for (Furnish furnish : Furnish.values()){
                console.write(" " + furnish.toString() + ";");
            }
            console.writeln("");

            while ((input = console.read("Введите мебель: ")) != null) {
                try {
                    Flat.ValidateFurnish(Furnish.valueOf(input.toUpperCase()));
                    return Furnish.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    console.writeln("Введите значение из предложенного списка!");
                    return inputFurnish();
                } catch (ValidationException e) {
                    console.writeln(e.getMessage());
                    return inputFurnish();
                }
            }
        }
        return null;
    }

    private long inputHeight() {
        String input;
        while ((input = console.read("Введите высоту: ")) != null) {
            try {
                Flat.ValidateHeight((long) Long.valueOf(input));
                return (long) Long.valueOf(input);
            } catch (NumberFormatException e) {
                console.writeln("Введите целое число!");
                return inputHeight();
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputHeight();
            }
        }
        return 0l;
    }

    private int inputNumberOfRooms() {
        String input;
        while ((input = console.read("Введите количество комнат: ")) != null) {
            try {
                Flat.ValidateNumberOfRooms((int) Integer.valueOf(input));
                return (int) Integer.valueOf(input);
            } catch (NumberFormatException e) {
                console.writeln("Введите целое число!");
                return inputNumberOfRooms();
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputNumberOfRooms();
            }
        }
        return 0;
    }

    private float inputArea() {
        String input;
        while ((input = console.read("Введите площадь: ")) != null) {
            try {
                Flat.ValidateArea((float) Float.valueOf(input));
                return (float) Float.valueOf(input);
            } catch (NumberFormatException e) {
                console.writeln("Введите дробное или целое число!");
                return inputArea();
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputArea();
            }
        }
        return 0f;
    }

    private Coordinates inputCoordinates() {
        try {
            Coordinates coordinates = new CoordinatesForm(console).run();
            Flat.ValidateCoordinates(coordinates);
            return coordinates;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputCoordinates();
        }
    }

    private String inputName() {
        String input;
        while ((input = console.read("Введите название: ")) != null) {
            try {
                Flat.ValidateName(input);
                return input;
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputName();
            }
        }
        return null;
    }
}
