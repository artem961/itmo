package lab6;

import common.ConfigLoader;
import common.client.CommandManager;
import common.client.console.Console;
import common.client.console.StandartConsole;
import common.network.exceptions.NetworkException;
import lab6.client.AuthManager;
import lab6.client.Controller;
import lab6.client.commands.ExecuteScript;
import lab6.client.commands.Exit;
import lab6.network.NetworkManager;


import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import static java.lang.System.exit;

public class Main {
    public static void main(String... args) {
        
        Console console = new StandartConsole();
        CommandManager commandManager = new CommandManager();
        NetworkManager networkManager = null;
        try {
            networkManager = new NetworkManager();
            console.writeln("Подключение с сервером установлено!");
        } catch (NetworkException e) {
            console.writeln(e.getMessage());
            exit(0);
        }
        AuthManager authManager = new AuthManager(console, networkManager);
        Controller controller = new Controller(console, networkManager, commandManager, authManager);

        commandManager.registerCommand(new ExecuteScript(console, controller, commandManager));
        commandManager.registerCommand(new Exit(networkManager));

         authManager.auth();
        controller.run();
    }
}
