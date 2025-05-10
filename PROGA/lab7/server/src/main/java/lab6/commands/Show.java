package lab6.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import common.network.Response;
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
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
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
