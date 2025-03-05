package collections;
//29. Given a `List<String>`, write a Java program to **find the most frequently occurring word**.
import java.util.*;

public class Collection29 {

    public static void main(String[] args) {



        List<String> words = Arrays.asList("apple", "banana", "apple", "orange", "banana", "apple", "grape", "banana");
        String repeat=findFrequent(words);
        System.out.println(repeat);




    }

    private static String findFrequent(List<String> words) {


            Map<String,Integer> map=new HashMap<>();

            for(String w:words)
            {
                map.put(w,map.getOrDefault(w,0)+1);
            }
            int max=0;
            String word="";
            for(Map.Entry<String,Integer> stringIntegerEntry: map.entrySet())
            {
                if(stringIntegerEntry.getValue()>max)
                {
                    max=stringIntegerEntry.getValue();
                    word=stringIntegerEntry.getKey();
                }



            }
            return word;

    }

}
