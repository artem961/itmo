package lab6.network;

import common.network.Request;
import common.network.Response;

public abstract class RequestHandler {
    public abstract Response handleRequest(Request request);
}
