package lab6.client;

import common.builders.UserBuilder;
import common.client.console.Console;
import common.network.Request;
import common.network.Response;
import common.network.Serializer;
import common.network.User;
import common.network.exceptions.NetworkException;
import lab6.network.NetworkManager;
import lombok.Getter;

import java.util.List;

public class AuthManager {
    private final Console console;
    private final NetworkManager networkManager;
    @Getter
    private User user;

    public AuthManager(Console console, NetworkManager networkManager) {
        this.console = console;
        this.networkManager = networkManager;
        this.user = null;
    }

    public boolean isAuth() {
        return user != null;
    }

    public void auth() {
        String input;
        while (!isAuth() && (input = console.read("\nДля входа auth\nРегистрация reg\n> ")) != null) {
            handleInput(input);
        }
    }

    public void handleInput(String input) {
        String[] data = input
                .trim()
                .replace("\t", " ")
                .split("\\s+");

        String commandName = data[0];
        switch (commandName) {
            case "auth":
                handleResponce(authUser());
                break;
            case "reg":
                handleResponce(regUser());
                break;
        }
    }

    private void handleResponce(Response response) {
        switch (response.type()) {
            case AUTH:
                @SuppressWarnings("unchecked")
                List<User> users = (List<User>) response.collection();
                user = users.get(0);
                console.writeln(response.message());
                break;
            case EXCEPTION:
                console.writeln(response.message());
                break;
        }

    }

    private Response authUser() {
        try {
            console.writeln("");
            User user = new UserBuilder(console).inputUser();
            Request request = new Request("auth", null, null, user);
            networkManager.sendData(Serializer.serializeObject(request));
            return (Response) Serializer.deserialazeObject(networkManager.receiveData());
        } catch (NetworkException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Response regUser() {
        try {
            console.writeln("");
            User user = new UserBuilder(console).inputUser();
            Request request = new Request("reg", null, null, user);
            networkManager.sendData(Serializer.serializeObject(request));
            return (Response) Serializer.deserialazeObject(networkManager.receiveData());
        } catch (NetworkException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
