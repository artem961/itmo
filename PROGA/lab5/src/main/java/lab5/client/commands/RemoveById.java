package lab5.client.commands;

import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;

/**
 * Команда удаляет элемент коллекции по id.
 */
public class RemoveById extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id", "Удаляет элемент коллекции по id.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        try {
            boolean rezult = collectionManager.removeById(Integer.valueOf(args[0]));
            if (rezult) console.writeln("Элемент с id " + args[0] + " удалён!");
            else console.writeln("Элемента с id " + args[0] + " не существует!");
            return rezult;
        } catch (NumberFormatException e) {
            throw new CommandExecutionError("Неверный формат ввода id! Введите целое положительное число!");
        } catch (IndexOutOfBoundsException e) {
            throw new CommandExecutionError("Введите id!");
        }
    }
}
