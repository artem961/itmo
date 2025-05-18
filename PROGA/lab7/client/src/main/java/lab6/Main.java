package lab6;

import common.ConfigLoader;
import common.client.CommandManager;
import common.client.console.Console;
import common.client.console.StandartConsole;
import lab6.client.AuthManager;
import lab6.client.Controller;
import lab6.client.commands.ExecuteScript;
import lab6.client.commands.Exit;
import lab6.network.NetworkManager;


import java.io.IOException;
import java.net.InetAddress;


public class Main {
    public static void main(String... args) throws IOException {
        ConfigLoader configLoader = new ConfigLoader("connection.properties");
        InetAddress serverAdress = InetAddress.getByName(configLoader.get("server_address"));
        int serverPort = Integer.valueOf(configLoader.get("server_port"));

        Console console = new StandartConsole();
        CommandManager commandManager = new CommandManager();
        NetworkManager networkManager = new NetworkManager(serverPort, serverAdress);
        AuthManager authManager = new AuthManager(console, networkManager);
        Controller controller = new Controller(console, networkManager, commandManager, authManager);

        commandManager.registerCommand(new ExecuteScript(console, controller, commandManager));
        commandManager.registerCommand(new Exit());

        controller.run();
    }
}
