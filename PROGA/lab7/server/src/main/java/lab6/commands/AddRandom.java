package lab6.commands;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;
import common.network.Response;
import lab6.collection.CollectionManager;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Random;

import static java.lang.Math.round;

/**
 * Команда добавлет случайный элемент в коллекцию.
 * В качестве аргумента можно указать количество добавляемых элементов.
 */
public class AddRandom extends Command {
    private final CollectionManager collectionManager;

    public AddRandom(CollectionManager collectionManager) {
        super("add_random", "Добавляет случайный элемент в коллекцию.");
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
    public Response apply(String[] args) throws CommandExecutionError {
        try {
            Integer count = 0;
            if (args.length == 0) count = 1;
            else count = Integer.parseInt(args[0]);

            if (count < 0) throw new CommandExecutionError("Введите положительное число!");
            //if (count > 100) throw new CommandExecutionError("Нельзя создать больше 100 объектов за раз!");

            for (int i = 0; i < count; i++) {
                collectionManager.add(makeRandomFlat());
            }
            return Response.builder()
                    .setMessage("Добавлены случайные квартиры в количестве " + count.toString() + " штук!")
                    .build();
        } catch (ValidationException | SQLException e) {
            throw new CommandExecutionError(e.getMessage());
        } catch (NumberFormatException e) {
            throw new CommandExecutionError("Введите целое положительное число в качеcтве аргумента!");
        } catch (CommandExecutionError e) {
            throw new CommandExecutionError(e.getMessage());
        }
    }
}
