package lab5.client.commands;

import lab5.client.commands.builders.FlatBuilder;
import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.Flat;

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
