package lab5.client.commands;

import lab5.client.commands.forms.FlatForm;
import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.Flat;

import java.io.IOException;

public class Update extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Update(Console console, CollectionManager collectionManager) {
        super("update", "Обновляет элемент коллекции с заданным id.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        if (args.length != 1) throw new CommandExecutionError("Введите id элемента который хотите изменить!");
        try {
            Integer id = Integer.valueOf(args[0]);
            Flat flat = new FlatForm(console).run();
            collectionManager.update(flat, id);
            console.writeln("Элемент с id " + id.toString() + " успешно обновлён!");
        } catch (NumberFormatException e) {
            throw new CommandExecutionError("Введите целое положительное число!");
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
        return true;
    }
}
