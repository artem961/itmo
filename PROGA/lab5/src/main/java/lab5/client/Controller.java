package lab5.client;

import lab5.client.commands.Command;
import lab5.client.console.StandartConsole;
import lab5.client.exceptions.CommandExecutionError;
import lab5.client.exceptions.CommandNotFoundException;

import java.util.Arrays;

public class Controller {
    private final StandartConsole console;
    private final CommandManager commandManager;

    public Controller(CommandManager commandManager, StandartConsole console){
        this.console = console;
        this.commandManager = commandManager;
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
                //currentRecursionLevel = 0;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String handleInput(String input){
        try {
            return input + (parseInput(input) == true? " успешно": " неуспешно") + " выполнил";
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
        return command.apply(Arrays.copyOfRange(data, 1, data.length));
    }
}
