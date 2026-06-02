package lab1.fcgi.tools.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParamsParser {
    public static Map<String, List<String>> parseQueryParams(String queryString) {
        List<String> params = List.of(queryString.split("&"));
        Map<String, List<String>> result = new HashMap<>();

        try {
            params.forEach(param -> {
                String[] split = param.split("=");
                String key = split[0];
                String value = split[1];
                if (result.containsKey(key)) {
                   result.get(key).add(value);
                } else{
                    List<String> values = new ArrayList<>();
                    values.add(value);
                    result.put(key, values);
                }
            });
            return result;
        } catch (IndexOutOfBoundsException e) {
            throw new ServerException(StatusCode.BAD_REQUEST, "Invalid query string");
        }
    }
}
