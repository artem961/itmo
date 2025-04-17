package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import lab6.collection.CollectionManager;

/**
 * Команда добавления элемента в коллекцию.
 */
public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Add(Console console, CollectionManager collectionManager) {
        super("add", "Добавляет новый элемент в коллекцию.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    private Flat getFlat(String[] args) {
        if (args.length != 0) return new FlatBuilder(console, args).build();
        else return new FlatBuilder(console).build();
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        try {
            Flat flat = getFlat(args);
            if (flat == null) {
                console.writeln("Не удалось создать квартиру!");
                return false;
            }

            collectionManager.add(flat);
            console.writeln("Квартира успешно добавлена!");
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
        return true;
    }
}
