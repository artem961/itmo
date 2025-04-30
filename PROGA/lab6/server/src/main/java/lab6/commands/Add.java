package lab6.commands;

import common.builders.FlatBuilder;
import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import common.network.Response;
import common.network.enums.ResponseType;
import lab6.collection.CollectionManager;

/**
 * Команда добавления элемента в коллекцию.
 */
public class Add extends Command {
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        super("add", "Добавляет новый элемент в коллекцию.");
        this.collectionManager = collectionManager;
    }

    private Flat getFlat(String[] args, Object object) {
        if (object != null){
            return (Flat) object;
        }else if (args.length != 0) {
            return FlatBuilder.buildFromString(args);
        } else{
            return null;
        }
    }

    private Response execute(Flat flat) throws CommandExecutionError {
        try {
            if (flat == null) {
                return Response.builder()
                        .setType(ResponseType.INPUT_FLAT)
                        .setMessage("Не удалось создать квартиру!")
                        .build();
            }

            collectionManager.add(flat);
            return Response.builder()
                    .setMessage("Квартира успешно добавлена!")
                    .build();
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        Flat flat = getFlat(args, null);
        return execute(flat);
    }

    @Override
    public Response apply(String[] args, Object object) throws CommandExecutionError {
        Flat flat = getFlat(args, object);
        return execute(flat);
    }
}
