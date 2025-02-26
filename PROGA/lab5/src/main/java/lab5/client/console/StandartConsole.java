package lab5.client.console;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Класс для ввода и вывода информации через консоль.
 */
public class StandartConsole implements Console {
    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    private final BufferedWriter consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));
    private Deque<String> script = new ArrayDeque<>();
    private boolean scriptMode = false;

    public void setScriptMode(boolean scriptMode) {
        this.scriptMode = scriptMode;
    }

    public void setScript(List<String> script) {
        this.script.addAll(script);
    }

    @Override
    public String read() {
        if (!scriptMode) {
            try {
                return consoleReader.readLine();
            } catch (IOException e) {
                return null;
            }
        } else{
            if (script.size() == 1) setScriptMode(false);
            String line = script.getFirst();
            script.removeFirst();
            writeln(line);
            return line;
        }
    }

    @Override
    public void write(String data){
        try {
            consoleWriter.append(data).flush();
        } catch (IOException exc) {
            throw new RuntimeException("Ошибка вывода в консоль");
        }
    }

    @Override
    public void close() throws Exception {
        consoleReader.close();
        consoleWriter.close();
    }
}
