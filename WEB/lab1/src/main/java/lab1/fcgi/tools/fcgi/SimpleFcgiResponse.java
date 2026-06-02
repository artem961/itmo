package lab1.fcgi.tools.fcgi;

import lab1.fcgi.tools.http.StatusCode;
import lombok.Builder;

import java.util.List;

@Builder
public class SimpleFcgiResponse implements FcgiResponse {
    private StatusCode statusCode;
    private List<String> headers;
    private String body;

    @Override
    public String buildResponse(){
        StringBuilder sb = new StringBuilder();
        sb.append("Status: ");
        sb.append(this.statusCode.getStatusCode());
        sb.append(" ");
        sb.append(this.statusCode.toString());
        sb.append("\n");

        this.headers.forEach(header -> sb.append(header + "\n"));
        sb.append("\n");

        sb.append(this.body);
        return sb.toString();
    }
}
