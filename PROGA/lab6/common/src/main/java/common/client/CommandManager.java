package common.client;

import common.client.exceptions.CommandNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Менеджер команд.
 */
public class CommandManager{
    private final List<Command> commands = new ArrayList<>();
    private final HistoryManager historyManager = new HistoryManager();

    /**
     * Зарегистрировать команду.
     *
     * @param command
     */
    public void registerCommand(Command command) {
        commands.add(command);
    }

    /**
     * Получить команду по имени.
     *
     * @param commandName
     * @return
     */
    public Command getCommand(String commandName) {
        return commands.stream()
                .filter(command -> commandName.equalsIgnoreCase(command.getName()))
                .findFirst()
                .orElseThrow(() -> new CommandNotFoundException(commandName));
    }

    /**
     * Добавить команлу в историю.
     *
     * @param command
     */
    public void addToHistory(Command command) {
        historyManager.addToHisory(command);
    }

    /**
     * Получить историю команд.
     *
     * @return
     */
    public List<Command> getHistory() {
        return historyManager.getHistory();
    }

    /**
     * Получить все команды.
     *
     * @return
     */
    public List<Command> getAllCommands() {
        return this.commands;
    }

    public List<String> getAllCommandsAsString(){
        return this.commands.stream()
                .map(Command::toString)
                .collect(Collectors.toList());
    }
}
