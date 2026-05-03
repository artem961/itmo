import java.util.ArrayList;
import java.util.List;

public class Test {
    static void removeMoreTen(ArrayList<Integer> integers) {

        for (int i = 0; i < integers.size(); i++) {

            if (integers.get(i) > 10) {

                integers.remove(i);

            }

        }

        System.out.println(integers);

    }

    public static void main(String[] args) {
        ArrayList<Integer> list =  new ArrayList<>(List.of(5, 12, 15, 8, 3, 20));
        removeMoreTen(list);
    }


}
