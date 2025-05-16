package lab6;

import common.ConfigLoader;
import common.client.console.Console;
import common.client.console.StandartConsole;
import common.client.CommandManager;
import lab6.collection.CollectionManager;
import lab6.commands.*;
import lab6.commands.server.Close;
import lab6.commands.server.Status;
import lab6.network.RequestHandler;
import lab6.network.Server;
import lab6.network.StandartRequestHandler;

import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static java.lang.System.exit;


public class Main {
    public static void main(String... args) {
        try {
            Console console = new StandartConsole();
            CommandManager commandManager = new CommandManager();
            CollectionManager collectionManager = new CollectionManager();
            RequestHandler requestHandler = new StandartRequestHandler(commandManager, console);
            Server server = null;
            try {
                //server = new Server(new InetSocketAddress("192.168.10.80", 13531), requestHandler);
                ConfigLoader configLoader = new ConfigLoader("connection.properties");
                server = new Server(new InetSocketAddress(
                        InetAddress.getByName(configLoader.get("server_address")),
                        Integer.valueOf(configLoader.get("server_port"))),
                        requestHandler);
            } catch (BindException e) {
                console.writeln("Этот порт уже занят!");
                exit(0);
            } catch (Exception e) {
                System.out.println(e);
            }

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
            serverCommandManager.registerCommand(new Status(server));

            controller.run();
            //endregion
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
