package lab1.fcgi.tools;

import lab1.fcgi.tools.json.CalcResultsArray;
import lab1.fcgi.tools.json.StandartCalcResult;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CalcResultsArrayTest {
    @Test
    void TestCalcResultsArray(){
        StandartCalcResult result1 = new StandartCalcResult(1d, 1d, 1d, true, "time", "time");
        StandartCalcResult result2 = new StandartCalcResult(1d, 1d, 1d, true, "time", "time");
        CalcResultsArray array = new CalcResultsArray(List.of(result1, result2));

        System.out.println(array.toJson());
    }
}
