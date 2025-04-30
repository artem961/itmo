package lab6.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import common.network.enums.ResponseType;
import lab6.collection.CollectionManager;

/**
 * Команда очищает коллекцию.
 */
public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "Очищает коллекцию.");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        collectionManager.removeAll();
        return Response.builder()
                .setMessage("Коллекция очищена!")
                .build();
    }
}
