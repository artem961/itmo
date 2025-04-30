package lab6.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import lab6.collection.CollectionManager;
import lab6.collection.utils.CollectionInfo;


/**
 * Команда выводит информацию о коллекции.
 */
public class Info extends Command {
    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", "Показывает информацию у коллекции.");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        collectionManager.updateCollectionInfo();
        CollectionInfo collectionInfo = collectionManager.getCollectionInfo();

        return Response.builder()
                .setMessage(
                        "Информация о коллекции: " +
                        "тип: " + collectionInfo.collectionType() +
                        "; количество элементов: " + collectionInfo.size() +
                        "; загружена из файла: " + collectionInfo.loadedFrom() + ";")
                .build();
    }
}
