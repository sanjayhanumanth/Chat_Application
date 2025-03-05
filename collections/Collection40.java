package collections;

import java.util.*;

public class Collection40 {

    public static void main(String[] args) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            integerList.add(i);
        }
        int c = 5;

        integerList.removeIf(b -> b > c);

        System.out.println(integerList);
    }
}
