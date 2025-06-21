package lab6.client;

import common.client.CommandManager;
import common.client.Command;
import common.network.*;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.client.exceptions.CommandNotFoundException;
import common.collection.models.Flat;
import common.network.enums.ResponseType;
import common.network.exceptions.NetworkException;
import lab6.client.exceptions.ScriptRecursionException;
import lab6.network.NetworkManager;
import common.builders.FlatBuilder;

import java.util.*;

public class Controller implements Runnable {
    private final Console console;
    private final NetworkManager networkManager;
    private final CommandManager localCommandManager;
    private final AuthManager authManager;
    private final List<String> launchedScripts;

    public Controller(Console console, NetworkManager networkManager, CommandManager localCommandManager, AuthManager authManager) {
        this.console = console;
        this.networkManager = networkManager;
        this.localCommandManager = localCommandManager;
        this.authManager = authManager;
        this.launchedScripts = new ArrayList<>();
    }

    @Override
    public void run() {
        String input;
        while ((input = console.read("Введите команду: ")) != null) {
            console.writeln(handleInput(input));
        }
    }

    public void launchScript(String scriptName, List<String> script) {
        if (launchedScripts.contains(scriptName)) {
            launchedScripts.clear();
            throw new ScriptRecursionException("Рекурсивный вызов скрипта!");
        }
        launchedScripts.add(scriptName);

        for (String line : script) {
            console.writeln(line.trim());
            try {
                console.writeln(handleInput(line.trim()));
            } catch (ScriptRecursionException e) {
                console.writeln(e.getMessage());
                break;
            }
        }
        launchedScripts.remove(scriptName);
    }

    public String handleInput(String input) {
        try {
            if (!authManager.isAuth()) {
                return "Авторизуйтесь, чтобы исполнять команды!";
            } else {
                parseInput(input);
                return "";
            }
        } catch (CommandNotFoundException e) {
            return e.getMessage() + " Введите help для справки по командам.";
        } catch (ScriptRecursionException e) {
            throw e;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private void parseInput(String input) throws NetworkException, CommandExecutionError {
        String[] data = input
                .trim()
                .replace("\t", " ")
                .split("\\s+");

        String commandName = data[0];
        String[] args = Arrays.copyOfRange(data, 1, data.length);
        try {
            executeLocal(commandName, args);
        } catch (CommandNotFoundException e) {
            Request request = new Request(commandName, args, null, authManager.getUser());
            Response response = makeRequest(request);
            printResponse(request, response);
        }
    }

    private void printResponse(Request request, Response response) throws CommandExecutionError, common.network.exceptions.NetworkException {
        String commandName = request.commandName();
        String[] args = request.args();

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
            case INPUT_FLAT:
                Flat flat = new FlatBuilder(console).build();
                request = new Request(commandName, args, flat, authManager.getUser());
                Response resp = makeRequest(request);
                printResponse(request, resp);
                break;
            case GET_COMMANDS:
                List<String> commandsList = localCommandManager.getAllCommandsAsString();
                request = new Request(commandName, args, commandsList, authManager.getUser());
                response = makeRequest(request);
                printResponse(request, response);
                break;
            case AUTH:
                console.writeln(message);
        }
    }

    private void executeLocal(String commandName, String[] args) throws CommandExecutionError {
        Command command = this.localCommandManager.getCommand(commandName);
        command.apply(args, null, authManager.getUser());
    }

    private Response makeRequest(Request request) throws NetworkException {
        networkManager.sendData(Serializer.serializeObject(request));
        return (Response) Serializer.deserialazeObject(networkManager.receiveData());
    }

    private void printCollection(Collection<?> collection) {
        if (collection != null) {
            collection.forEach(object -> console.writeln(object.toString()));
        }
    }
}
