package lab6.network.server;

import common.network.Response;
import common.network.Serializer;
import common.network.exceptions.NetworkException;
import lab6.network.NetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResponseSenderModule {
    private static final Logger logger = LogManager.getLogger(ResponseSenderModule.class);
    private final Map<SelectionKey, Queue<Response>> responses;
    private final NetworkManager networkManager;
    private final ExecutorService senderPool;

    public ResponseSenderModule(Map<SelectionKey, Queue<Response>> responses,
                                NetworkManager networkManager) {
        this.responses = responses;
        this.networkManager = networkManager;
        this.senderPool = Executors.newCachedThreadPool();
        logger.info("Проинициализирован модуль отправки ответов.");
    }

    public void sendToChannel(SelectionKey key) {
        senderPool.submit(() -> {
            SocketChannel channel = (SocketChannel) key.channel();
            Queue<Response> responsesToSend = responses.get(key);

            try {
                if (responsesToSend == null) return;

                while (!responsesToSend.isEmpty()) {
                    Response response = responsesToSend.poll();
                    networkManager.sendData(Serializer.serializeObject(response), channel);
                    logger.info("Отправлен ответ на адрес {}",
                            channel.getRemoteAddress());
                }
            } catch (NetworkException | IOException e) {
                logger.debug(e);
                key.cancel();
            } finally {
                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
            }
        });
    }

    public void shutdown() {
        senderPool.shutdown();
        logger.info("Остановлен модуль отправки ответов.");
    }
}
