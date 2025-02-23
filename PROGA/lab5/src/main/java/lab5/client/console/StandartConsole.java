package lab5.client.console;

import java.io.*;

/**
 * Класс для ввода и вывода информации через консоль.
 */
public class StandartConsole implements Console {
    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    private final BufferedWriter consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));

    @Override
    public String read() {
        try {
            return consoleReader.readLine();
        } catch (IOException exc) {
            return null;
        }
    }

    @Override
    public void write(String data) throws IOException {
        try {
            consoleWriter.append(data).flush();
        } catch (IOException exc) {
            throw new IOException("Ошибка вывода в консоль");
        }
    }

    @Override
    public void close() throws Exception {
        consoleReader.close();
        consoleWriter.close();
    }
}
