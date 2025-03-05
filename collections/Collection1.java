package collections;
//21. Write a Java program to **sort a list of students by their marks in descending order** using `Comparator`.

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Collection1 {

    public static void main(String[] args) {

        List<Students> students = Arrays.asList(
                new Students("Alice", 85.5),
                new Students("Bob", 92.0),
                new Students("Charlie", 78.0),
                new Students("David", 90.5)
        );
        students.sort(new Comparator<Students>() {
            @Override
            public int compare(Students s1, Students s2) {
                return Double.compare(s2.marks,s1.marks);
            }
        });
        for(Students students1:students)
        {
            System.out.println(students1.marks+" "+students1.name);
        }

    }
}
class Students
{
    String name;
    Double marks;

    public Students(String name, Double marks) {
        this.name = name;
        this.marks = marks;
    }
}
