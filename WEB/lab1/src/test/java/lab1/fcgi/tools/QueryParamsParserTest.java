package lab1.fcgi.tools;

import lab1.fcgi.tools.http.QueryParamsParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParamsParserTest {
    @Test
    void simpleQueryStringTest(){
        String queryString = "test1=value1&test2=value2&test3=value3";
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("test1", List.of("value1"));
        expected.put("test2",  List.of("value2"));
        expected.put("test3",  List.of("value3"));

        Assertions.assertEquals(expected,QueryParamsParser.parseQueryParams(queryString));
    }

    @Test
    void multiRadiusQueryStringTest(){
        String queryString = "r=rv1&r=rv2&r=rv3&test2=value2&test3=value3";
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("r", List.of("rv1", "rv2", "rv3"));
        expected.put("test2",  List.of("value2"));
        expected.put("test3",  List.of("value3"));

        Assertions.assertEquals(expected,QueryParamsParser.parseQueryParams(queryString));
    }
}
