package common.builders;

import common.client.console.Console;
import common.network.User;
import lombok.SneakyThrows;

import java.security.MessageDigest;

public class UserBuilder{
    @SneakyThrows
    public User buildUser(String name, String pass){
        MessageDigest md = MessageDigest.getInstance("SHA-384");

        byte[] bytes = md.digest(pass.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        String hash = hexString.toString();

        return new User(name, hash);
    }

}
