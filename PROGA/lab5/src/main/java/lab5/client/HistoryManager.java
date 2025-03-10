package lab5.client;

import lab5.client.commands.Command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Менеджер истории команд.
 */
public class HistoryManager {
    /**
     * Класс хранящий историю команд.
     */
    private class History {
        private final int maxSize;
        Deque<Command> hisory;

        private History(int maxSize) {
            this.maxSize = maxSize;
            hisory = new ArrayDeque<>(maxSize);
        }

        /**
         * Добавить команду в историю.
         *
         * @param command
         */
        private void add(Command command) {
            if (hisory.size() == maxSize) hisory.removeFirst();
            hisory.addLast(command);
        }

        /**
         * Получить список команд.
         *
         * @return
         */
        private List<Command> toList() {
            return hisory.stream().toList();
        }
    }

    private History history;

    public HistoryManager() {
        history = new History(15);
    }

    /**
     * Добавить команду в историю.
     *
     * @param command
     */
    public void addToHisory(Command command) {
        history.add(command);
    }

    /**
     * Получить историю.
     *
     * @return
     */
    public List<Command> getHistory() {
        return history.toList();
    }

}
