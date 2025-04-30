package common.builders;


import common.client.console.Console;
import common.collection.exceptions.ValidationException;
import common.collection.models.*;

public class FlatBuilder extends DefaultConsoleBuilder {
    private String[] args;

    public FlatBuilder(Console console) {
        super(console);
    }

    public FlatBuilder(Console console, String[] args) {
        super(console);
        this.args = args;
    }

    @Override
    public Flat build() {
        if (args != null) return buildFromString(args);
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
            return build();
        }
    }

    public static Flat buildFromString(String[] args) {
        String format = "\nФормат создания в одну строку: \n" +
        "add name x y area numberOfRooms height furnish transport houseName year numberOfFlatsOnFloor";

        try {
            if (args.length > 11){
                throw new BuildException("Слишком много аргументов! Можно запутаться!");
            }
            return new Flat(
                    args[0],
                    new Coordinates(Float.valueOf(args[1]), Double.valueOf(args[2])),
                    Float.valueOf(args[3]),
                    Integer.valueOf(args[4]),
                    Long.valueOf(args[5]),
                    Furnish.valueOf(args[6].toUpperCase()),
                    Transport.valueOf(args[7].toUpperCase()),
                    new House(args[8], Integer.valueOf(args[9]), Long.valueOf(args[10])));
        } catch (IndexOutOfBoundsException e) {
            throw new BuildException("Слишком мало аргументов. Для создания квартиры требуется 11." + format);
        } catch (NumberFormatException e) {
            throw new BuildException("Неверный формат ввода числа!" + format);
        } catch (IllegalArgumentException e) {
            throw new BuildException("Такого значения нет в доступных для ввода транспорта и мебели!" + format);
        } catch (Exception e) {
            throw new BuildException(e.getMessage());
        }
    }

    private House inputHouse() {
        if (!console.read("Если не хотите добавлять дом, то нажмите Enter: ").equals("")) {
            try {
                House house = new HouseBuilder(console).build();
                Flat.Validator.validateHouse(house);
                return house;
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputHouse();
            }
        }
        return null;
    }

    private Transport inputTransport() {
        Transport transport = inputEnum("Введите транспорт: ", Transport.class);
        try {
            Flat.Validator.validateTransport(transport);
            return transport;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputTransport();
        }
    }

    private Furnish inputFurnish() {
        if (!console.read("Если не хотите добавлять мебель, то нажмите Enter: ").isEmpty()) {
            Furnish furnish = inputEnum("Введите мебель: ", Furnish.class);
            try {
                Flat.Validator.validateFurnish(furnish);
                return furnish;
            } catch (ValidationException e) {
                console.writeln(e.getMessage());
                return inputFurnish();
            }
        }
        return null;
    }

    private long inputHeight() {
        long input = (long) inputLong("Введите высоту: ");
        try {
            Flat.Validator.validateHeight(input);
            return input;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputHeight();
        }
    }

    private int inputNumberOfRooms() {
        int input = inputInteger("Введите количество комнат: ");
        try {
            Flat.Validator.validateNumberOfRooms(input);
            return input;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputNumberOfRooms();
        }
    }

    private float inputArea() {
        float input = inputFloat("Введите площадь: ");
        try {
            Flat.Validator.validateArea((float) Float.valueOf(input));
            return input;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputArea();
        }
    }

    private Coordinates inputCoordinates() {
        try {
            Coordinates coordinates = new CoordinatesBuilder(console).build();
            Flat.Validator.validateCoordinates(coordinates);
            return coordinates;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputCoordinates();
        }
    }

    private String inputName() {
        String input = inputString("Введите название: ").trim();
        try {
            Flat.Validator.validateName(input);
            return input;
        } catch (ValidationException e) {
            console.writeln(e.getMessage());
            return inputName();
        }
    }
}
