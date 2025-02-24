package lab5.client.commands;

import lab5.client.exceptions.CommandExecutionError;

import static java.lang.System.exit;

public class Exit extends Command {
    public Exit() {
        super("exit", "Завершает программу без сохранения в файл.");
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        exit(0);
        return super.apply(args);
    }
}
