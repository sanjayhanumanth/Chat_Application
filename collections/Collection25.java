package collections;
//25. Write a Java program to **find the first non-repeating character** in a string using a HashMap.

import java.util.LinkedHashMap;
import java.util.Map;

public class Collection25 {

    public static void main(String[] args) {

        String str="balloon";
        char rep=findrepeating(str);
        System.out.println(rep);
    }

    private static char findrepeating(String str) {

        Map<Character,Long> map=new LinkedHashMap<>();

        for(char ch:str.toCharArray())
        {
            map.put(ch,map.getOrDefault(ch,0L)+1);
        }
        for(Map.Entry<Character,Long> map1:map.entrySet())
        {
            if(map1.getValue()==1)
            {
                return map1.getKey();
            }
        }
        return '\0';

    }
}
