package collections;

import java.util.HashMap;
import java.util.Map;

public class Collection27 {

    public static void main(String[] args) {


        int[] numbers = {5, 3, 7, 3, 5, 7, 7, 3, 8, 5};
        Map<Integer,Integer> map=new HashMap<>();
        for(int n:numbers)
        {
            map.put(n,map.getOrDefault(n,0)+1);
        }
        for(Map.Entry<Integer,Integer> entry:map.entrySet())
        {
            System.out.println(entry.getKey()+" "+ entry.getValue());
        }
    }
}
