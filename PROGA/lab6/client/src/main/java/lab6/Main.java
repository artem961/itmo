package lab6;

import common.client.CommandManager;
import common.client.console.Console;
import common.client.console.StandartConsole;
import common.collection.exceptions.ValidationException;
import common.network.exceptions.NetworkException;
import lab6.client.Controller;
import lab6.client.commands.ExecuteScript;
import lab6.client.commands.Exit;
import lab6.network.NetworkManager;


import java.io.IOException;
import java.net.InetAddress;


public class Main {
    public static void main(String... args) throws IOException, ValidationException, NetworkException, InterruptedException {
        InetAddress serverAdress = InetAddress.getByName("192.168.10.80");
        int serverPort = 13531;

        Console console = new StandartConsole();
        CommandManager commandManager = new CommandManager();
        NetworkManager networkManager = new NetworkManager(serverPort, serverAdress);
        Controller controller = new Controller(console, networkManager, commandManager);

        commandManager.registerCommand(new ExecuteScript(console, controller, commandManager));
        commandManager.registerCommand(new Exit());





        controller.run();
    }
}
