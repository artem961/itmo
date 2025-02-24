package lab5.client.commands;

import lab5.client.CommandManager;
import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;

public class History extends Command{
    private final Console console;
    private final CommandManager commandManager;

    public History(Console console, CommandManager commandManager) {
        super("history", "Показать последние 15 команд.");
        this.console = console;
        this.commandManager = commandManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        return super.apply(args);
    }
}
