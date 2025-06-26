package lab6.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.client.CommandManager;
import common.network.Response;
import common.network.User;
import lab6.collection.CollectionManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда выводит историю команд.
 */
public class History extends common.client.Command {
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;

    public History(CommandManager commandManager, CollectionManager collectionManager) {
        super("history", "Показать последние 5 команд.");
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        if (commandManager.getHistory(user) != null) {
            List<String> history = commandManager.getHistory(user).stream()
                    .map(Command::getName)
                    .collect(Collectors.toList());
            return Response.builder()
                    .setMessage(history.stream()
                            .collect(Collectors.joining("\n")))
                    .setCollection(collectionManager.getAsList())
                    .build();
        }
        return Response.builder()
                .setCollection(collectionManager.getAsList())
                .setMessage("История пустая!")
        .build();

    }
}
