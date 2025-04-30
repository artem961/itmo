package lab6.network;

import common.network.*;
import common.network.enums.ResponseType;
import common.network.exceptions.NetworkException;
import common.network.exceptions.SerializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Server implements Runnable {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private final InetSocketAddress serverAddress;
    private final NetworkManager networkManager;
    private final DatagramChannel serverChannel;
    private final RequestHandler requestHandler;
    private final Selector selector;
    private volatile boolean isRunning = false;

    public Server(InetSocketAddress serverAddress, RequestHandler requestHandler) throws IOException {
        this.serverAddress = serverAddress;
        this.requestHandler = requestHandler;
        this.selector = Selector.open();
        this.serverChannel = DatagramChannel.open();

        this.networkManager = new NetworkManager(serverChannel);
        serverChannel.configureBlocking(false);
        serverChannel.bind(serverAddress);
        serverChannel.register(selector, SelectionKey.OP_READ);
        logger.info("Сервер инициализирован по адресу {}", serverAddress);
    }

    public boolean isRunning(){
        return isRunning;
    }

    private void handleSelectionKey(SelectionKey key) throws NetworkException, IOException {
        if (key.isReadable()) {
            Map<SocketAddress, byte[]> messages = networkManager.readFromChannel((DatagramChannel) key.channel());
            messages.forEach((address, bytes) -> logger.info("Получено новое сообщение от {}", address));
            Response response;
            for (Map.Entry<SocketAddress, byte[]> entry : messages.entrySet()) {
                try {
                    Request request = (Request) Serializer.deserialazeObject(entry.getValue());
                    response = requestHandler.handleRequest(request);
                } catch (SerializationException e) {
                    response = new Response.ResponseBuilder()
                            .setType(ResponseType.EXCEPTION)
                            .setMessage(e.getMessage())
                            .build();
                    logger.warn(e.getMessage());
                }
                networkManager.sendData(Serializer.serializeObject(response), entry.getKey());
                logger.info("Отправлен ответ на {}", entry.getKey());
            }
        }
    }

    @Override
    public void run() {
        isRunning = true;
        logger.info("Сервер запущен по адресу {}", this.serverAddress);

        try {
            while (true) {
                selector.select();

                if (!isRunning) break;

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handleSelectionKey(key);
                }
            }
        } catch (ClosedSelectorException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error("Ошибка при работе сервера!", e);
        } finally {
            try {
                if (selector.isOpen()) selector.close();
                if (serverChannel.isOpen()) serverChannel.close();
            } catch (IOException e) {
                logger.error("Ошибка при остановке сервера!", e);
            }
            logger.info("Сервер остановлен.");
        }
    }

    public void close() {
        logger.info("Остановка сервера.");
        isRunning = false;
        if (selector != null && selector.isOpen()) {
            selector.wakeup();
        }
    }
}
