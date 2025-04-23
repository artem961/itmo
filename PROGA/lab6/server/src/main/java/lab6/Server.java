package lab6;

import common.client.console.Console;
import common.network.*;
import common.client.console.BufferedConsole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Server implements Runnable{
    private static final Logger logger =  LogManager.getLogger(Server.class);
    private final InetSocketAddress serverAddress;
    private final NetworkManager networkManager;
    private final DatagramChannel serverChannel;
    private final RequestHandler requestHandler;
    private final Selector selector;

    public Server(InetSocketAddress serverAddress, RequestHandler requestHandler) throws IOException {
        this.serverAddress = serverAddress;
        this.requestHandler = requestHandler;
        this.selector = Selector.open();
        this.serverChannel = DatagramChannel.open();
        this.networkManager = new NetworkManager(serverChannel);
        serverChannel.bind(serverAddress);
        serverChannel.register(selector, SelectionKey.OP_READ);
        logger.info("Сервер инициализирован по адресу {}", serverAddress);
    }

    private void handleSelectionKey(SelectionKey key) throws NetworkException, IOException {
        if (key.isReadable()) {
            Map<SocketAddress, byte[]> messages = networkManager.readFromChannel((DatagramChannel) key.channel());
            messages.forEach((address, bytes) -> logger.info("Получено новое сообщение от {}", address));
            for (Map.Entry<SocketAddress, byte[]> entry : messages.entrySet()) {
                Response response;
                try {
                    Request request = (Request) Serializer.deserialazeObject(entry.getValue());
                    response = requestHandler.handleRequest(request);
                } catch (SerializationException e) {
                    response = new Response(ResponseType.EXCEPTION, Serializer.serializeObject(e));
                    logger.warn(e.getMessage());
                }
                networkManager.sendData(Serializer.serializeObject(response), entry.getKey());
                logger.info("Отправлен ответ на {}", entry.getKey());
            }
        }
    }

    @Override
    public void run(){
        logger.info("Сервер запущен по адресу {}", this.serverAddress);
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handleSelectionKey(key);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void stop() throws Exception {
        this.serverChannel.close();
        this.selector.wakeup();
        this.selector.close();
        logger.info("Сервер остановлен.");
    }
}
