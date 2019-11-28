package org.kx.util.base;

import org.kx.util.FileUtil;

import java.io.*;

/**
 * Description ： Created by  xianguang.skx Since 2019/8/5
 * 检查文本字符
 */

public class CheckGarbled {

    public static  String checkAndReplace(String source){
        String s = new String(source);
        if(s!=null){
            if(!(java.nio.charset.Charset.forName("GBK").newEncoder().canEncode(s))){
                try {
                    s = new String(source.getBytes("ISO-8859-1"),"UTF-8");
                    System.out.println(s);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return  s;
    }

    public static void main(String[] args) throws IOException {
        String path = "/Users/xianguang/IdeaProjects/*/src/main/java/com/*/impl/*.java" ;
        InputStream is = new FileInputStream(path);
        String line; // 用来保存每行读取的内容
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            String newLine = checkAndReplace(line);
            if(!line.equals(newLine)){
                buffer.append(newLine);
            }else {
                buffer.append(line);

            }
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        System.out.println(buffer.toString());
        FileUtil.writeStringToFile(buffer.toString(),path);
    }
}
