package lab6.commands.server;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import common.network.User;
import lab6.network.server.Server;

public class Status extends Command {
    private final Server server;

    public Status(Server server) {
        super("status", "Выводит статус работы сервера.");
        this.server = server;
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
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
