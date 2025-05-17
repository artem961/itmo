package lab6.network;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandNotFoundException;
import common.network.Request;
import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;
import common.client.CommandManager;
import lab6.collection.database.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

public class StandartRequestHandler extends RequestHandler {
    private final CommandManager commandManager;
    private final UserManager userManager;
    private final Console console;

    public StandartRequestHandler(CommandManager commandManager, Console console) {
        this.commandManager = commandManager;
        this.console = console;
        this.userManager = new UserManager();
    }

    @Override
    public Response handleRequest(Request request) {
        String commandName = request.commandName();
        String[] args = request.args();
        Object object = request.object();
        User user = request.user();
        try {
            switch (commandName) {
                case "auth":
                    return userManager.authUser(user);
                case "reg":
                    return userManager.regUser(user);
            }
            if (!userManager.checkUser(user)) {
                throw new RuntimeException("Пользователь не найден! Войдите или зарегистрируйтесь!");
            }

            Command command = commandManager.getCommand(commandName);
            Response response = command.apply(args, object, user);
            if (response.type() != ResponseType.GET_COMMANDS && response.type() != ResponseType.INPUT_FLAT) {
                commandManager.addToHistory(command, user);
            }
            return response;

        } catch (CommandNotFoundException e) {
            return Response.builder()
                    .setType(ResponseType.COMMAND_NOT_FOUND)
                    .setMessage(commandName)
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .setType(ResponseType.EXCEPTION)
                    .setMessage(e.getMessage())
                    .build();
        }
        //return new Response(ResponseType.OK, Serializer.serializeObject("nigger"));
    }
}
