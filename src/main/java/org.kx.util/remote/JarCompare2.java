package org.kx.util.remote;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.base.WriteJarVersion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Description ： beta jar冲突比较 Created by  xianguang.skx Since 2018/8/31
 */

public class JarCompare2 {

    public static void main(String[] args) throws Exception {
        String old_file = "/Users/xianguang/temp/oldIp.log"; //old
        String new_file = "/Users/xianguang/temp/newIp.log"; //new

        Map<String,String > old_ = readFile(old_file);
        Map<String,String > new_ = readFile(new_file);

        WriteJarVersion.writeDiff(old_,new_);

    }

    public static Map<String,String > readFile(String filePath) throws Exception {

        Map<String,String > maps = new HashMap<>();
        InputStream is = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("-rw")) {
                line  = line.substring("-rw-rw-r-- 1 admin admin   419592 May 28 19:23 ".length());
            }
            WriteJarVersion.parse(line,maps);
        }
        reader.close();
        is.close();
        return maps ;
    }


    public static Map<String,String > readContent(String content) throws Exception {
        Map<String,String > maps = new HashMap<>();
        String []  lines = content.split("\n");


        for (String line:lines) {
            if(StringUtils.isBlank(line)){
                continue;
            }
            if (line.startsWith("-rw")) {
                line  = line.substring("-rw-rw-r-- 1 admin admin   419592 May 28 19:23 ".length());
            }
            WriteJarVersion.parse(line,maps);
        }

        return maps ;
    }



}
