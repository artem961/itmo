package common;

import java.io.Serializable;

public record Response(ResponseType type, byte[] data) implements Serializable {
}
