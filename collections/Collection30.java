package collections;

import java.util.HashSet;
import java.util.Set;
//30. Write a Java program to **find the intersection of two lists (common elements)** using Sets.

public class Collection30 {

    public static void main(String[] args) {
        Set<Integer> s1 = new HashSet<>();
        s1.add(1);
        s1.add(3);
        s1.add(5);
        s1.add(7);
        Set<Integer> s2 = new HashSet<>();
        s2.add(3);
        s2.add(6);
        s2.add(7);
        s2.add(8);

        Set<Integer> s3=new HashSet<>(s1);
        s3.retainAll(s2);
        System.out.println(s3);

    }
}
