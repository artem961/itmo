package lab6.network.server;

import common.network.*;
import lab6.network.NetworkManager;
import lab6.network.server.handlers.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Server implements Runnable, AutoCloseable {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private final SocketAddress serverAddress;
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private volatile boolean isRunning = false;
    private final RequestReaderModule readerModule;
    private final RequestHandlerModule handlerModule;
    private final ResponseSenderModule senderModule;

    private final Map<SelectionKey, Queue<Request>> requests;
    private final Map<SelectionKey, Queue<Response>> responses;

    public Server(SocketAddress serverAddress, RequestHandler requestHandler) throws IOException {
        this.serverAddress = serverAddress;
        this.selector = Selector.open();
        this.serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(serverAddress);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        NetworkManager networkManager = new NetworkManager();

        requests = new ConcurrentHashMap<>();
        responses = new ConcurrentHashMap<>();

        readerModule = new RequestReaderModule(networkManager, requests);
        handlerModule = new RequestHandlerModule(requests, responses, requestHandler, 10);
        senderModule = new ResponseSenderModule(responses, networkManager);

        logger.info("Сервер инициализирован по адресу {}", serverAddress);
    }

    private void handleSelectionKey(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                System.out.println(selector.keys().size());
                acceptClient();
            } else if (key.isReadable()) {
                key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
                readerModule.handleRead(key);
            } else if (key.isWritable()) {
                if (!responses.get(key).isEmpty()) {
                    key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                    senderModule.sendToChannel(key);
                }
            }
        } catch (Exception e){
            key.cancel();
            responses.remove(key);
            requests.remove(key);
            logger.info("Клиент отключен");
        }
    }

    private void acceptClient() {
        try {
            SocketChannel clientChannel = serverSocketChannel.accept();
            clientChannel.configureBlocking(false);
            SelectionKey key = clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            requests.put(key, new ConcurrentLinkedQueue<>());
            responses.put(key, new ConcurrentLinkedQueue<>());
            logger.info("Принято подключение от {}", clientChannel.getRemoteAddress());
        } catch (IOException e) {
            logger.error("Не удалось принять подключение!", e);
        }
    }

    @Override
    public void run() {
        new Thread(handlerModule).start();
        isRunning = true;
        logger.info("Сервер запущен по адресу {}", serverAddress);

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
                handlerModule.shutdown();
                readerModule.shutdown();
                senderModule.shutdown();
                if (selector.isOpen()) selector.close();
                if (serverSocketChannel.isOpen()) serverSocketChannel.close();
            } catch (IOException e) {
                logger.error("Ошибка при остановке сервера!", e);
            }
            logger.info("Сервер остановлен.");
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void close() {
        logger.info("Остановка сервера...");
        isRunning = false;
        if (selector != null && selector.isOpen()) {
            selector.wakeup();
        }
    }
}
