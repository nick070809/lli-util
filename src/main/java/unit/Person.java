package unit;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/10/29
 */

public class Person {

    {
        System.out.println("1");
    }

    public Person() {
        System.out.println(2);
    }

    public static void main(String[] args) {
        new Person();
        new Person();
    }
}
