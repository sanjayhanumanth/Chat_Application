package collections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Collection23 {

    public static void main(String[] args) {
        HashMap<String,Integer> map=new HashMap<>();
        map.put("Apple", 50);
        map.put("Mango", 20);
        map.put("Banana", 30);
        map.put("Orange", 10);
        LinkedHashMap<String,Integer> hashMap=sortbyvalue(map);
        hashMap.forEach((key,value)-> System.out.println(key+" "+value));
    }

    private static LinkedHashMap<String, Integer> sortbyvalue(HashMap<String, Integer> map) {

        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(LinkedHashMap::new,(m,e)->m.put(e.getKey(),e.getValue()),Map::putAll);
    }
}
