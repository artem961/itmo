package lab5.client.commands.utility;

import lab5.client.console.Console;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.House;

import java.io.IOException;

public class HouseForm extends Form{
    public HouseForm(Console console) {
        super(console);
    }

    @Override
    public House run() throws IOException {
        try {
            House house = new House(inputName(), inputYear(), inputNumberOfFlatsOnFloor());
            return house;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return run();
        }
    }

    private String inputName() throws IOException {
        String input;
        while ((input = console.read("Введите название: ")) != null){
            return input;
        }
        return null;
    }

    private int inputYear() throws IOException {
        String input;
        while ((input = console.read("Введите год постройки: ")) != null){
            return (int) Integer.valueOf(input);
        }
        return 0;
    }

    private Long inputNumberOfFlatsOnFloor() throws IOException {
        String input;
        while ((input = console.read("Введите количество квартир на этаже: ")) != null){
            return Long.valueOf(input);
        }
        return null;
    }
}
