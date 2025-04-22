package common.client.console;

import java.io.IOException;

/**
 * Интерфейс консоли.
 */
public interface Console extends AutoCloseable  {
    /**
     * Считывает данные с консоли.
     *
     * @return Считанные данные.
     * @throws IOException
     */
    String read();

    /**
     * Выводит сообщение и считывает данные с консоли.
     *
     * @param message Сообщение.
     * @return Считанные данные.
     * @throws IOException
     */
    default String read(String message) {
        write(message);
        return read();
    }

    /**
     * Написать данные в консоль.
     *
     * @param data Строка для вывода.
     */
    void write(String data);

    /**
     * Написать данные в консоль и перейти на следующую строку.
     *
     * @param data
     */
    default void writeln(String data) {
        write(data + "\n");
    }
}

