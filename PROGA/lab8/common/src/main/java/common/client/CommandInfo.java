package common.client;

import java.io.Serializable;

public record CommandInfo(String name, String description, boolean clientAvailable) implements Serializable {
}
