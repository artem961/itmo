package lab1.fcgi.tools;

import lab1.fcgi.tools.http.ServerException;
import lab1.fcgi.tools.http.StatusCode;
import org.junit.jupiter.api.Test;

public class StandartErrorTest {
    @Test
    void jsonOutputTest(){
        ServerException se =  new ServerException(StatusCode.OK, "you are stupid monkey");

        System.out.println(se.toJson());
    }
}
