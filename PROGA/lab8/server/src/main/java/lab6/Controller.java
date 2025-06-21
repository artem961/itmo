package lab6;

import common.builders.FlatBuilder;
import common.client.CommandManager;
import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.client.exceptions.CommandNotFoundException;
import common.collection.models.Flat;
import common.network.Request;
import common.network.Response;
import common.network.Serializer;
import common.network.enums.ResponseType;
import common.network.exceptions.NetworkException;

import java.util.*;

/**
 * Контроллер, запрашивающий у пользователя команду и исполняющий их.
 */
public class Controller {
    private final Console console;
    private CommandManager commandManager;

    public Controller(Console console, CommandManager localCommandManager) {
        this.console = console;
        this.commandManager = localCommandManager;
    }

    /**
     * Начать работу с пользователем.
     */
    public void run() {
        String input;
        try {
            while ((input = this.console.read("Введите команду: ")) != null) {
                try {
                    console.writeln(handleInput(input));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Обработать пользовательский ввод.
     *
     * @param input
     * @return
     */
    public String handleInput(String input) {
        try {
            parseInput(input);
            return "";
        } catch (CommandNotFoundException e) {
            return e.getMessage() + " Введите help для справки по командам.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Выполнить парсинг пользовательского ввода.
     *
     * @param input
     * @throws CommandExecutionError
     */
    private void parseInput(String input) throws common.network.exceptions.NetworkException, CommandExecutionError {
        String[] data = input
                .trim()
                .replace("\t", " ")
                .split("\\s+");

        String commandName = data[0];
        String[] args = Arrays.copyOfRange(data, 1, data.length);

        Request request = new Request(commandName, args, Collections.emptyList(), null);
        Response response = executeCommand(request);
        printResponce(response);
    }

    private void printResponce(Response response) throws CommandExecutionError, common.network.exceptions.NetworkException {
        ResponseType type = response.type();
        String message = response.message();
        Collection<?> collection = response.collection();

        switch (type) {
            case OK:
                console.write(message);
                printCollection(collection);
                break;
            case COMMAND_NOT_FOUND:
                throw new CommandNotFoundException(message);
            case EXCEPTION:
                throw new CommandExecutionError(message);
        }
    }

    private Response executeCommand(Request request) throws CommandExecutionError {
        Command command = this.commandManager.getCommand(request.commandName());
        return command.apply(request.args(), request.object(), null);
    }

    private void printCollection(Collection<?> collection) {
        if (collection != null) {
            collection.forEach(object -> console.writeln(object.toString()));
        }
    }
}

