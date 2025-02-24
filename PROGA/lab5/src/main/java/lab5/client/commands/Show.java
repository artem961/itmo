package lab5.client.commands;

import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.models.Flat;

import java.io.IOException;
import java.util.List;

public class Show extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "Вывести все элементы коллекции.");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        List<Flat> flatList = collectionManager.sort();
        if (flatList.size() == 0){
            try {
                console.writeln("Коллекция пустая!");
            } catch (IOException e) {
                throw new CommandExecutionError();
            };
            return true;
        }

        for (Flat flat: flatList){
            try {
                console.writeln(flat.toString());
            } catch (IOException e) {
                throw new CommandExecutionError();
            }
        }
        return true;
    }
}
