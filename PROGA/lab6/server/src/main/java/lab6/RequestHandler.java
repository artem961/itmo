package lab6;

import common.network.Request;
import common.network.Response;

public abstract class RequestHandler {
    public abstract Response handleRequest(Request request);
}
