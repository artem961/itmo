package common.network;

import java.io.IOException;

public class NetworkException extends Exception {
    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(IOException e) {
    }
}
