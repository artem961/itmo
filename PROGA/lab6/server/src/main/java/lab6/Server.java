package lab6;

import common.network.*;
import common.client.console.BufferedConsole;
import lab6.client.CommandManager;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Server {
    private final InetSocketAddress serverAddress;
    private final NetworkManager networkManager;
    private final RequestHandler requestHandler;
    private final BufferedConsole console;
    private final Selector selector;
    private final DatagramChannel serverChannel;

    public Server(InetSocketAddress serverAddress, RequestHandler requestHandler, BufferedConsole console) throws IOException {
        this.serverAddress = serverAddress;
        this.requestHandler = requestHandler;
        this.console = console;
        this.selector = Selector.open();
        this.serverChannel = DatagramChannel.open();
        this.networkManager = new NetworkManager(serverChannel);
        serverChannel.bind(serverAddress);
        serverChannel.register(selector, SelectionKey.OP_READ);
    }


    public void run() throws IOException, NetworkException {
        while (true) {
            selector.select();

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isReadable()) {
                    Map<SocketAddress, byte[]> messages = networkManager.readFromChannel((DatagramChannel) key.channel());
                    //messages.forEach((address, bytes) -> System.out.println(address.toString() + "  :  " + bytes.length));
                    for (Map.Entry<SocketAddress, byte[]> entry: messages.entrySet()){
                        Response response;
                        try {
                            Request request = (Request) Serializer.deserialazeObject(entry.getValue());
                            response = requestHandler.handleRequest(request);
                        } catch (SerializationException e){
                            response = new Response(ResponseType.EXCEPTION, Serializer.serializeObject(e));

                        }
                        networkManager.sendData(Serializer.serializeObject(response), entry.getKey());
                    }

                }
            }
        }
        /*
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

         */
    }
}
