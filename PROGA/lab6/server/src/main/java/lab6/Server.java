package lab6;

import common.network.*;
import common.client.console.BufferedConsole;
import lab6.client.CommandManager;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class Server {
    private final int port;
    private final NetworkManager networkManager;
    private final RequestHandler requestHandler;
    private final BufferedConsole console;
    private final Selector selector;

    public Server(int port, RequestHandler requestHandler, BufferedConsole console) throws IOException {
        this.port = port;
        this.networkManager = new NetworkManager(port);
        this.requestHandler = requestHandler;
        this.console = console;
        this.selector = Selector.open();
    }

    private void acceptConnection(InetAddress address, int port) throws NetworkException {
        try {
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            datagramChannel.bind(new InetSocketAddress(address, port));
            datagramChannel.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            throw new NetworkException("Не удалось подключить клиента!");
        }
    }

    public void run(){
        while (true){
            try {
                UserDataObject data = networkManager.receiveData();
                Request request = (Request) Serializer.deserialazeObject(data.data());
                InetAddress userAdress = data.userAddress();
                int userPort = data.userPort();

                Response response = requestHandler.handleRequest(request);
                networkManager.sendData(Serializer.serializeObject(response), userAdress, userPort);


            } catch (NetworkException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
