package org.fla.nnd.s1;

import org.junit.Test;
import org.kx.util.DateUtil;
import org.kx.util.FileUtil;
import org.kx.util.mail.MailSendUtil;

import java.io.IOException;
import java.util.Date;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/4
 */

public class SQ extends Cx {
    //this is a miwen txt
    static String encryptStr = "SF5VX8hJLn/T1LsdcM9MOI7u/nsyxJkuUq7qAS1izGWvSu+CdJIOxOpK5Fxtyt/TICY6FbE3LGNcgepuaqgKIcg7vi85kP/ZKVK34NCuZy9fT9lVz3JYiX5crumamiqQKZjtqs9L75WTsZH+zdkVq9zRJYG9mvNX5zAGy/cOLjA=";
    static String MINGWEN = "/mingwen/";
    static String MIWEN = "/miwen/";

    public static void main(String[] args) throws IOException {
        System.out.println(getPath());
    }


    public static String getPath()  {
        return   Cx.show2(encryptStr);

    }



    @Test
    public void readPath2EncryptTxt() throws IOException {
        String path = "/*.java";

        String temp[]=path.split("/");
        String fileName=temp[temp.length-1];
        if(fileName.contains(".")){
            String[] sd=fileName.split("\\.");
            if(sd.length ==2){
                String sufix = sd[1];
                if(sufix.equals("java")){
                    sufix = "jx";
                }
                fileName =sd[0].substring(0,1) +DateUtil.getDateTimeStr(new Date(), "yyyyMMdd_HHmmss")+"."+sufix;
            }
        }
        String mingwen = FileUtil.readFile(path).trim();
        String miwen = encrypt(mingwen);
        FileUtil.writeStringToFile(miwen, getPath() + MIWEN + fileName);
    }


    @Test
    public void readPath2EncryptMail() throws Exception {
        String path = "";

        String temp[]=path.split("/");
        String fileName=temp[temp.length-1];
        if(fileName.contains(".")){
            String[] sd=fileName.split("\\.");
            if(sd.length ==2){
                fileName =sd[0].substring(0,1) +DateUtil.getDateTimeStr(new Date(), "yyyyMMdd_HHmmss")+"."+sd[1];
            }
        }
        String mingwen = FileUtil.readFile(path).trim();
        String miwen = encrypt(mingwen);
        MailSendUtil.sendCommonMail(fileName,miwen);
    }
    @Test
    public void readHtml2EncryptTxt() throws IOException {
        String path = "/*.txt";

        String temp[]=path.split("/");
        String fileName=temp[temp.length-1];
        if(fileName.contains(".")){
            String[] sd=fileName.split("\\.");
            if(sd.length ==2){
                fileName =sd[0].substring(0,1) +DateUtil.getDateTimeStr(new Date(), "yyyyMMdd_HHmmss")+"."+sd[1];
            }
        }
        String mingwen = FileUtil.readFile(path).trim();
        String miwen = encrypt(mingwen);
        FileUtil.writeStringToFile(miwen, getPath() + MIWEN + fileName);
    }



}
