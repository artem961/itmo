package lab6.client.commands;

import common.builders.FlatBuilder;
import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import lab6.client.InputFlatException;
import lab6.collection.CollectionManager;

/**
 * Команда добавления элемента в коллекцию.
 */
public class Add extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    public Add(Console console, CollectionManager collectionManager) {
        super("add", "Добавляет новый элемент в коллекцию.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    private Flat getFlat(String[] args) throws InputFlatException {
        if (args.length != 0) return new FlatBuilder(console, args).build();
        else throw new InputFlatException("Введите кваритру!");
    }

    private Flat getFlat(Object object){
        if (object == null) throw new InputFlatException("Введите кваритру!");
        return (Flat) object;
    }

    private boolean executeCommand(Flat flat) throws CommandExecutionError {
        try {
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

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        Flat flat = getFlat(args);
        return executeCommand(flat);
    }

    @Override
    public boolean apply(String[] args, Object object) throws CommandExecutionError {
        Flat flat = getFlat(object);
        return executeCommand(flat);
    }
}
