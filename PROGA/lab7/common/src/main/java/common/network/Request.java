package common.network;

import java.io.Serializable;

public record Request(String commandName, String[] args, Object object, User user) implements Serializable {
}
