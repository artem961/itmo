package lab6;

import common.client.CommandManager;
import common.client.Command;
import common.network.*;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.client.exceptions.CommandNotFoundException;
import common.collection.models.Flat;
import lab6.NetworkManager;
import common.builders.FlatBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер, запрашивающий у пользователя команду и исполняющий их.
 */
public class Controller {
    private final Console console;
    private CommandManager CommandManager;

    public Controller(Console console, CommandManager commandManager) {
        this.console = console;
        this.CommandManager = commandManager;
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
        } catch (Exception e){
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
        executeLocal(commandName, args);
    }

    private void executeLocal(String commandName, String[] args) throws CommandExecutionError {
        Command command = this.CommandManager.getCommand(commandName);
        command.apply(args);
    }
}

