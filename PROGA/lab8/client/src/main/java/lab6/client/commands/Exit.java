package lab6.client.commands;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import common.network.User;
import lab6.network.NetworkManager;
import lombok.SneakyThrows;

import static java.lang.System.exit;

/**
 * Команда выходит без сохранения коллекции.
 */
public class Exit extends Command {
    private final NetworkManager networkManager;

    public Exit(NetworkManager networkManager) {
        super("exit", "Завершает работу приложения.");
        this.networkManager = networkManager;
    }

    @SneakyThrows
    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        networkManager.close();
        exit(0);
        return null;
    }
}
