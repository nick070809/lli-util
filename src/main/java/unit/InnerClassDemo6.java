package unit;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/10/30
 */

public class InnerClassDemo6 {

   static class Inner{

        static void show(){}

    }

    public void method(){

        Inner.show();//可以

    }

    public static void main(String[] args) {//static不允许this

        Inner.show();//错误，Inner类需要定义成static

    }
}
