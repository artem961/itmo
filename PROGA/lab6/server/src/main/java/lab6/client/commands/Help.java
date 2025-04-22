package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.client.CommandManager;
import lab6.client.GetCommandsException;

import java.util.List;
import java.util.Map;

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
    public boolean apply(String[] args, Object object) throws CommandExecutionError {
        if (object == null) {
            throw new GetCommandsException("Запрос у клиента его доступных команд");
        }

        Map<String, String> clientCommands = (Map<String, String>) object;
        Map<String, String> commandList = commandManager.getCommandsMap();
        commandList.putAll(clientCommands);
        commandList.forEach((name, discription) -> console.writeln(name + " - " + discription));
        return true;
    }
}
