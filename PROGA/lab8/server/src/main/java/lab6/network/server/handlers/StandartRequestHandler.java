package lab6.network.server.handlers;

import common.client.Command;
import common.client.exceptions.CommandNotFoundException;
import common.network.Request;
import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;
import common.client.CommandManager;
import lab6.network.UserManager;

import java.util.ArrayList;

public class StandartRequestHandler extends RequestHandler {
    private final CommandManager commandManager;
    private final UserManager userManager;

    public StandartRequestHandler(CommandManager commandManager, UserManager userManager) {
        this.commandManager = commandManager;
        this.userManager = userManager;
    }

    @Override
    public Response handleRequest(Request request) {
        String commandName = request.commandName();
        String[] args = request.args();
        Object object = request.object();
        User user = request.user();

        if (args == null){
            args = new String[0];
        }
        try {
            if (!userManager.checkUser(user)) {
                switch (commandName) {
                    case "auth":
                        return userManager.authUser(user);
                    case "reg":
                        return userManager.regUser(user);
                }
                throw new RuntimeException("Пользователь не найден в текущей сессии! Войдите или зарегистрируйтесь!");
            } else if (commandName.equals("delete_user")) {
                return userManager.deleteUser(user);
            }

            Command command = commandManager.getCommand(commandName);
            Response response = command.apply(args, object, user);
            if (response.type() != ResponseType.GET_COMMANDS &&
                    response.type() != ResponseType.INPUT_FLAT &&
                    !commandName.equals("show") &&
                    !commandName.equals("get_commands")) {
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
