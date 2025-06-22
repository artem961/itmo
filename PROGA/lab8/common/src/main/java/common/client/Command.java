package common.client;

import common.client.exceptions.CommandExecutionError;
import common.network.Response;
import common.network.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Абстрактный класс команды.
 */
@EqualsAndHashCode
@Getter
public abstract class Command{
    private final String name;
    private final String description;
    @Setter
    private boolean clientAvailable;

    public Command(String name, String description)  {
        this.name = name;
        this.description = description;
        this.clientAvailable = true;
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

    public CommandInfo getInfo(){
        return new CommandInfo(name, description, clientAvailable);
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
