package lab6.network.server;

import common.network.Request;
import common.network.Serializer;
import common.network.exceptions.NetworkException;
import lab6.network.NetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class RequestReaderModule {
    private static final Logger logger = LogManager.getLogger(RequestReaderModule.class);
    private final Map<SelectionKey, Queue<Request>> requests;
    private final NetworkManager networkManager;
    private final ExecutorService readerPool;

    public RequestReaderModule(NetworkManager networkManager,
                               Map<SelectionKey, Queue<Request>> requests) {
        this.networkManager = networkManager;
        this.requests = requests;
        this.readerPool = Executors.newCachedThreadPool();
        logger.info("Проинициализирован модуль чтения запросов.");
    }

    public void handleRead(SelectionKey key) {
        readerPool.submit(() -> {
            try {
                SocketChannel channel = (SocketChannel) key.channel();

                byte[] data = networkManager.readFromChannel(channel);
                Request request = (Request) Serializer.deserialazeObject(data);
                logger.info("Получено новое сообщение c адреса {}, пользователь {}, команда {}",
                        channel.getRemoteAddress(),
                        request.user().name(),
                        request.commandName());

                Queue<Request> queue = requests.get(key);
                if (queue != null) queue.add(request);

                synchronized (requests) {
                    requests.notify();
                }
            } catch (IOException | NetworkException e) {
                key.cancel();
                logger.info("Соединение принудительно разорвано!");
            } finally {
                key.interestOps(key.interestOps() | SelectionKey.OP_READ);
            }
        });
    }

    public void shutdown() {
        readerPool.shutdown();
        logger.info("Остановлен модуль чтения запросов.");
    }

}
