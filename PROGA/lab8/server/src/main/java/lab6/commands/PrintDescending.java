package lab6.commands;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import common.network.Response;
import common.network.User;
import lab6.collection.CollectionManager;

import java.util.Collections;
import java.util.List;

/**
 * Команда выводит элементы коллекции в порядке убывания.
 */
public class PrintDescending extends Command {
    private final CollectionManager collectionManager;

    public PrintDescending(CollectionManager collectionManager) {
        super("print_descending", "Вывести все элементы коллекции в порядке убывания.");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        List<Flat> flatList = collectionManager.sort();

        if (flatList.size() == 0) {
            return Response.builder()
                    .setMessage("Коллекция пустая!")
                    .build();
        }

        Collections.reverse(flatList);
        return Response.builder()
                .setCollection(flatList)
                .build();
    }
}
