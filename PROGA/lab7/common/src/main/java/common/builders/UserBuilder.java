package common.builders;

import common.client.console.Console;
import common.network.User;
import lombok.SneakyThrows;

import java.security.MessageDigest;

public class UserBuilder extends DefaultConsoleBuilder{
    public UserBuilder(Console console) {
        super(console);
    }

    @SneakyThrows
    public User inputUser(){
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        String name = inputString("Введите имя: ");
        String pass = inputString("Введите пароль: ");

        byte[] bytes = md.digest(pass.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        String hash = hexString.toString();

        return new User(name, hash);
    }

}
