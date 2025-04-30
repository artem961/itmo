package lab6.commands;

import common.builders.FlatBuilder;
import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import common.network.Response;
import common.network.enums.ResponseType;
import lab6.collection.CollectionManager;

import java.util.List;


/**
 * Команда удаляет из коллекции все элементы, меньшие заданного.
 */
public class RemoveLower extends Command {
    private final CollectionManager collectionManager;

    public RemoveLower(CollectionManager collectionManager) {
        super("remove_lower", "Удаляет из коллекции все элементы, меньшие заданного.");
        this.collectionManager = collectionManager;
    }

    private Flat getFlat(String[] args)  {
        if (args.length != 0) {
            return FlatBuilder.buildFromString(args);
        } else {
            return null;
        }
    }

    private Flat getFlat(Object object) {
        if (object == null) {
            return null;
        } else {
            return (Flat) object;
        }
    }

    private Response executeCommand(Flat flat) {
        if (flat == null) {
            return Response.builder()
                    .setType(ResponseType.INPUT_FLAT)
                    .setMessage("Не удалось создать квартиру!")
                    .build();
        }

        List<Flat> flatList = collectionManager.getAsList();
        long elementsDelete = flatList.stream()
                .filter(flat1 -> flat1.compareTo(flat) < 0)
                .peek(collectionManager::remove)
                .count();

        return Response.builder()
                .setMessage("Удалено элементов: " + elementsDelete + ".")
                .build();
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        Flat flat = getFlat(args);
        return executeCommand(flat);
    }

    @Override
    public Response apply(String[] args, Object object) throws CommandExecutionError {
        Flat flat = getFlat(object);
        return executeCommand(flat);
    }
}
