package lab5.client;

import lab5.client.commands.Command;
import lab5.client.exceptions.CommandNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();
    private final HistoryManager historyManager = new HistoryManager();

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public Command getCommand(String commandName) {
        return commands.stream()
                .filter(command -> commandName.equalsIgnoreCase(command.getName()))
                .findFirst()
                .orElseThrow(() -> new CommandNotFoundException(commandName));
    }

    public void addToHistory(Command command){
        historyManager.addToHisory(command);
    }

    public List<Command> getHistory(){
        return historyManager.getHistory();
    }
    /**
     * Получить все команды.
     * @return
     */
    public List<Command> getAllCommands() {
        return this.commands;
    }
}
