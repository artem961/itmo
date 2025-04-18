package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import common.collection.models.Furnish;
import lab6.collection.CollectionManager;

import java.util.List;

/**
 * Команда выводит элементы коллекции, значение которых меньше чем введённое значение furnish.
 */
public class FilterLessThanFurnish extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public FilterLessThanFurnish(Console console, CollectionManager collectionManager) {
        super("filter_less_than_furnish", "Выводит все элементы значение поля furnish которых меньше заданного.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        try {
            Furnish furnish = Furnish.valueOf(args[0].toUpperCase());
            List<Flat> flatList = collectionManager.sort();
            for (Flat flat : flatList) {
                if (flat.getFurnish() == null) continue;
                if (Furnish.compare(flat.getFurnish(), furnish) == -1) {
                    console.writeln(flat.toString());
                }
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            console.write("Список доступных для ввода значений мебели:");
            for (Furnish furnish : Furnish.values()) {
                console.write(" " + furnish.toString() + ";");
            }
            console.writeln("");
            throw new CommandExecutionError("Введите доступное значение furnish!");
        }
        return true;
    }
}
