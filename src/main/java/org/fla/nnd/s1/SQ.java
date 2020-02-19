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
    static String info = "/Users/{user}/IdeaProjects/nick070809/lli-util/src/main/resources";
    static String encryptStr = "XIIczXtuhF4Q5WBp+oiMge87Z1JKH6AifTVZVAU0DEoWNN1MkmWxPsemSOSYaxLy23ghrF96ZrJ0/1UPdEo3Cw8/CNeEJMOWj88y6C27m7OPDqxzjFpONNmT9HdblSXuPBJ1k0WEFLI960+PbKIF1R8eJmk4kILfiuPFyLuJ9uQ=";
    static String MINGWEN = "/mingwen/";
    static String MIWEN = "/miwen/";

    public static void main(String[] args) throws IOException {
        getPath();
    }




    @Deprecated
    @Test
    public void showEncryptTxt() throws IOException {
        String path = getPath() + MIWEN + "test.txt.20200204155047";
        String encryptStr = FileUtil.readFile(path).trim();
        System.out.println(show(encryptStr));
    }

    @Test
    public void read2EncryptTxt() throws IOException {
        String fileName = "test.txt";
        String path = getPath() + MINGWEN + fileName;
        String mingwen = FileUtil.readFile(path).trim();
        String miwen = encrypt(mingwen);
        //fileName ="TDDL.txt";
        String newFileName = fileName + "." + DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmmss");
        FileUtil.writeStringToFile(miwen, getPath() + MIWEN + newFileName);
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




    @Deprecated
    @Test
    public void read2Txt() throws IOException {
        String fileName = "D20200213_145706.jx";
        String path =getPath() + MIWEN + fileName;
        String mingwen = FileUtil.readFile(path).trim();
        String miwen = show(mingwen);
        FileUtil.writeStringToFile(miwen, getPath() + MINGWEN + fileName);
    }


    @Deprecated
    @Test
    public void showTxt() throws IOException {
        String fileName = "D20200213_145706.jx";
        String path = getPath() + MIWEN + fileName;
        String info = FileUtil.readFile(path).trim();
        System.out.println(show(info));
    }

    public static String getPath()  {
        return Cx.show2(encryptStr);
    }
}
