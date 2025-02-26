package lab5.client.commands;

import lab5.client.commands.forms.FlatForm;
import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.Flat;

import java.io.IOException;
import java.util.List;

public class AddIfMax extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    public AddIfMax(Console console, CollectionManager collectionManager) {
        super("add_if_max", "Добавляет новый элемент в коллекцию," +
                " если его значение превышает значение наибольшего элемента этой коллекции.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        try {
            Flat flat = new FlatForm(console).run();
            List<Flat> flatList = collectionManager.sort();
            if (flat.compareTo(flatList.get(flatList.size() - 1)) == 1){
                collectionManager.add(flat);
            }
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
        return true;
    }
}
