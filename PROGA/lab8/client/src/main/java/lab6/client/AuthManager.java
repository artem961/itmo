package lab6.client;

import common.network.Request;
import common.network.Response;
import common.network.Serializer;
import common.network.User;
import common.network.exceptions.NetworkException;
import lab6.network.NetworkManager;
import lombok.Getter;

import java.util.List;

public class AuthManager {
    private final NetworkManager networkManager;
    @Getter
    private User user;

    public AuthManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.user = null;
    }

    public boolean isAuth() {
        return user != null;
    }

    private Response sendRequest(Request request) {
        try {
            networkManager.sendData(Serializer.serializeObject(request));
            return (Response) Serializer.deserialazeObject(networkManager.receiveData());
        } catch (NetworkException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private void handleResponce(Response response) {
        switch (response.type()) {
            case AUTH:
                @SuppressWarnings("unchecked")
                List<User> users = (List<User>) response.collection();
                user = users.get(0);
                break;
            case EXCEPTION:
                break;
        }

    }

    public Response authUser(User user) {
        Request request = new Request("auth", null, null, user);
        Response response = sendRequest(request);
        handleResponce(response);
        return response;
    }

    public Response regUser(User user) {
        Request request = new Request("reg", null, null, user);
        Response response = sendRequest(request);
        handleResponce(response);
        return response;
    }
}
