package lab5;

import lab5.client.CommandManager;
import lab5.client.Controller;
import lab5.client.commands.Help;
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
        /*
        try {
            CollectionManager collectionManager = new CollectionManager();
            collectionManager.loadCollection("test.json");
            collectionManager.saveCollection("test.json");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
         */
        CommandManager commandManager = new CommandManager();
        StandartConsole console = new StandartConsole();
        commandManager.registerCommand(new Help(console, commandManager));

        Controller controller = new Controller(commandManager, console);
        controller.run();

    }
}
