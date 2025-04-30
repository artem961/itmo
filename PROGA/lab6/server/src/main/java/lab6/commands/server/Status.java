package lab6.commands.server;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import lab6.network.Server;

public class Status extends Command {
    private final Server server;

    public Status(Server server) {
        super("status", "Выводит статус работы сервера.");
        this.server = server;
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        Response response;
        if (server.isRunning()){
            response = Response.builder()
                    .setMessage("Сервер запущен.")
                    .build();
        } else {
            response = Response.builder()
                    .setMessage("Сервер остановлен.")
                    .build();
        }
        return response;
    }
}
