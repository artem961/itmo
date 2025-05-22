package lab6.network.server;

import common.network.Request;
import common.network.Response;
import lab6.network.server.handlers.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.SocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestHandlerModule extends ServerModule {
    private static final Logger logger = LogManager.getLogger(RequestHandlerModule.class);
    private final ExecutorService handlerThreadsPool;
    private final Map<SocketAddress, Request> requests;
    private final Map<SocketAddress, Response> responses;
    private final RequestHandler requestHandler;

    public RequestHandlerModule(Map<SocketAddress, Request> requests,
                                Map<SocketAddress, Response> responses,
                                RequestHandler requestHandler,
                                int handlerThreads) {
        this.requests = requests;
        this.responses = responses;
        this.requestHandler = requestHandler;
        this.handlerThreadsPool = Executors.newFixedThreadPool(handlerThreads);
    }

    @Override
    public void run() {
        logger.info("Запущен модуль обработки запросов.");
        while (isRunning) {
            try {
                synchronized (requests) {
                    while (requests.isEmpty() && isRunning) {
                        requests.wait();
                    }
                    if (!isRunning) break;

                    Set<SocketAddress> keys = requests.keySet();
                    for (SocketAddress key : keys) {
                        handlerThreadsPool.submit(() -> {
                            Response response = requestHandler.handleRequest(requests.remove(key));
                            logger.info("Обработан запрос от " + key + ".");
                            synchronized (responses) {
                                responses.put(key, response);
                                responses.notify();
                            }
                        });
                    }
                }
            } catch (InterruptedException e) {
                logger.warn("Поток был прерван.", e);
                Thread.currentThread().interrupt();
            }
        }
        logger.info("Остановлен модуль обработки запросов.");
    }

    @Override
    public void shutdown() {
        handlerThreadsPool.shutdown();
        super.shutdown();
    }
}
