package org.kx.util;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/7/7
 */

public class ODPS {

    //中文空格

    @Test
    public void readOdpsFile() throws Exception {
        String filePath = "/Users/xianguang/Downloads/down/odps.txt";
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        StringBuilder resultContent = new StringBuilder();
        while (line != null) { // 如果 line 为空说明读完了
            String  thisline = line.replace("\u001B[K","");
            resultContent.append(thisline).append("\n");
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        FileUtil.writeStringToFile(resultContent.toString(),"/Users/xianguang/Downloads/down/od.txt");
    }

}
