package lab6.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;
import lab6.collection.CollectionManager;

import java.sql.SQLException;

/**
 * Команда очищает коллекцию.
 */
public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "Удаляет все объекты данного пользователя.");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        collectionManager.removeAll(user.id());

        return Response.builder()
                .setMessage("Все элемеенты пользователя " + user.name() + " удалены!")
                .build();
    }
}
