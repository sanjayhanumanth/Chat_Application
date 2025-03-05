package collections;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Collection26 {

    public static void main(String[] args) {
        Map<Integer,String> map=new HashMap<>();
        map.put(5,"Sanjay");
        map.put(3,"December");
        map.put(1,"Kamal");
        map.put(2,"Ram");
        map.put(4,"jaanu");
        Map<Integer,String> treeMap=new TreeMap<>(map);
        System.out.println(treeMap);
    }
}
