package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import lab6.client.CommandManager;

import java.util.List;

/**
 * Команда выводит справку по доступным командам.
 */
public class Help extends common.client.Command {
    private final Console console;
    private final CommandManager commandManager;

    public Help(Console console, CommandManager commandManager) {
        super("help", "Вывести справку по доступным командам.");
        this.commandManager = commandManager;
        this.console = console;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        List<common.client.Command> commandList = commandManager.getAllCommands();
        for (Command command : commandList) {
            console.writeln(command.getName() + " - " + command.getDescription());
        }
        return true;
    }
}
