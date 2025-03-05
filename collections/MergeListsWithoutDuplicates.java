package collections;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MergeListsWithoutDuplicates {

    public static void main(String[] args) {
        List<String> list1 = Arrays.asList("Apple", "Banana", "Orange", "Mango");
        List<String> list2 = Arrays.asList("Mango", "Grapes", "Apple", "Pineapple");
        Set<String>  set=new LinkedHashSet<>();
        set.addAll(list1);
        set.addAll(list2);
        System.out.println(set);
    }
}
