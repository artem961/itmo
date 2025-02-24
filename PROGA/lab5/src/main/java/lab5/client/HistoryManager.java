package lab5.client;

import lab5.client.commands.Command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class HistoryManager {
    private class History {
        private final int maxSize;
        Deque<Command> hisory;

        private History(int maxSize) {
            this.maxSize = maxSize;
            hisory = new ArrayDeque<>(maxSize);
        }

        private void add(Command command) {
            if (hisory.size() == maxSize) hisory.removeFirst();
            hisory.addLast(command);
        }

        private List<Command> toList() {
            return hisory.stream().toList();
        }
    }

    private History history;

    public HistoryManager(){
        history = new History(15);
    }

    public void addToHisory(Command command){
        history.add(command);
    }

    public List<Command> getHistory(){
        return history.toList();
    }

}
