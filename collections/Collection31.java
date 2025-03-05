package collections;

import java.util.HashSet;
import java.util.Set;

public class Collection31 {

    public static void main(String[] args) {
        int arr[]={1,1,3,5,7,9,7,2,1};
        Set<Integer> hashSet=new HashSet<>();
        for(int a:arr)
        {
            if(!hashSet.add(a))
            {
                System.out.println(a+" ");
            }
        }
    }
}
