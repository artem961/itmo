package common;
/*
import common.client.commands.*;
import common.client.console.StandartConsole;
import common.collection.CollectionManager;
import common.collection.exceptions.ValidationException;
import common.collection.models.Flat;

import java.io.*;

import static java.lang.System.exit;

public class Main {
    private static void loadCollection(StandartConsole console, CollectionManager collectionManager, String fileName) throws ValidationException, IOException {
        collectionManager.loadCollection(fileName);
        if (collectionManager.backupManager.haveCorruptedElements()){
            console.writeln("В коллекции элементы с одинаковым id:");
            for (Flat flat: collectionManager.backupManager.getCorruptedElements()){
                console.writeln(flat.toString());
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

    public static void main(String... args) {
        StandartConsole console = new StandartConsole();
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager();
        Controller controller = new Controller(commandManager, console);

        String fileName = "";
        try{
            fileName = args[0];
            if (!(new File(fileName)).exists()) {
                console.writeln("Файл не существует!");
                exit(0);
            }
        } catch (Exception e) {
            console.writeln("Введите имя файла!");
            exit(0);
        }

        try {
            if (collectionManager.backupManager.checkBackupFile()) {
                if (console.read("Программа была завершена неправильно. Если хотите восстановить коллекцию введите yes: ").toLowerCase().equals("yes")) {
                    loadCollection(console, collectionManager, collectionManager.backupManager.getBackupFile());
                } else {
                    loadCollection(console, collectionManager, fileName);
                }
            } else{
                loadCollection(console, collectionManager, fileName);
            }
        } catch (Exception e) {
            console.writeln(e.getMessage());
            console.writeln("Не удалось загрузить коллекцию из файла!");
            exit(0);
        }

        //region registerCommands
        commandManager.registerCommand(new Help(console, commandManager));
        commandManager.registerCommand(new Info(console, collectionManager));
        commandManager.registerCommand(new Show(console, collectionManager));
        commandManager.registerCommand(new Add(console, collectionManager));
        commandManager.registerCommand(new AddRandom(console, collectionManager));
        commandManager.registerCommand(new Update(console, collectionManager));
        commandManager.registerCommand(new RemoveById(console, collectionManager));
        commandManager.registerCommand(new Clear(console, collectionManager));
        commandManager.registerCommand(new Save(console, collectionManager));
        commandManager.registerCommand(new ExecuteScript(console, controller, commandManager, collectionManager));
        commandManager.registerCommand(new Exit(collectionManager));
        commandManager.registerCommand(new AddIfMax(console, collectionManager));
        commandManager.registerCommand(new RemoveLower(console, collectionManager));
        commandManager.registerCommand(new History(console, commandManager));
        commandManager.registerCommand(new MaxByFurnish(console, collectionManager));
        commandManager.registerCommand(new FilterLessThanFurnish(console, collectionManager));
        commandManager.registerCommand(new PrintDescending(console, collectionManager));
        //endregion

        controller.run();
    }
}
*/