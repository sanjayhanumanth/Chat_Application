package collections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Collection39 {

    public static void main(String[] args) {
        List<Integer> numberList = Arrays.asList(10, 20, 30, 10, 40, 50, 20, 60, 30);
        Set<Integer> set=new HashSet<>(numberList);
        System.out.println(set);

    }
}
