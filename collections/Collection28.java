package collections;
//28. Write a Java program to **remove all occurrences of a given element from an ArrayList**.

import java.util.ArrayList;
import java.util.List;

public class Collection28 {

    public static void main(String[] args) {
        List<Integer> numbers=new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(2);
        numbers.add(4);
        numbers.add(2);
        numbers.add(5);
        int ele=3;
        numbers.removeIf(n->n==ele);
        System.out.println(numbers);

    }
}
