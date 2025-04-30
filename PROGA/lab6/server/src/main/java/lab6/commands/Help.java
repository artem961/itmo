package lab6.commands;

import common.client.Command;
import common.client.exceptions.CommandExecutionError;
import common.client.CommandManager;
import common.network.Response;
import common.network.enums.ResponseType;


import java.util.List;


/**
 * Команда выводит справку по доступным командам.
 */
public class Help extends Command {
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", "Вывести справку по доступным командам.");
        this.commandManager = commandManager;
    }

    @Override
    public Response apply(String[] args) throws CommandExecutionError {
        return Response.builder()
                .setCollection(commandManager.getAllCommandsAsString())
                .build();
    }

    @Override
    public Response apply(String[] args, Object object) throws CommandExecutionError {
        if (object == null) {
            return Response.builder()
                    .setType(ResponseType.GET_COMMANDS)
                    .build();
        }

        @SuppressWarnings("unchecked")
        List<String> clientCommands = (List<String>) object;
        List<String> commandList = commandManager.getAllCommandsAsString();
        commandList.addAll(clientCommands);
        return Response.builder()
                .setCollection(commandList)
                .build();
    }
}
