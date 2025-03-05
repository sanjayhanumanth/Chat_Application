package collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//LinkedList into ArrayList
public class Collection33 {

    public static void main(String[] args) {

        LinkedList<Integer> list=new LinkedList<>();
        list.add(50);
        list.add(30);
        list.add(25);
        list.add(90);
        list.add(80);
        List<Integer> integers=new ArrayList<>(list);
        System.out.println(integers);
    }
}
