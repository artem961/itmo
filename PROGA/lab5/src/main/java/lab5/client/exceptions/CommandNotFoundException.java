package lab5.client.exceptions;

public class CommandNotFoundException extends ControllerException {
    public CommandNotFoundException(String commandName) {
        super("Команда " + commandName + " не найдена!");
    }
}
