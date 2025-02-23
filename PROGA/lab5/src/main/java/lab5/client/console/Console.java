package lab5.client.console;

import java.io.IOException;

/**
 * Интерфейс консоли.
 */
public interface Console extends AutoCloseable {
    /**
     * Считывает данные с консоли.
     *
     * @return Считанные данные.
     * @throws IOException
     */
    String read() throws IOException;

    /**
     * Выводит сообщение и считывает данные с консоли.
     *
     * @param message Сообщение.
     * @return Считанные данные.
     * @throws IOException
     */
    default String read(String message) throws IOException {
        write(message);
        return read();
    }

    /**
     * Написать данные в консоль.
     *
     * @param data Строка для вывода.
     */
    void write(String data) throws IOException;

    default void writeln(String data) throws IOException {
        write(data + "\n");
    }
}

