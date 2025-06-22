package lab6.client;

import common.builders.FlatBuilder;
import common.client.Command;
import common.client.CommandManager;
import common.client.console.Console;
import common.client.exceptions.CommandExecutionError;
import common.client.exceptions.CommandNotFoundException;
import common.collection.models.Flat;
import common.network.Request;
import common.network.Response;
import common.network.Serializer;
import common.network.enums.ResponseType;
import common.network.exceptions.NetworkException;
import lab6.client.exceptions.ScriptRecursionException;
import lab6.network.NetworkManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RequestManager {
    private final NetworkManager networkManager;
    private final AuthManager authManager;

    public RequestManager(NetworkManager networkManager, AuthManager authManager) {
        this.networkManager = networkManager;
        this.authManager = authManager;
    }

    public Response parseAndMake(String input) throws NetworkException {
        String[] data = input
                .trim()
                .replace("\t", " ")
                .split("\\s+");

        String commandName = data[0];
        String[] args = Arrays.copyOfRange(data, 1, data.length);
        Request request = new Request(commandName, args, null, authManager.getUser());
        Response response = makeRequest(request);
        return response;
    }

    public Response makeRequest(Request request) throws NetworkException {
        networkManager.sendData(Serializer.serializeObject(request));
        return (Response) Serializer.deserialazeObject(networkManager.receiveData());
    }
}
