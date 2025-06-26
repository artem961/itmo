package lab6.commands;

import common.builders.FlatBuilder;
import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import common.network.Response;
import common.network.User;
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

    private Flat getFlat(String[] args, Object object) {
        if (object == null && args.length != 0) {
            return FlatBuilder.buildFromString(args);
        } else {
            return (Flat) object;
        }
    }

    private Response executeCommand(Flat flat) {
        if (flat == null) {
            return Response.builder()
                    .setType(ResponseType.INPUT_FLAT)
                    .setMessage("Не удалось создать квартиру!")
                    .setCollection(collectionManager.getAsList())
                    .build();
        }

        List<Flat> flatList = collectionManager.getAsList();

        long elementsDelete = flatList.stream()
                .filter(flat1 -> flat1.compareTo(flat) < 0 && flat1.getUserId() == flat.getUserId())
                .peek(collectionManager::remove)
                .count();

        return Response.builder()
                .setCollection(collectionManager.getAsList())
                .setMessage("Удалено элементов: " + elementsDelete + ".")
                .build();
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        Flat flat = getFlat(args, object);
        if (flat != null) {
            flat.setUserId(user.id());
        }
        return executeCommand(flat);
    }
}
