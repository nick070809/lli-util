package org.kx.util.base;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/10/20
 */

public class FileZip {


    public static void main(String[] args) throws IOException {
        //要生成的压缩文件
        String targetZipPath ="/Users/xianguang/Downloads/down/test.zip" ;


        List<String> filePaths = new ArrayList<String>(){{
            add("/Users/xianguang/Downloads/down/superLongOrdes.txt");
            add("/Users/xianguang/Downloads/down/odps.txt");
        }};
        compress(targetZipPath,filePaths);
    }

    public static void compress(String targetZipPath , List<String> filePaths) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetZipPath));
        byte[] buffer = new byte[1024];
        for (String filePath : filePaths) {
            try {
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);
                out.putNextEntry(new ZipEntry(file.getName()));

                int len;
                while ((len = fis.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
                fis.close();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        out.close();
    }




}
