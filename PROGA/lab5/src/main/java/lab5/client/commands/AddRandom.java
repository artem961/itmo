package lab5.client.commands;

import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.*;

import java.time.LocalTime;
import java.util.Random;
import static java.lang.Math.round;

public class AddRandom extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public AddRandom(Console console, CollectionManager collectionManager) {
        super("add_random", "Добавляет случайный элемент в коллекцию.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    private int getRandom(){
        Random random = new Random(LocalTime.now().getNano());
        return (int) round(random.nextDouble(2, 10000));
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        try {
            Flat flat = new Flat(
                    "random_flat_" + String.valueOf(getRandom()),
                    new Coordinates(Float.valueOf(getRandom()), (double) getRandom()),
                    (float) getRandom(),
                    getRandom(),
                    Long.valueOf(getRandom()),
                    Furnish.NONE,
                    Transport.NONE,
                    new House("random_house_" + String.valueOf(getRandom()),
                            getRandom(),
                            Long.valueOf(getRandom())));

            collectionManager.add(flat);
            console.writeln("Случайная квартира успешно добавлена!");
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        }
        return true;
    }
}
