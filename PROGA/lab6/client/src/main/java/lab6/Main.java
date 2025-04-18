package lab6;

import common.client.console.Console;
import common.client.console.StandartConsole;
import common.collection.exceptions.ValidationException;
import common.network.NetworkException;
import lab6.client.Controller;


import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;


public class Main {
    public static void main(String... args) throws IOException, ValidationException, NetworkException, InterruptedException {
        InetAddress serverAdress = InetAddress.getLocalHost();
        int serverPort = 1488;

        Console console = new StandartConsole();
        NetworkManager networkManager = new NetworkManager(1487, serverPort, serverAdress);
        Controller controller = new Controller(console, networkManager);
        controller.run();
    }
}
