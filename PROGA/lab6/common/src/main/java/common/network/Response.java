package common.network;

import java.io.Serializable;

public record Response(ResponseType type, byte[] data) implements Serializable {
}
