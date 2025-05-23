package lab5.client.commands;

import lab5.client.commands.builders.FlatBuilder;
import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.Flat;

import java.util.List;

/**
 * Команда добавления элемента в коллекцию, если его значение превышает значение наибольшего элемента.
 */
public class AddIfMax extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public AddIfMax(Console console, CollectionManager collectionManager) {
        super("add_if_max", "Добавляет новый элемент в коллекцию," +
                " если его значение превышает значение наибольшего элемента этой коллекции.");
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

            List<Flat> flatList = collectionManager.sort();
            if (flatList.size() == 0) {
                collectionManager.add(flat);
            } else if (flat.compareTo(flatList.get(flatList.size() - 1)) == 1) {
                collectionManager.add(flat);
            } else {
                console.writeln("Квартира не добавлена!");
                return false;
            }
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
        console.writeln("Квартира добавлена!");
        return true;
    }
}
