package lab6;

import common.Request;
import common.Response;
import common.ResponseType;
import common.client.Command;
import common.client.console.BufferedConsole;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.client.exceptions.CommandNotFoundException;
import lab6.client.CommandManager;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class Server {
    private final int port;
    private final NetworkManager networkManager;
    private final CommandManager commandManager;
    private final BufferedConsole console;

    public Server(int port, BufferedConsole console, CommandManager commandManager) throws SocketException, UnknownHostException {
        this.port = port;
        this.networkManager = new NetworkManager(port);
        this.commandManager = commandManager;
        this.console = console;
    }

    public Response handleRequest(Request request){
        String commandName = request.commandName();
        var data = NetworkManager.deserialazeObject(request.data());
        try {
            if (data instanceof String[]) {
                Command command = commandManager.getCommand(commandName);

                console.setBufferMode(true);
                command.apply((String[]) data);
                List<String> out = console.getBuffer();
                console.setBufferMode(false);
                console.clearBuffer();

                return new Response(ResponseType.OK,
                        NetworkManager.serializeObject(String.join("", out)));
            }
        } catch (CommandNotFoundException e) {
            return new Response(ResponseType.COMMAND_NOT_FOUND_EXCEPTION,
                    NetworkManager.serializeObject(commandName));
        }
        catch (Exception e) {
            return new Response(ResponseType.EXCEPTION,
                    NetworkManager.serializeObject(e.getMessage()));
        }
        return new Response(ResponseType.OK,
                NetworkManager.serializeObject("nigger"));
    }

    public void run(){
        while (true){
            try {
                DatagramPacket dp = networkManager.receivePacket();
                Request request = (Request) NetworkManager.deserialazeObject(dp.getData());
                InetAddress userAdress = dp.getAddress();
                int userPort = dp.getPort();

                Response response = handleRequest(request);
                networkManager.sendData(NetworkManager.serializeObject(response), userAdress, userPort);
            } catch (NetworkException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
