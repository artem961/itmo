package lab4.backend.utils.mapping;

import lab4.backend.dto.UserDTO;
import lombok.SneakyThrows;

import java.security.MessageDigest;

public class PasswordHasher {
    public static String hashPassword(String password) {
        return sha256(password);
    }

    public static UserDTO hashPassword(UserDTO user) {
        user.setPassword(hashPassword(user.getPassword()));
        return user;
    }

    @SneakyThrows
    private static String sha256(String input) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
