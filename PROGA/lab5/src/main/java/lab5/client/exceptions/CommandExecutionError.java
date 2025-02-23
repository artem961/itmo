package lab5.client.exceptions;

public class CommandExecutionError extends Exception {
    public CommandExecutionError() {
        super("Ошибка во время выполнения команды!");
    }
}
