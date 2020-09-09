package org.kx.util;

import nick.doc.MdDoc;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/7
 */

public class JarFileReader {

    public static String read(String jarPath, String filePath) throws IOException {

       /* JarFile jarFile = new JarFile("/Users/xianguang/myDocs/lli-util.jar");
        JarEntry jarEntry = jarFile.getJarEntry("docs/public/java核心卷.md");*/

        JarFile jarFile = new JarFile(jarPath);
        JarEntry jarEntry = jarFile.getJarEntry(filePath);
        InputStream inputStream = jarFile.getInputStream(jarEntry);
        StringBuffer buffer = new StringBuffer();
        InputStreamReader ireader = new InputStreamReader(inputStream, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        inputStream.close();
        return buffer.toString();
    }


    public static List<MdDoc> list(String jarPath) throws IOException {
        List<MdDoc> names = new ArrayList<>();
        JarFile jarFile = new JarFile(jarPath);
        Enumeration enu = jarFile.entries();
        while (enu.hasMoreElements()) {
            JarEntry element = (JarEntry) enu.nextElement();
            String name = element.getName();
            if (name.startsWith("docs") || name.startsWith("codes")) {
                MdDoc mdDoc = new MdDoc();
                String[] nameDeatils = name.split(File.separator);
                if (nameDeatils.length == 3) {
                    if (nameDeatils[0].equals("codes")) {
                        mdDoc.setCode(true);
                    } else if (nameDeatils[0].equals("docs")) {
                        mdDoc.setDoc(true);
                    }

                    if (nameDeatils[1].equals("private")) {
                        mdDoc.setPrivate(true);
                    }

                    mdDoc.setPath(name);
                    names.add(mdDoc);
                }

            }

        }
        return names;
    }


}
