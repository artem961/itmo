package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import lab6.client.CommandManager;

import java.util.List;

/**
 * Команда выводит историю команд.
 */
public class History extends common.client.Command {
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

        for (Command command : history) {
            if (command == null) continue;
            console.writeln(command.getName());
        }
        return true;
    }
}
