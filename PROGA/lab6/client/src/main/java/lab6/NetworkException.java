package lab6;

import java.io.IOException;

public class NetworkException extends Exception {
    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(IOException e) {
    }
}
