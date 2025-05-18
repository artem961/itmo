package lab6.commands.server;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;
import lab6.network.server.Server;
import lab6.collection.CollectionManager;

/**
 * Команда выходит без сохранения коллекции.
 */
public class Close extends Command {
    private final Server server;
    private final CollectionManager collectionManager;

    public Close(Server server, CollectionManager collectionManager) {
        super("close", "Завершает работу сервера без сохранения в файл.");
        this.server = server;
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        server.close();
        return Response.builder()
                .setType(ResponseType.OK)
                .build();
    }
}
