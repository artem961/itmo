package lab6.network.server;

import common.network.Request;
import common.network.Response;
import lab6.network.server.handlers.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RequestHandlerModule extends ServerModule {
    private static final Logger logger = LogManager.getLogger(RequestHandlerModule.class);
    private final ExecutorService handlerPool;
    private final Map<SelectionKey, Queue<Request>> requests;
    private final Map<SelectionKey, Queue<Response>> responses;
    private final RequestHandler requestHandler;

    public RequestHandlerModule(Map<SelectionKey, Queue<Request>> requests,
                                Map<SelectionKey, Queue<Response>> responses,
                                RequestHandler requestHandler,
                                int handlerThreads) {
        this.requests = requests;
        this.responses = responses;
        this.requestHandler = requestHandler;
        this.handlerPool = Executors.newFixedThreadPool(handlerThreads);
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

                    Set<SelectionKey> keys = requests.keySet();
                    for (SelectionKey key : keys) {
                        handleKey(key, requests.get(key));
                    }
                }
            } catch (InterruptedException e) {
                logger.warn("Поток был прерван.", e);
                Thread.currentThread().interrupt();
            }
        }
        logger.info("Остановлен модуль обработки запросов.");
    }

    private void handleKey(SelectionKey key, Queue<Request> requests) {
        while (!requests.isEmpty()) {
            Request request = requests.poll();
            handlerPool.submit(() -> {
                Response response = requestHandler.handleRequest(request);
                logger.info("Обработан запрос от пользователя {}, команда {}",
                        request.user().name(),
                        request.commandName());
                responses.get(key).add(response);
            });
        }
    }

    @Override
    public void shutdown() {
        handlerPool.shutdown();
        super.shutdown();
    }
}
