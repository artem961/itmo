package lab5.client.commands;

import lab5.client.CommandManager;
import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;

import java.io.IOException;
import java.util.List;

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
        List<Command> history = commandManager.getHistory();

        for (Command command: history){
            if (command == null) continue;
            console.writeln(command.getName());
        }
        return true;
    }
}
