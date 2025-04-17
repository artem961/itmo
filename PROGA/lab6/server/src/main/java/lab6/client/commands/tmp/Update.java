package lab6.client.commands;

import common.client.Command;
import common.client.builders.FlatBuilder;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.CollectionManager;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;

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
        else return new FlatBuilder(console).build();
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
}
