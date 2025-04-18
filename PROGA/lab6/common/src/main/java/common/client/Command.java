package common.client;

import common.client.exceptions.CommandExecutionError;

import javax.management.ObjectName;
import java.util.Objects;

/**
 * Абстрактный класс команды.
 */
public abstract class Command {
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Получить имя команды.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Получить описание команды.
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Применить команду.
     * @param args аргументы.
     * @return
     * @throws CommandExecutionError
     */
    public boolean apply(String[] args) throws CommandExecutionError {
        return true;
    }

    /**
     * Применить команду, передав объект.
     * @param args
     * @param object
     * @return
     * @throws CommandExecutionError
     */
    public boolean apply(String[] args, Object object) throws CommandExecutionError{
        return apply(args);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Command command = (Command) object;
        return Objects.equals(name, command.name) && Objects.equals(description, command.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
