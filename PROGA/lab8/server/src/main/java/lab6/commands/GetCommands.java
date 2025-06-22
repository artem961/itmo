package lab6.commands;

import common.client.Command;
import common.client.CommandInfo;
import common.client.exceptions.CommandExecutionError;
import common.client.CommandManager;
import common.network.Response;
import common.network.User;
import common.network.enums.ResponseType;


import java.util.List;


/**
 * Команда выводит справку по доступным командам.
 */
public class GetCommands extends Command {
    private final CommandManager commandManager;

    public GetCommands(CommandManager commandManager) {
        super("get_commands", "Получить доступные команды.");
        this.commandManager = commandManager;
        this.setClientAvailable(false);
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
        List<CommandInfo> commandInfoList = commandManager.getAllCommands().stream()
                .map(Command::getInfo)
                .toList();
        return Response.builder()
                .setCollection(commandInfoList)
                .build();
    }
}
