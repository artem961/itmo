package common.client;

import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import common.network.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Абстрактный класс команды.
 */
@EqualsAndHashCode
@Getter
public abstract class Command{
    private final String name;
    private final String description;

    public Command(String name, String description)  {
        this.name = name;
        this.description = description;
    }

    /**
     * Применить команду, передав объект.
     * @param args
     * @param object
     * @return
     * @throws CommandExecutionError
     */
    public Response apply(String[] args, Object object, User user) throws CommandExecutionError{
        return null;
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
