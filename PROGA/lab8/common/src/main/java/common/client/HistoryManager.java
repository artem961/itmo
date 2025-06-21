package common.client;

import common.network.User;

import java.util.*;

/**
 * Менеджер истории команд.
 */
public class HistoryManager {
    /**
     * Класс хранящий историю команд.
     */
    private class HistoryStorage {
        private int maxSize;
        Map<User, Deque<Command>> history;


        private HistoryStorage(int maxSize) {
            this.maxSize = maxSize;
            history = new HashMap<>();
        }

        /**
         * Добавить команду в историю.
         *
         * @param command
         */
        private void add(Command command, User user) {
            history.computeIfAbsent(user, key -> new ArrayDeque<>(maxSize)).add(command);
        }

        /**
         * Получить список команд.
         *
         * @return
         */
        private List<Command> toList(User user) {
            Deque<Command> userHistory = history.get(user);
            if (userHistory == null){
                return null;
            } else{
                return history.get(user).stream().toList();
            }
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
    public void addToHisory(Command command, User user) {
        history.add(command, user);
    }

    /**
     * Получить историю.
     *
     * @return
     */
    public List<Command> getHistory(User user) {
        return history.toList(user);
    }

}
