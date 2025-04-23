package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import lab6.collection.CollectionManager;

import java.time.LocalTime;
import java.util.Random;

import static java.lang.Math.round;

/**
 * Команда добавлет случайный элемент в коллекцию.
 * В качестве аргумента можно указать количество добавляемых элементов.
 */
public class AddRandom extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public AddRandom(Console console, CollectionManager collectionManager) {
        super("add_random", "Добавляет случайный элемент в коллекцию.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    private int getRandom() {
        Random random = new Random(LocalTime.now().getNano());
        return (int) round(random.nextDouble(2, 10000));
    }

    private common.collection.models.Flat makeRandomFlat() throws ValidationException {
        common.collection.models.Flat flat = new Flat(
                "random_flat_" + String.valueOf(getRandom()),
                new common.collection.models.Coordinates(Float.valueOf(getRandom()), (double) getRandom()),
                (float) getRandom(),
                getRandom(),
                Long.valueOf(getRandom()),
                common.collection.models.Furnish.NONE,
                common.collection.models.Transport.NONE,
                new common.collection.models.House("random_house_" + String.valueOf(getRandom()),
                        getRandom(),
                        Long.valueOf(getRandom())));
        return flat;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        try {
            Integer count = 0;
            if (args.length == 0) count = 1;
            else count = Integer.parseInt(args[0]);

            if (count < 0) throw new CommandExecutionError("Введите положительное число!");
            if (count > 100) throw new CommandExecutionError("Нельзя создать больше 100 объектов за раз!");

            for (int i = 0; i < count; i++) {
                collectionManager.add(makeRandomFlat());
            }
            console.writeln("Добавлены случайные квартиры в количестве " + count.toString() + " штук!");
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        } catch (NumberFormatException e) {
            throw new CommandExecutionError("Введите целое положительное число в качеcтве аргумента!");
        } catch (CommandExecutionError e){
            throw new CommandExecutionError(e.getMessage());
        }
        return true;
    }
}
