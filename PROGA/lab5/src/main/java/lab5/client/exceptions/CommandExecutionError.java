package lab5.client.exceptions;

/**
 * Исключение во время выполнения команды.
 */
public class CommandExecutionError extends Exception {
    public CommandExecutionError() {
        super("Ошибка во время выполнения команды!");
    }

    public CommandExecutionError(String message) {
        super(message);
    }
}
