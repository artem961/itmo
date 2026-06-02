    package lab1.fcgi.tools.json;

    import lab1.fcgi.tools.fcgi.FcgiResponse;
    import lab1.fcgi.tools.fcgi.SimpleFcgiResponse;
    import lab1.fcgi.tools.http.StatusCode;

    import java.nio.charset.StandardCharsets;
    import java.util.List;

    public class JsonResponse implements FcgiResponse {
        private String jsonBody;
        private StatusCode statusCode;

        public JsonResponse(StatusCode statusCode, String jsonBody) {
            this.jsonBody = jsonBody;
            this.statusCode = statusCode;
        }

        @Override
        public String buildResponse() {
            FcgiResponse fcgiResponse = SimpleFcgiResponse
                    .builder()
                    .statusCode(statusCode)
                    .headers(List.of("Content-Type: application/json",
                            "Content-Length: " + jsonBody.getBytes(StandardCharsets.UTF_8).length))
                    .body(jsonBody)
                    .build();
            return fcgiResponse.buildResponse();
        }
    }
