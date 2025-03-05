package collections;

import java.util.LinkedHashMap;
import java.util.Map;

public class Collection34 {

    public static void main(String[] args) {
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();

        linkedHashMap.put(3, "Banana");
        linkedHashMap.put(1, "Apple");
        linkedHashMap.put(4, "Grapes");
        linkedHashMap.put(2, "Orange");
        linkedHashMap.put(5, "Mango");

        System.out.println(linkedHashMap);
    }
}
