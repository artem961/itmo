package lab6.network;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandNotFoundException;
import common.network.Request;
import common.network.Response;
import common.network.enums.ResponseType;
import common.client.CommandManager;

public class StandartRequestHandler extends RequestHandler {
    private final CommandManager commandManager;
    private final Console console;

    public StandartRequestHandler(CommandManager commandManager, Console console) {
        this.commandManager = commandManager;
        this.console = console;
    }

    @Override
    public Response handleRequest(Request request) {
        String commandName = request.commandName();
        String[] args = request.args();
        Object object = request.object();
        try {
            Command command = commandManager.getCommand(commandName);
            Response response = command.apply(args, object);
            if (response.type() != ResponseType.GET_COMMANDS) {
                commandManager.addToHistory(command);
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
