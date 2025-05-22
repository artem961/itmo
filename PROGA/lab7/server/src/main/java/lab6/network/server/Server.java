package lab6.network.server;

import common.network.*;
import common.network.exceptions.NetworkException;
import lab6.network.NetworkManager;
import lab6.network.server.handlers.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;
import java.util.*;


public class Server implements Runnable, AutoCloseable {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private final InetSocketAddress serverAddress;
    private final DatagramChannel serverChannel;
    private final Selector selector;
    private volatile boolean isRunning = false;
    private final List<ServerModule> modules;
    private final Set<Channel> readableChannels;
    private final Map<SocketAddress, Request> requests;
    private final Map<SocketAddress, Response> responses;

    public Server(InetSocketAddress serverAddress, RequestHandler requestHandler) throws IOException {
        this.serverAddress = serverAddress;
        this.selector = Selector.open();
        this.serverChannel = DatagramChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.bind(serverAddress);
        serverChannel.register(selector, SelectionKey.OP_READ);
        NetworkManager networkManager = new NetworkManager(serverChannel);

        readableChannels = Collections.synchronizedSet(new HashSet<>());
        requests = Collections.synchronizedMap(new HashMap<>());
        responses = Collections.synchronizedMap(new HashMap<>());

        modules = new ArrayList<>();
        modules.add(new RequestReaderModule(networkManager, readableChannels, requests));
        modules.add(new RequestHandlerModule(requests, responses, requestHandler, 10));
        modules.add(new ResponseSenderModule(responses, networkManager));

        logger.info("Сервер инициализирован по адресу {}", serverAddress);
    }

    private void handleSelectionKey(SelectionKey key) {
        if (key.isReadable()) {
            synchronized (readableChannels) {
                readableChannels.add(key.channel());
                readableChannels.notify();
            }
        }
    }

    @Override
    public void run() {
        modules.forEach(module -> new Thread(module, module.getClass().getSimpleName()).start());
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
                modules.forEach(ServerModule::shutdown);
                if (selector.isOpen()) selector.close();
                if (serverChannel.isOpen()) serverChannel.close();
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
