package lab6;

import common.client.console.BufferedConsole;
import common.client.console.Console;
import common.collection.exceptions.ValidationException;
import common.network.NetworkException;
import lab6.client.CommandManager;
import lab6.client.commands.*;
import lab6.collection.CollectionManager;

import java.io.IOException;


public class Main {
    private static void loadCollection(Console console, CollectionManager collectionManager, String fileName) throws ValidationException, IOException {
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

    public static void main(String... args) throws IOException, NetworkException, ValidationException {
        BufferedConsole console = new BufferedConsole();
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager();

        /*
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

         */


        //region registerCommands
        commandManager.registerCommand(new Help(console, commandManager));
        commandManager.registerCommand(new Info(console, collectionManager));
        commandManager.registerCommand(new Show(console, collectionManager));
        //commandManager.registerCommand(new Add(console, collectionManager));
        commandManager.registerCommand(new AddRandom(console, collectionManager));
        //commandManager.registerCommand(new Update(console, collectionManager));
        //commandManager.registerCommand(new RemoveById(console, collectionManager));
        //commandManager.registerCommand(new Clear(console, collectionManager));
       // commandManager.registerCommand(new Save(console, collectionManager));
        //commandManager.registerCommand(new ExecuteScript(console, controller, commandManager, collectionManager));
        //commandManager.registerCommand(new Exit(collectionManager));
       // commandManager.registerCommand(new AddIfMax(console, collectionManager));
        //commandManager.registerCommand(new RemoveLower(console, collectionManager));
       // commandManager.registerCommand(new History(console, commandManager));
       // commandManager.registerCommand(new MaxByFurnish(console, collectionManager));
       // commandManager.registerCommand(new FilterLessThanFurnish(console, collectionManager));
       // commandManager.registerCommand(new PrintDescending(console, collectionManager));
        //endregion

        RequestHandler requestHandler = new StandartRequestHandler(commandManager, console);
        Server server = new Server(1488, requestHandler, console);
        server.run();
    }
}
