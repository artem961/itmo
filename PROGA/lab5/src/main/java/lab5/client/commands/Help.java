package lab5.client.commands;

import lab5.client.CommandManager;
import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;

import java.io.IOException;
import java.util.List;

public class Help extends Command{
    private final Console console;
    private final CommandManager commandManager;

    public Help(Console console, CommandManager commandManager) {
        super("help", "Вывести справку по доступным командам.");
        this.commandManager = commandManager;
        this.console = console;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        List<Command> commandList = commandManager.getAllCommands();
        for (Command command: commandList) {
            console.writeln(command.getName() + " - " + command.getDescription());
        }
        return true;
    }
}
