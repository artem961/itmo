package lab6;

import java.net.InetAddress;

public record UserDataObject(InetAddress userAddress, int userPort, byte[] data) {
}
