package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import lab6.collection.CollectionManager;

import java.util.List;

/**
 * Команда выводит элементы коллекции в порядке убывания.
 */
public class PrintDescending extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public PrintDescending(Console console, CollectionManager collectionManager) {
        super("print_descending", "Вывести все элементы коллекции в порядке убывания.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        List<Flat> flatList = collectionManager.sort();
        if (flatList.size() == 0) {
            console.writeln("Коллекция пустая!");
            return true;
        }
        for (int i = flatList.size() - 1; i >= 0; i--) {
            console.writeln(flatList.get(i).toString());
        }
        return true;
    }
}
