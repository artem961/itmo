package lab6.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.client.CommandManager;
import common.network.Response;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда выводит историю команд.
 */
public class History extends common.client.Command {
    private final CommandManager commandManager;

    public History(CommandManager commandManager) {
        super("history", "Показать последние 15 команд.");
        this.commandManager = commandManager;
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        List<String> history = commandManager.getHistory().stream()
                .map(Command::getName)
                .collect(Collectors.toList());

        return Response.builder()
                .setCollection(history)
                .build();
    }
}
