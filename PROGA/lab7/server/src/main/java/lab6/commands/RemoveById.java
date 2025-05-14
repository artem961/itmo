package lab6.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import lab6.collection.CollectionManager;

import java.sql.SQLException;

/**
 * Команда удаляет элемент коллекции по id.
 */
public class RemoveById extends Command {
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id", "Удаляет элемент коллекции по id.");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        try {
            boolean rezult = collectionManager.removeById(Integer.valueOf(args[0]));
            if (rezult) {
                return Response.builder()
                        .setMessage("Элемент с id " + args[0] + " удалён!")
                        .build();
            } else {
                throw new CommandExecutionError("Элемента с id " + args[0] + " не существует!");
            }
        } catch (NumberFormatException e) {
            throw new CommandExecutionError("Неверный формат ввода id! Введите целое положительное число!");
        } catch (IndexOutOfBoundsException e) {
            throw new CommandExecutionError("Введите id!");
        }
    }
}
