package lab6.commands;

import common.builders.FlatBuilder;
import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import common.network.Response;
import common.network.enums.ResponseType;
import lab6.collection.CollectionManager;

import java.util.List;

/**
 * Команда добавления элемента в коллекцию, если его значение превышает значение наибольшего элемента.
 */
public class AddIfMax extends Command {
    private final CollectionManager collectionManager;

    public AddIfMax(CollectionManager collectionManager) {
        super("add_if_max", "Добавляет новый элемент в коллекцию," +
                " если его значение превышает значение наибольшего элемента этой коллекции.");
        this.collectionManager = collectionManager;
    }

    private Flat getFlat(String[] args, Object object) {
        if (object != null) {
            return (Flat) object;
        } else if (args.length != 0) {
            return FlatBuilder.buildFromString(args);
        } else {
            return null;
        }
    }

    private Response executeCommand(Flat flat) throws CommandExecutionError {
        try {
            if (flat == null) {
                return Response.builder()
                        .setType(ResponseType.INPUT_FLAT)
                        .setMessage("Не удалось создать квартиру!")
                        .build();
            }

            List<Flat> flatList = collectionManager.sort();
            if (flatList.isEmpty()) {
                collectionManager.add(flat);
            } else {
                Flat maxFlat = flatList.get(0);
                if (maxFlat.compareTo(flat) < 0) {
                    collectionManager.add(flat);
                } else {
                    return Response.builder()
                            .setMessage("Квартира не добавлена!")
                            .build();
                }
            }
            return Response.builder()
                    .setMessage("Квартира добавлена!")
                    .build();
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        Flat flat = getFlat(args, null);
        return executeCommand(flat);
    }

    @Override
    public Response apply(String[] args, Object object) throws CommandExecutionError {
        Flat flat = getFlat(args, object);
        return executeCommand(flat);
    }
}
