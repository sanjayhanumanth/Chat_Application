package collections;

import java.util.Collections;
import java.util.LinkedList;

public class Collection36 {

    public static void main(String[] args) {
        LinkedList<Integer> linkedList=new LinkedList<>();
        linkedList.add(1);
        linkedList.add(30);
        linkedList.add(7);
        linkedList.add(9);
        linkedList.add(12);
        Collections.reverse(linkedList);
        System.out.println(linkedList);
    }
}
