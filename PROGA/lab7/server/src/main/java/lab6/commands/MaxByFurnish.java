package lab6.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.collection.models.Flat;
import common.collection.models.Furnish;
import common.network.Response;
import lab6.collection.CollectionManager;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда выводит элемент коллекции, значение поля furnish которого максимально.
 */
public class MaxByFurnish extends Command {
    private final CollectionManager collectionManager;

    public MaxByFurnish(CollectionManager collectionManager) {
        super("max_by_furnish", "Выводит элемент коллекции значение поля furnish которого максимально.");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        List<Flat> flatList = collectionManager.getAsList();
        Flat maxFlat = flatList.stream()
                .filter(flat -> flat.getFurnish() != null)
                .filter(flat -> flat.getFurnish().equals(Furnish.getMax()))
                .findFirst()
                .orElse(null);

        if (maxFlat != null) {
            return Response.builder()
                    .setCollection(List.of(maxFlat))
                    .build();
        } else {
            return Response.builder()
                    .setMessage("В коллекции нет элементов значение поля furnish которых максимально.")
                    .build();
        }
    }
}
