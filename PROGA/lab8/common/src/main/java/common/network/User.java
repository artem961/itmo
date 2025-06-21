package common.network;

import java.io.Serializable;

public record User(int id, String name, String hashedPassword) implements Serializable {
    public User(String name, String hashedPassword){
        this(-1, name, hashedPassword);
    }
}
