package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import lab6.collection.CollectionManager;

/**
 * Команда очищает коллекцию.
 */
public class Clear extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Clear(Console console, CollectionManager collectionManager) {
        super("clear", "Очищает коллекцию.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        console.writeln("Коллекция очищена!");
        return collectionManager.removeAll();
    }
}
