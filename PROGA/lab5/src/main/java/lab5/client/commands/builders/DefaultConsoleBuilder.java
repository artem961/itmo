package lab5.client.commands.builders;

import lab5.client.console.Console;

/**
 * Класс для сборки объектов через консоль.
 */
public class DefaultConsoleBuilder implements ConsoleBuilder {
    protected final Console console;

    public DefaultConsoleBuilder(Console console) {
        this.console = console;
    }

    @Override
    public Integer inputInteger(String message) {
        String input = inputString(message);
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException e) {
            console.writeln("Введите целое число!");
            return inputInteger(message);
        }
    }

    @Override
    public String inputString(String message) {
        String input;
        while ((input = console.read(message)) != null) {
            return input.trim();
        }
        return "";
    }

    @Override
    public Float inputFloat(String message) {
        String input = inputString(message);
        try {
            if (Float.isInfinite(Float.valueOf(input))){
                console.writeln("Значение слишком большое!");
                return inputFloat(message);
            }
            return Float.valueOf(input);
        } catch (NumberFormatException e) {
            console.writeln("Введите дробное или целое число!");
            return inputFloat(message);
        }
    }

    @Override
    public Double inputDouble(String message) {
        String input = inputString(message);
        try {
            if (Double.isInfinite(Double.valueOf(input))){
                console.writeln("Значение слишком большое!");
                return inputDouble(message);
            }
            return Double.valueOf(input);
        } catch (NumberFormatException e) {
            console.writeln("Введите дробное или целое число!");
            return inputDouble(message);
        }
    }

    @Override
    public Long inputLong(String message) {
        String input = inputString(message);
        try {
            return Long.valueOf(input);
        } catch (NumberFormatException e) {
            console.writeln("Введите целое число!");
            return inputLong(message);
        }
    }

    @Override
    public <E extends Enum<E>> E inputEnum(String message, Class<E> enumeration) {
        console.write("Список доступных для ввода значений:");
        for (E e : enumeration.getEnumConstants()) {
            console.write(" " + e.toString() + ";");
        }
        console.writeln("");

        String input = inputString(message).trim().toUpperCase();
        try {
            return Enum.valueOf(enumeration, input);
        } catch (IllegalArgumentException e) {
            console.writeln("Такого значения нет в списке!");
            return inputEnum(message, enumeration);
        }
    }

    @Override
    public Object build() {
        return null;
    }
}
