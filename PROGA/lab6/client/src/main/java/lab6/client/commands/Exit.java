package lab6.client.commands;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;

import static java.lang.System.exit;

/**
 * Команда выходит без сохранения коллекции.
 */
public class Exit extends Command {

    public Exit() {
        super("exit", "Завершает работу приложения.");
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        exit(0);
        return super.apply(args);
    }
}
