package lab6.client.commands;

import common.client.commands.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.CollectionManager;

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
