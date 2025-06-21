package lab6.client.commands;

import common.client.Command;
import common.client.CommandManager;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import common.network.User;
import lab6.client.Controller;

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
    private final CommandManager commandManager;
    private final Controller controller;

    public ExecuteScript(Console console, Controller controller, CommandManager commandManager) {
        super("execute_script", "Исполняет скрипт из указанного файла.");
        this.commandManager = commandManager;
        this.controller = controller;
    }

    @Override
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError {
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
            return null;
        } catch (IndexOutOfBoundsException e) {
            throw new CommandExecutionError("Введите имя файла!");
        } catch (FileNotFoundException e) {
            throw new CommandExecutionError("Файл не существует!");
        } catch (IOException e) {
            throw new CommandExecutionError("Ошибка чтения файла!");
        }
    }

    private void executeScript(String script, String filePath) throws CommandExecutionError {
        List<String> scriptLines = Stream.of(script.split("\n"))
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
        controller.launchScript(filePath, scriptLines);
    }
}
