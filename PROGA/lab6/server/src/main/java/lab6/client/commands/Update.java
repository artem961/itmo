package lab6.client.commands;

import common.builders.FlatBuilder;
import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import lab6.client.InputFlatException;
import lab6.collection.CollectionManager;

import java.util.Arrays;

/**
 * Команда обновляет элемент коллекции с заданным id.
 */
public class Update extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Update(Console console, CollectionManager collectionManager) {
        super("update", "Обновляет элемент коллекции с заданным id.");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    private Flat getFlat(String[] args) {
        if (args.length != 0) return new FlatBuilder(console, args).build();
        else throw new InputFlatException("Введите кваритру!");
    }

    private Flat getFlat(Object object){
        if (object == null) throw new InputFlatException("Введите кваритру!");
        return (Flat) object;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        if (args.length < 1) throw new CommandExecutionError("Введите id элемента который хотите изменить!");
        try {
            Integer id = Integer.valueOf(args[0]);
            Flat flat = getFlat(Arrays.copyOfRange(args, 1, args.length));
            if (flat == null) {
                console.writeln("Не удалось создать квартиру!");
                return false;
            }
            collectionManager.update(flat, id);
            console.writeln("Элемент с id " + id.toString() + " успешно обновлён!");
        } catch (NumberFormatException e) {
            throw new CommandExecutionError("Введите целое положительное число!");
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean apply(String[] args, Object object) throws CommandExecutionError {
        if (args.length < 1) throw new CommandExecutionError("Введите id элемента который хотите изменить!");
        try {
            Integer id = Integer.valueOf(args[0]);
            Flat flat = getFlat(object);
            System.out.println(id);
            System.out.println(flat);
            if (flat == null) {
                console.writeln("Не удалось создать квартиру!");
                return false;
            }
            collectionManager.update(flat, id);
            console.writeln("Элемент с id " + id.toString() + " успешно обновлён!");
        } catch (NumberFormatException e) {
            throw new CommandExecutionError("Введите целое положительное число!");
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
        return true;
    }
}
