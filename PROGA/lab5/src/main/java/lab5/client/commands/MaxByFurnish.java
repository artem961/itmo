package lab5.client.commands;

import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.models.Flat;
import lab5.collection.models.Furnish;

import java.util.List;

/**
 * Команда выводит элемент коллекции, значение поля furnish которого максимально.
 */
public class MaxByFurnish extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public MaxByFurnish(Console console, CollectionManager collectionManager) {
        super("max_by_furnish", "Выводит элемент коллекции значение поля furnish которого максимально.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        List<Flat> flatList = collectionManager.sort();
        for (Flat flat : flatList) {
            if (flat.getFurnish().equals(Furnish.getMax())) {
                console.writeln(flat.toString());
                return true;
            }
        }
        console.writeln("В коллекции нет элементов значение поля furnish которых максимально.");
        return true;
    }
}
