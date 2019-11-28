package org.kx.util;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sunkx on 2017/5/17.
 */
public class FileUtil {
    public  static void createDir(String dirPath) throws IOException{
        File daofile = new File(dirPath);
        if(!daofile.exists())daofile.mkdirs();
    }
    public  static File createFile(String filePath) throws IOException{
        filePath = filePath.replace("\\", "/");
        String dirPath = filePath.substring(0,filePath.lastIndexOf("/"));
        createDir(dirPath);
        File file = new File(filePath);
        if(!file.exists())file.createNewFile(); //创建文件
        return file;
    }

    public static boolean createFile(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            createDir(file.getParentFile());
        }
        return file.createNewFile();
    }


    public static void createDir(File dir) {
        if (!dir.getParentFile().exists()) {
            createDir(dir.getParentFile());
        }
        dir.mkdir();
    }


    /**
     * 将文本文件中的内容读入到buffer中
     */
    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }

    /**
     * 读取文本文件内容
     */
    public static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        FileUtil.readToBuffer(sb, filePath);
        return sb.toString();
    }

    /**
     * 获取目录下所有的文件
     * @param filePath
     * @return
     */
    public static List<File> listFiles(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            return Arrays.asList(files);
        }
        return null;
    }

    /**
     * 字符串写入文件
     * 使用缓冲区
     */
    public  static void writeStringToFile(String content,String filePath) throws IOException{
        File file = new File(filePath);
        //创建文件
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //FileWriter fwriter = new FileWriter(filePath);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
        BufferedWriter writer = new BufferedWriter(out);
        writer.write(content);
        writer.close();
    }
    /**
     * 将字符串追加到文件最后
     */
    public  static void appedStringToFile(String content,String filePath) throws IOException{
        FileOutputStream fos = new FileOutputStream(filePath,true);//true表示在文件末尾追加
        fos.write(content.getBytes());
        fos.close();//流要及时关闭
    }


    public static byte[] readFileInBytes(String filePath) {
        final int readArraySizePerRead = 4096;
        File file = new File(filePath);
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        try {
            if (file.exists()) {
                DataInputStream isr = new DataInputStream(new FileInputStream(
                        file));
                byte[] tempchars = new byte[readArraySizePerRead];
                int charsReadCount = 0;

                while ((charsReadCount = isr.read(tempchars)) != -1) {
                    for(int i = 0 ; i < charsReadCount ; i++){
                        bytes.add (tempchars[i]);
                    }
                }
                isr.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return toPrimitives(bytes.toArray(new Byte[0]));
    }






    static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {


        ByteArrayInputStream byteInt=new ByteArrayInputStream(readFileInBytes("/Users/xianguang/Downloads/xinxuan_offline/0BABB79A022915327B799D4CDDD136E5"));
        ObjectInputStream objInt=new ObjectInputStream(byteInt);
        System.out.println(JSON.toJSONString(objInt.readObject()));



    }

}


