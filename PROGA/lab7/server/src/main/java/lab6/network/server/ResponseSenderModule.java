package lab6.network.server;

import common.network.Response;
import common.network.Serializer;
import common.network.exceptions.NetworkException;
import lab6.network.NetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.SocketAddress;
import java.util.Map;
import java.util.Set;

public class ResponseSenderModule extends ServerModule {
    private static final Logger logger = LogManager.getLogger(ResponseSenderModule.class);
    private final Map<SocketAddress, Response> responses;
    private final NetworkManager networkManager;

    public ResponseSenderModule(Map<SocketAddress, Response> responses,
                                NetworkManager networkManager) {
        this.responses = responses;
        this.networkManager = networkManager;
    }

    @Override
    public void run() {
        logger.info("Запущен модуль отправки ответов.");
        while (isRunning) {
            try {
                synchronized (responses) {
                    while (responses.isEmpty() && isRunning) {
                        responses.wait();
                    }
                    if (!isRunning) break;

                    Set<SocketAddress> keys = responses.keySet();
                    for (SocketAddress key : keys) {
                        try {
                            networkManager.sendData(Serializer.serializeObject(responses.remove(key)), key);
                            logger.info("Отправлен ответ на {}", key);
                        } catch (NetworkException e) {
                            logger.error(e);
                        }
                    }
                    responses.clear();


                }
            } catch (InterruptedException e) {
                logger.warn("Поток был прерван.", e);
                Thread.currentThread().interrupt();
            }
        }
        logger.info("Остановлен модуль отправки ответов.");
    }
}
