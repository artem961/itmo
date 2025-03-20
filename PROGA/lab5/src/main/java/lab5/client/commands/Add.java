package lab5.client.commands;

import lab5.client.commands.builders.FlatBuilder;
import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.Flat;

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
