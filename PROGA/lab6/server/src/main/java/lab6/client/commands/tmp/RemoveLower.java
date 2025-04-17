package lab6.client.commands;

import common.client.commands.Command;
import common.client.commands.builders.FlatBuilder;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.CollectionManager;
import common.collection.models.Flat;

import java.util.List;

/**
 * Команда удаляет из коллекции все элементы, меньшие заданного.
 */
public class RemoveLower extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveLower(Console console, CollectionManager collectionManager) {
        super("remove_lower", "Удаляет из коллекции все элементы, меньшие заданного.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    private Flat getFlat(String[] args) {
        if (args.length != 0) return new FlatBuilder(console, args).build();
        else return new FlatBuilder(console).build();
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        Flat flat = getFlat(args);
        if (flat == null) {
            console.writeln("Не удалось создать квартиру!");
            return false;
        }

        List<Flat> flatList = collectionManager.sort();
        int counterDeletedElements = 0;
        for (Flat f : flatList) {
            if (f.compareTo(flat) == -1) {
                collectionManager.remove(f);
                counterDeletedElements++;
            }
        }
        console.writeln("Удалено элементов: " + String.valueOf(counterDeletedElements) + ".");
        return true;
    }
}
