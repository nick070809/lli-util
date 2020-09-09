package org.kx.util.io;

import java.io.*;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/8
 */

public class IoUtil {

    public static void Input2File(InputStream initialStream,String targetPath) throws IOException {

       // InputStream initialStream = new FileInputStream(new File("src/main/resources/sample.txt"));
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        File targetFile = new File(targetPath);
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);

    }


    public static void DataInput2File(InputStream dataInputStream,String targetPath) throws IOException {

        DataOutputStream out = new DataOutputStream(new  FileOutputStream(targetPath));
        BufferedInputStream  bufferedInput = new BufferedInputStream(dataInputStream);
        BufferedOutputStream  bufferedOutput = new BufferedOutputStream(out);

        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        //从文件中按字节读取内容，到文件尾部时read方法将返回-1
        while ((bytesRead = bufferedInput.read(buffer)) != -1) {
            bufferedOutput.write( buffer );
        }


        bufferedOutput.close();
        out.close();
        bufferedInput.close();
        dataInputStream.close();
    }

}
