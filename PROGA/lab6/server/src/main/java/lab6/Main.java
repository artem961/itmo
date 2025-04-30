package lab6;

import common.client.console.Console;
import common.client.console.StandartConsole;
import common.collection.exceptions.ValidationException;
import common.network.exceptions.NetworkException;
import common.client.CommandManager;
import lab6.collection.CollectionManager;
import lab6.collection.utils.CollectionLoader;
import lab6.commands.*;
import lab6.commands.server.Close;
import lab6.commands.server.Save;
import lab6.commands.server.Status;
import lab6.network.RequestHandler;
import lab6.network.Server;
import lab6.network.StandartRequestHandler;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

import static java.lang.System.exit;


public class Main {
    public static void main(String... args) throws IOException, NetworkException, ValidationException {
        Console console = new StandartConsole();
        CommandManager commandManager = new CommandManager();
        CollectionManager collectionManager = new CollectionManager();
        RequestHandler requestHandler = new StandartRequestHandler(commandManager, console);
        Server server = null;
        try {
            server = new Server(new InetSocketAddress("192.168.10.80", 13531), requestHandler);
        } catch (BindException e) {
            console.writeln("Этот порт уже занят!");
            exit(0);
        } catch (Exception e) {
            System.out.println(e);
        }

        //region loadCollection
        String fileName = getFileName(args);
        CollectionLoader collectionLoader = new CollectionLoader(console, collectionManager);
        collectionLoader.loadCollection(fileName);
        //endregion

        //region registerClientCommands
        commandManager.registerCommand(new AddIfMax(collectionManager));
        commandManager.registerCommand(new Add(collectionManager));
        commandManager.registerCommand(new AddRandom(collectionManager));
        commandManager.registerCommand(new Update(collectionManager));
        commandManager.registerCommand(new Clear(collectionManager));
        commandManager.registerCommand(new Show(collectionManager));
        commandManager.registerCommand(new Help(commandManager));
        commandManager.registerCommand(new Info(collectionManager));
        commandManager.registerCommand(new RemoveById(collectionManager));
        commandManager.registerCommand(new RemoveLower(collectionManager));
        commandManager.registerCommand(new History(commandManager));
        commandManager.registerCommand(new MaxByFurnish(collectionManager));
        commandManager.registerCommand(new FilterLessThanFurnish(collectionManager));
        commandManager.registerCommand(new PrintDescending(collectionManager));
        //endregion

        Thread serverThread = new Thread(server);
        serverThread.start();

        //region serverController
        Console serverConsole = new StandartConsole();
        CommandManager serverCommandManager = new CommandManager();
        Controller controller = new Controller(serverConsole, serverCommandManager);

        serverCommandManager.registerCommand(new Close(server, collectionManager));
        serverCommandManager.registerCommand(new Help(serverCommandManager));
        serverCommandManager.registerCommand(new Save(collectionManager));
        serverCommandManager.registerCommand(new Status(server));

        controller.run();
        //endregion
    }

    private static String getFileName(String[] args) {
        String fileName = "";
        try {
            fileName = args[0];
            if (!(new File(fileName)).exists()) {
                System.out.println("Файл не существует!");
                exit(0);
            }
        } catch (Exception e) {
            System.out.println("Введите имя файла!");
            exit(0);
        }
        return fileName;
    }
}
