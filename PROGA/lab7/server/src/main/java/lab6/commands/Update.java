package lab6.commands;

import common.builders.FlatBuilder;
import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;
import lab6.collection.CollectionManager;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * Команда обновляет элемент коллекции с заданным id.
 */
public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update", "Обновляет элемент коллекции с заданным id.");
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

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        if (args.length < 1) throw new CommandExecutionError("Введите id элемента который хотите изменить!");
        try {
            Integer id = Integer.valueOf(args[0]);
            if (collectionManager.isIdFree(id)){
                throw new CommandExecutionError("Элемента с таким id не существует!");
            } else if (!collectionManager.getAsList().stream()
                    .filter(flat -> flat.getId() == id)
                    .anyMatch(flat -> flat.getUserId() == user.id())){
                throw new CommandExecutionError("Этот элемент вам не принадлежит!");
            }

            Flat flat = getFlat(Arrays.copyOfRange(args, 1, args.length), object);
            if (flat == null) {
                return Response.builder()
                        .setType(ResponseType.INPUT_FLAT)
                        .setMessage("Не удалось создать квартиру!")
                        .build();
            }
            flat.setUserId(user.id());
            collectionManager.update(flat, id);
            return Response.builder()
                    .setMessage("Элемент с id " + id.toString() + " успешно обновлён!")
                    .build();
        } catch (NumberFormatException e) {
            throw new CommandExecutionError("Введите целое положительное число!");
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
    }
}
