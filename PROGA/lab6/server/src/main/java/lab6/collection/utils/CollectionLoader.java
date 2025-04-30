package lab6.collection.utils;

import common.client.console.Console;
import common.collection.exceptions.ValidationException;
import lab6.collection.CollectionManager;

import java.io.IOException;

import static java.lang.System.exit;

public class CollectionLoader {
    private final Console console;
    private final CollectionManager collectionManager;

    public CollectionLoader(Console console, CollectionManager collectionManager){
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public void loadCollection(String fileName){
        try {
            if (collectionManager.backupManager.checkBackupFile()) {
                if (console.read("Программа была завершена неправильно. Если хотите восстановить коллекцию введите yes: ").toLowerCase().equals("yes")) {
                    loadFromFile(collectionManager.backupManager.getBackupFile());
                } else {
                    loadFromFile(fileName);
                }
            } else{
                loadFromFile(fileName);
            }
        } catch (Exception e) {
            console.writeln(e.getMessage());
            console.writeln("Не удалось загрузить коллекцию из файла!");
            exit(0);
        }
    }

    private void loadFromFile(String fileName) throws ValidationException, IOException {
        collectionManager.loadCollection(fileName);
        if (collectionManager.backupManager.haveCorruptedElements()){
            console.writeln("В коллекции элементы с одинаковым id:");
            for (Object obj: collectionManager.backupManager.getCorruptedElements()){
                console.writeln(obj.toString());
            }
            console.writeln("Элементы будут автоматически восстановлены.");
            if (console.read("Если хотите удалить элементы введите yes: ").equals("yes")){
                collectionManager.backupManager.clearCorruptedElements();
                console.writeln("Элементы удалены!");
                console.writeln("");
            } else{
                collectionManager.loadCorruptedElements();
                console.writeln("Элементы восстановлены!");
                console.writeln("");
            }
        }
    }
}
