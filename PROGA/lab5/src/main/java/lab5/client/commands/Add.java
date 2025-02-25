package lab5.client.commands;

import lab5.client.commands.utility.CoordinatesForm;
import lab5.client.commands.utility.HouseForm;
import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.models.Coordinates;
import lab5.collection.models.House;

import java.io.IOException;

public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Add(Console console, CollectionManager collectionManager) {
        super("add", "Добавляет новый элемент в коллекцию.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {

        try {
            Coordinates coordinates = new CoordinatesForm(console).run();
            //House house = new HouseForm(console).run();
            console.writeln(coordinates.toString());
            //console.writeln(house.toString());
        } catch (IOException e) {
            throw new CommandExecutionError(e.getMessage());
        }

        return true;
    }
}
