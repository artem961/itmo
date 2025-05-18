package lab6;

import common.ConfigLoader;
import common.client.console.Console;
import common.client.console.StandartConsole;
import common.client.CommandManager;
import lab6.collection.CollectionManager;
import lab6.collection.database.connection.DBManager;
import lab6.commands.*;
import lab6.commands.server.Close;
import lab6.commands.server.Status;
import lab6.network.server.handlers.RequestHandler;
import lab6.network.server.Server;
import lab6.network.server.handlers.StandartRequestHandler;
import lab6.network.UserManager;

import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static java.lang.System.exit;


public class Main {
    public static void main(String... args) {
        try {
            Console console = new StandartConsole();
            CommandManager commandManager = new CommandManager();
            DBManager dbManager = new DBManager(5);
            CollectionManager collectionManager = new CollectionManager(dbManager);
            UserManager userManager = new UserManager(dbManager);
            RequestHandler requestHandler = new StandartRequestHandler(commandManager, userManager);
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

            Thread serverThread = new Thread(server, "Сервер");
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
