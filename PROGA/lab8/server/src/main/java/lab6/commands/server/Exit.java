package lab6.commands.server;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import common.network.User;

import static java.lang.System.exit;

/**
 * Команда выходит без сохранения коллекции.
 */
public class Exit extends Command {

    public Exit() {
        super("exit", "Завершает работу приложения.");
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        exit(0);
        return null;
    }
}
