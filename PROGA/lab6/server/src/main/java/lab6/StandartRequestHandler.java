package lab6;

import common.client.Command;
import common.client.console.BufferedConsole;
import common.client.exceptions.CommandNotFoundException;
import common.network.Request;
import common.network.Response;
import common.network.ResponseType;
import common.network.Serializer;
import lab6.client.CommandManager;
import lab6.client.InputFlatException;

import java.util.ArrayList;
import java.util.List;

public class StandartRequestHandler extends RequestHandler {
    private CommandManager commandManager;
    private BufferedConsole console;

    public StandartRequestHandler(CommandManager commandManager, BufferedConsole console) {
        this.commandManager = commandManager;
        this.console = console;
    }

    @Override
    public Response handleRequest(Request request) {
        String commandName = request.commandName();
        String[] args = request.args();
        Object object = request.flat();
        try {
                Command command = commandManager.getCommand(commandName);

                console.setBufferMode(true);
                command.apply(args, object);
                List<String> out = console.getBuffer();
                console.setBufferMode(false);
                console.clearBuffer();

                return new Response(ResponseType.OK,
                        Serializer.serializeObject(String.join("", out)));
        } catch (CommandNotFoundException e) {
            return new Response(ResponseType.COMMAND_NOT_FOUND_EXCEPTION,
                    Serializer.serializeObject(commandName));
        }catch (InputFlatException e){
            return new Response(ResponseType.INPUT_FLAT,
                    Serializer.serializeObject(e.getMessage()));
        }
        catch (Exception e) {
            return new Response(ResponseType.EXCEPTION,
                    Serializer.serializeObject(e.getMessage()));
        }
       //return new Response(ResponseType.OK, Serializer.serializeObject("nigger"));
    }
}
