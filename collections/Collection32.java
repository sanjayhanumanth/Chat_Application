package collections;
//32. Given a list of employees with `id` and `name`, convert it into a **Map<id, name>** using Java 8 Streams.


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Collection32 {

    public static void main(String[] args) {

        List<Employees> employees = Arrays.asList(
                new Employees(101, "Alice"),
                new Employees(102, "Bob"),
                new Employees(103, "Charlie"),
                new Employees(104, "David")
        );
        Map<Integer,String> value=employees.stream()
                .collect(Collectors.toMap(Employees::getId,Employees::getName));

        System.out.println(value);


    }
}
class Employees
{
    Integer id;
    String name;

    public Employees(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

