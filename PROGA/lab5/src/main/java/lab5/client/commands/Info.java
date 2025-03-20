package lab5.client.commands;

import lab5.client.console.Console;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;
import lab5.collection.utils.CollectionInfo;

/**
 * Команда выводит информацию о коллекции.
 */
public class Info extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Info(Console console, CollectionManager collectionManager) {
        super("info", "Показывает информацию у коллекции.");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        collectionManager.updateCollectionInfo();
        CollectionInfo collectionInfo = collectionManager.getCollectionInfo();
        console.writeln(
                "Информация о коллекции: " +
                        "тип: " + collectionInfo.collectionType() +
                        "; количество элементов: " + collectionInfo.size() +
                        "; загружена из файла: " + collectionInfo.loadedFrom() + ";");
        return true;
    }
}
