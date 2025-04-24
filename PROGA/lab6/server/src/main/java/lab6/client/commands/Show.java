package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import lab6.collection.CollectionManager;

import java.util.List;

/**
 * Команда выводит все элементы коллекции.
 */
public class Show extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "Вывести все элементы коллекции.");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        List<Flat> flatList = collectionManager.sort();
        if (flatList.size() == 0) {
            console.writeln("Коллекция пустая!");
            return true;
        }
        flatList.stream().forEach(flat -> console.writeln(flat.toString()));
        return true;
    }
}
