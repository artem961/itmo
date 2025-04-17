package lab6.client.commands;

import common.client.Command;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import lab6.client.CommandManager;
import lab6.client.Controller;
import lab6.collection.CollectionManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Команда исполняет скрипт.
 */
public class ExecuteScript extends Command {
    private final Console console;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final Controller controller;

    public ExecuteScript(Console console, Controller controller, CommandManager commandManager, CollectionManager collectionManager) {
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
            reader.close();
            executeScript(fileInput.toString(), filePath);
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
        List<String> scriptLines = Stream.of(script.split("\n"))
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
        controller.launchScript(filePath, scriptLines);
    }
}
