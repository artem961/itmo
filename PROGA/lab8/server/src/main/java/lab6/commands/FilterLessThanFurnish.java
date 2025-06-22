package lab6.commands;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import common.collection.models.Furnish;
import common.network.Response;
import common.network.User;
import lab6.collection.CollectionManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Команда выводит элементы коллекции, значение которых меньше чем введённое значение furnish.
 */
public class FilterLessThanFurnish extends Command {
    private final CollectionManager collectionManager;

    public FilterLessThanFurnish(CollectionManager collectionManager) {
        super("filter_less_than_furnish", "Выводит все элементы значение поля furnish которых меньше заданного.");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        try {
            Furnish furnish = Furnish.valueOf(args[0].toUpperCase());
            List<Flat> collection = collectionManager.getAsList();

            List<Flat> fileredList = collection.stream()
                    .filter(flat -> flat.getFurnish() != null)
                    .filter(flat -> Furnish.compare(flat.getFurnish(), furnish) == -1)
                    .collect(Collectors.toList());

            return Response.builder()
                    .setCollection(fileredList)
                    .build();
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            return Response.builder()
                    .setMessage("Список доступных для ввода значений мебели:\n" +
                            Arrays.stream(Furnish.values())
                                    .map(Objects::toString)
                                    .collect(Collectors.joining("\n")))
                    .build();
        }
    }
}
