package lab6.network.server;

import common.network.Request;
import common.network.Serializer;
import common.network.exceptions.NetworkException;
import common.network.exceptions.SerializationException;
import lab6.network.NetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class RequestReaderModule extends ServerModule {
    private static final Logger logger = LogManager.getLogger(RequestReaderModule.class);
    private final Set<Channel> readableChannels;
    private final Map<SocketAddress, Request> requests;
    private final NetworkManager networkManager;

    public RequestReaderModule(NetworkManager networkManager,
                               Set<Channel> readableChannels,
                               Map<SocketAddress, Request> requests) {
        this.networkManager = networkManager;
        this.readableChannels = readableChannels;
        this.requests = requests;
    }

    @Override
    public void run() {
        logger.info("Запущен модуль чтения запросов.");
        while (isRunning) {
            try {
                synchronized (readableChannels) {
                    while (readableChannels.isEmpty() && isRunning) {
                        readableChannels.wait();
                    }
                    if (!isRunning) break;

                    Iterator<Channel> iterator = readableChannels.iterator();
                    while (iterator.hasNext()) {
                        try {
                            Map<SocketAddress, byte[]> messages = networkManager.readFromChannel((DatagramChannel) iterator.next());
                            iterator.remove();
                            messages.forEach((address, bytes) -> logger.info("Получено новое сообщение от {}", address));

                            for (Map.Entry<SocketAddress, byte[]> entry : messages.entrySet()) {
                                processMessage(entry.getKey(), entry.getValue());
                            }
                        } catch (IOException | NetworkException e) {
                            logger.error(e);
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.warn("Поток был прерван.", e);
                Thread.currentThread().interrupt();
            }
        }
        logger.info("Остановлен модуль чтения запросов.");
    }

    private void processMessage(SocketAddress address, byte[] message) {
        try {
            Request request = (Request) Serializer.deserialazeObject(message);
            synchronized (requests) {
                requests.put(address, request);
                requests.notify();
            }
        } catch (SerializationException e) {
            logger.warn(e);
        }
    }
}
