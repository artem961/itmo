package web.tools.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {
    private StatusCode statusCode;

    public ServerException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public String toJson(){
        Gson gson = new Gson();
        JsonObject jO = new JsonObject();

        jO.addProperty("statusCode", statusCode.getStatusCode());
        jO.addProperty("message", this.getMessage());

        return gson.toJson(jO);
    }
}
