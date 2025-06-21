package lab6.commands;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.exceptions.ValidationException;
import common.collection.models.*;
import common.network.Response;
import common.network.User;
import lab6.collection.CollectionManager;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

    private Flat makeRandomFlat() throws ValidationException {
        int randomInt = getRandom();
        String randomStr = String.valueOf(randomInt);

        return new Flat(
                "random_flat_" + randomStr,
                new Coordinates((float) randomInt, randomInt),
                (float) randomInt,
                randomInt,
                (long) randomInt,
                Furnish.NONE,
                Transport.NONE,
                new House("random_house_" + randomStr,
                        randomInt,
                        (long) randomInt));
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        try {
            Integer count = 0;
            if (args.length == 0) count = 1;
            else count = Integer.parseInt(args[0]);

            if (count < 0) throw new CommandExecutionError("Введите положительное число!");
            //if (count > 100) throw new CommandExecutionError("Нельзя создать больше 100 объектов за раз!");

            Set<Flat> flats = new HashSet<>();
            Flat flat;
            for (int i = 0; i < count; i++) {
                flat = makeRandomFlat();
                flat.setUserId(user.id());
                flats.add(flat);
            }
            collectionManager.add(flats);
            return Response.builder()
                    .setMessage("Добавлены случайные квартиры в количестве " + count.toString() + " штук!")
                    .build();
        } catch (ValidationException e) {
            throw new CommandExecutionError(e.getMessage());
        } catch (NumberFormatException e) {
            throw new CommandExecutionError("Введите целое положительное число в качеcтве аргумента!");
        } catch (CommandExecutionError e) {
            throw new CommandExecutionError(e.getMessage());
        }
    }
}
