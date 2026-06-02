package lab1.fcgi.tools.fcgi;

import lab1.fcgi.tools.json.JsonResponse;
import lab1.fcgi.tools.http.ServerException;
import lab1.fcgi.tools.http.StatusCode;

public class ResponseSender {
    public static void sendJsonResponse(StatusCode statusCode, String jsonBody) {
        sendResponse(new JsonResponse(statusCode, jsonBody));
    }

    public static void sendResponse(FcgiResponse response) {
        System.out.println(response.buildResponse());
    }

    public static void sendError(ServerException serverException) {
        sendJsonResponse(serverException.getStatusCode(), serverException.toJson());
    }
}
