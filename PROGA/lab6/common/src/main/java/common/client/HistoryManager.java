package common.client;

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
    private class HistoryStorage {
        Deque<Command> history;

        private HistoryStorage(int maxSize) {
            history = new ArrayDeque<>(maxSize);
        }

        /**
         * Добавить команду в историю.
         *
         * @param command
         */
        private void add(Command command) {
            history.add(command);
        }

        /**
         * Получить список команд.
         *
         * @return
         */
        private List<Command> toList() {
            return history.stream().toList();
        }
    }

    private HistoryStorage history;

    public HistoryManager() {
        history = new HistoryStorage(15);
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
