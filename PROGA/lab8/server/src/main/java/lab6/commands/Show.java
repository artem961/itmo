package lab6.commands;

import common.client.Command;
import common.client.CommandInfo;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import common.network.Response;
import common.network.User;
import lab6.collection.CollectionManager;

import java.util.List;

/**
 * Команда выводит все элементы коллекции.
 */
public class Show extends Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "Вывести все элементы коллекции.");
        this.collectionManager = collectionManager;
        this.setClientAvailable(false);
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        List<Flat> flatList = collectionManager.sort();
        if (flatList.size() == 0) {
            return Response.builder()
                    .setMessage("Коллекция пустая!")
                    .build();
        }
        return Response.builder()
                .setCollection(flatList)
                .build();
    }
}
