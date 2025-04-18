package common.network;

import java.io.Serializable;

public record Request(String commandName, byte[] data) implements Serializable {
}
