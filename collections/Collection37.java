package collections;

import java.util.HashMap;
import java.util.Map;

public class Collection37 {

    public static void main(String[] args) {
        Map<Integer,String> map=new HashMap<>();
        map.put(1,"sanjay");
        map.put(2,"kamal");
        map.put(3,"str");
        System.out.println(map.containsKey(1));
        System.out.println(map.containsValue("kamal"));
        System.out.println(map.containsKey(4));
    }
}
