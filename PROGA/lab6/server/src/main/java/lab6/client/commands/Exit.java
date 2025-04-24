package lab6.client.commands;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;

import static java.lang.System.exit;

/**
 * Команда выходит без сохранения коллекции.
 */
public class Exit extends Command {

    public Exit() {
        super("exit", "Завершает программу.");
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        exit(0);
        return super.apply(args);
    }
}
