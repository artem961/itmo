package lab5.client.commands;

import lab5.client.CommandManager;
import lab5.client.Controller;
import lab5.client.console.Console;
import lab5.client.console.StandartConsole;
import lab5.client.exceptions.CommandExecutionError;
import lab5.collection.CollectionManager;

import java.io.*;
import java.util.List;

public class ExecuteScript extends Command {
    private final StandartConsole console;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final Controller controller;

    public ExecuteScript(StandartConsole console, Controller controller, CommandManager commandManager, CollectionManager collectionManager) {
        super("execute_script", "Исполняет скрипт из указанного файла.");
        this.console = console;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.controller = controller;
    }

    @Override
    public boolean apply(String[] args) throws CommandExecutionError {
        try {
            String filePath = args[0];
            FileReader reader = new FileReader(filePath);
            StringBuilder fileInput = new StringBuilder();
            int data;
            while ((data = reader.read()) != -1) {
                fileInput.append((char) data);
            }
            executeScript(fileInput.toString(), filePath);
            reader.close();
        } catch (IndexOutOfBoundsException e) {
            throw new CommandExecutionError("Введите имя файла!");
        } catch (FileNotFoundException e) {
            throw new CommandExecutionError("Файл не существует!");
        } catch (IOException e) {
            throw new CommandExecutionError("Ошибка чтения файла!");
        }
        return true;
    }

    private void executeScript(String script, String filePath) throws CommandExecutionError {
        List<String> scriptLines = List.of(script.split("\n"));
        if (controller.getLaunchedScripts().contains(filePath)){
            controller.clearLaunchedScripts();
            throw new CommandExecutionError("Рекурсивный вызов скрипта!");
        }

        controller.addLaunchedScript(filePath);
        console.setScript(scriptLines);
        console.setScriptMode(true);
        try {
            controller.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
