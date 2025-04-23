package lab6;

import common.client.console.BufferedConsole;
import common.client.console.Console;
import common.client.console.StandartConsole;
import common.collection.exceptions.ValidationException;
import common.network.NetworkException;
import common.client.CommandManager;
import lab6.client.commands.*;
import lab6.client.commands.Add;
import lab6.collection.CollectionManager;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static java.lang.System.exit;


public class Main {

    public static void main(String... args) throws IOException, NetworkException, ValidationException {
        BufferedConsole console = new BufferedConsole();
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager();
        RequestHandler requestHandler = new StandartRequestHandler(commandManager, console);
        Server server = null;
        try {
            server = new Server(new InetSocketAddress(InetAddress.getLocalHost(), 13531), requestHandler);
        }catch (BindException e){
            console.writeln("Этот порт уже занят!");
            exit(0);
        }

        //region loadCollection
        String fileName = getFileName(args);
        CollectionLoader collectionLoader = new CollectionLoader(console, collectionManager);
        collectionLoader.loadCollection(fileName);
        //endregion

        //region registerClientCommands
        commandManager.registerCommand(new Help(console, commandManager));
        commandManager.registerCommand(new Info(console, collectionManager));
        commandManager.registerCommand(new Show(console, collectionManager));
        commandManager.registerCommand(new Add(console, collectionManager));
        commandManager.registerCommand(new AddRandom(console, collectionManager));
        commandManager.registerCommand(new Update(console, collectionManager));
        commandManager.registerCommand(new RemoveById(console, collectionManager));
        commandManager.registerCommand(new Clear(console, collectionManager));
        commandManager.registerCommand(new AddIfMax(console, collectionManager));
        commandManager.registerCommand(new RemoveLower(console, collectionManager));
        commandManager.registerCommand(new History(console, commandManager));
        commandManager.registerCommand(new MaxByFurnish(console, collectionManager));
        commandManager.registerCommand(new FilterLessThanFurnish(console, collectionManager));
        commandManager.registerCommand(new PrintDescending(console, collectionManager));
        //endregion
        Thread serverThread = new Thread(server);
        serverThread.start();

        //region serverController
        Console serverConsole = new StandartConsole();
        CommandManager serverCommandManager = new CommandManager();
        Controller controller = new Controller(serverConsole, serverCommandManager);

        serverCommandManager.registerCommand(new Close(server, collectionManager));
        serverCommandManager.registerCommand(new Help(serverConsole, serverCommandManager));
        serverCommandManager.registerCommand(new Save(serverConsole, collectionManager));

        controller.run();
        //endregion
    }

    private static String getFileName(String[] args){
        String fileName = "re";
        try{
            fileName = args[0];
            if (!(new File(fileName)).exists()) {
                System.out.println("Файл не существует!");
               // exit(0);
            }
        } catch (Exception e) {
            System.out.println("Введите имя файла!");
           // exit(0);
        }
        return fileName;
    }
}
