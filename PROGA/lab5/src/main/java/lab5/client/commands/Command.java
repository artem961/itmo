package lab5.client.commands;

import lab5.client.exceptions.CommandExecutionError;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
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
