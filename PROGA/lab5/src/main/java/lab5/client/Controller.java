package lab5.client;

import lab5.client.commands.Command;
import lab5.client.commands.History;
import lab5.client.console.Console;
import lab5.client.console.StandartConsole;
import lab5.client.exceptions.CommandExecutionError;
import lab5.client.exceptions.CommandNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    private final Console console;
    private final CommandManager commandManager;
    private List<String> launchedScripts = new ArrayList<>();

    public Controller(CommandManager commandManager, Console console){
        this.console = console;
        this.commandManager = commandManager;
    }

    public void addLaunchedScript(String name){
        launchedScripts.add(name);
    }

    public List<String> getLaunchedScripts() {
        return launchedScripts;
    }

    public void clearLaunchedScripts(){
        this.launchedScripts.clear();
    }

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

    public String handleInput(String input){
        try {
            parseInput(input);
            return "";
        } catch (CommandExecutionError e) {
            return e.getMessage();
        } catch (CommandNotFoundException e){
            return e.getMessage() + " Введите help для справки по командам.";
        }
        //return "";
    }

    private boolean parseInput(String input) throws CommandExecutionError {
        String[] data = input.split(" ");
        String commandName = data[0];
        Command command = commandManager.getCommand(commandName);
        commandManager.addToHistory(command);
        return command.apply(Arrays.copyOfRange(data, 1, data.length));
    }
}
