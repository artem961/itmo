package lab5;

import lab5.client.CommandManager;
import lab5.client.Controller;
import lab5.client.commands.*;
import lab5.client.console.StandartConsole;
import lab5.collection.CollectionManager;
import lab5.collection.DumpManager;
import lab5.collection.exceptions.ValidationException;
import lab5.collection.models.Coordinates;
import lab5.collection.models.Flat;
import lab5.collection.models.House;
import lab5.collection.models.Transport;

import java.io.*;

public class Main {
    public static void main(String... args){
        StandartConsole console = new StandartConsole();
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager();
        Controller controller = new Controller(commandManager, console);

        try {
            collectionManager.loadCollection("test.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }

        commandManager.registerCommand(new Help(console, commandManager));
        commandManager.registerCommand(new Info(console, collectionManager));
        commandManager.registerCommand(new Show(console, collectionManager));
        commandManager.registerCommand(new Add(console, collectionManager));
        commandManager.registerCommand(new Update(console, collectionManager));
        commandManager.registerCommand(new RemoveById(console, collectionManager));
        commandManager.registerCommand(new Clear(console, collectionManager));
        commandManager.registerCommand(new Save(console, collectionManager));
        commandManager.registerCommand(new ExecuteScript(console, controller, commandManager, collectionManager));
        commandManager.registerCommand(new Exit());
        commandManager.registerCommand(new AddIfMax(console, collectionManager));
        commandManager.registerCommand(new RemoveLower(console, collectionManager));
        commandManager.registerCommand(new History(console, commandManager));
        commandManager.registerCommand(new MaxByFurnish(console, collectionManager));
        commandManager.registerCommand(new FilterLessThanFurnish(console, collectionManager));
        commandManager.registerCommand(new PrintDescending(console, collectionManager));

        controller.run();
    }
}
