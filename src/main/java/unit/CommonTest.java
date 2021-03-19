package unit;

import org.junit.Test;
import org.kx.util.FileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/9
 */

public class CommonTest {

    @Test
    public  void readOriginFile() throws IOException {
        System.out.println(63&123);
    }


    @Test
    public  void readToBufferFilterBlank() throws IOException {
        StringBuffer sb = new StringBuffer();
        FileUtil.readToBufferFilterBlank(sb, "/Users/xianguang/temp/tempx.txt");
        System.out.printf(sb.toString());
    }




    public static void main(String[] args) {
        String s = "香港罚恶香港罚恶香港罚恶香港罚恶";
        int index = s.charAt(8);
        System.out.println(index);
    }



    @Test
    public void map_() {
       Map x = new HashMap();
    }


    @Test
    public void pad_() {
        System.out.println("\n".length());    }
}
