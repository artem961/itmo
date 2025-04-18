package lab6.client;

import common.network.Request;
import common.network.Response;
import common.network.ResponseType;
import common.network.Serializer;
import common.network.NetworkException;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.client.exceptions.CommandNotFoundException;
import common.collection.models.Flat;
import lab6.NetworkManager;
import common.builders.FlatBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Контроллер, запрашивающий у пользователя команду и исполняющий их.
 */
public class Controller {
    private final Console console;
    private final NetworkManager networkManager;
    //private final common.client.CommandManager commandManager;
    private final List<String> launchedScripts = new ArrayList<>();

    public Controller(Console console, NetworkManager networkManager) {
        this.console = console;
        this.networkManager = networkManager;
        //this.commandManager = commandManager;
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
     * Запустить скрипт.
     *
     * @param scriptName имя скрипта
     * @param script     скрипт
     */
    public void launchScript(String scriptName, List<String> script) {
        if (launchedScripts.contains(scriptName)) {
            launchedScripts.clear();
            throw new RuntimeException("Рекурсивный вызов скрипта!");
        }
        launchedScripts.add(scriptName);

        for (String line : script) {
            console.writeln(line.trim());
            console.writeln(handleInput(line.trim()));
        }
        launchedScripts.remove(scriptName);
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
    private void parseInput(String input) throws NetworkException, CommandExecutionError {
        String[] data = input
                .trim()
                .replace("\t", " ")
                .split("\\s+");

        String commandName = data[0];
        String[] args = Arrays.copyOfRange(data,1, data.length);

        Request request = new Request(commandName, args, null);
        Response response = makeRequest(request);
        parseResponce(commandName, args, response);
    }

    private void parseResponce(String commandName, String[] args, Response response) throws CommandExecutionError, NetworkException {
        ResponseType type = response.type();
        var data = Serializer.deserialazeObject(response.data());

        switch (response.type()){
            case OK:
                console.write(data.toString());
                break;
            case COMMAND_NOT_FOUND_EXCEPTION:
                throw new CommandNotFoundException(data.toString());
            case EXCEPTION:
                throw new CommandExecutionError(data.toString());
            case INPUT_FLAT:
                Flat flat = new FlatBuilder(console).build();
                Request request = new Request(commandName, args, flat);
                Response resp =  makeRequest(request);
                parseResponce(commandName, args, resp);
                break;
        }

    }

    private Response makeRequest(Request request) throws NetworkException, CommandExecutionError {
        networkManager.sendData(Serializer.serializeObject(request));
        Response response = (Response) Serializer.deserialazeObject(networkManager.receiveData());
        return response;
    }
}
