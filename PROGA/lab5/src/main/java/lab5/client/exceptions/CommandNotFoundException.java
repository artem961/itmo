package lab5.client.exceptions;

/**
 * Исключение, когда команду не удалось найти.
 */
public class CommandNotFoundException extends RuntimeException{
    public CommandNotFoundException(String commandName) {
        super("Команда " + commandName + " не найдена!");
    }
}
