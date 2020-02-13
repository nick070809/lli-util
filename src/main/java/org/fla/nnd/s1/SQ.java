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
    static String encryptStr = "MPT58bxQPP52bK3c8gTqH/pjEHerGBAAVZcvGaNq0TL9jfNoIlPclRNEo5cKfE1zjr1I6bvYrV7zNib176dzcnxIaOm4KptE9YJq9+h7fgsZF1nGt2O2g77JjRATG7yf28cKZ00B4ZiYcgcJOTY1qmYtZs2mVuPEx7sm82Swsoc=";
    static String MINGWEN = "/mingwen/";
    static String MIWEN = "/miwen/";

    public static void main(String[] args) throws IOException {

    }

    @Test
    public void showEncryptTxt() throws IOException {
        String path = show(encryptStr) + MIWEN + "test.txt.20200204155047";
        String encryptStr = FileUtil.readFile(path).trim();
        System.out.println(show(encryptStr));
    }

    @Test
    public void read2EncryptTxt() throws IOException {
        String fileName = "test.txt";
        String path = show(encryptStr) + MINGWEN + fileName;
        String mingwen = FileUtil.readFile(path).trim();
        String miwen = encrypt(mingwen);
        //fileName ="TDDL.txt";
        String newFileName = fileName + "." + DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmmss");
        FileUtil.writeStringToFile(miwen, show(encryptStr) + MIWEN + newFileName);
    }

    @Test
    public void readPath2EncryptTxt() throws IOException {
        String path = "/Users/xianguang/IdeaProjects/0910/test_case/sp_test/sp-server/src/test/java/com/taobao/settle/sp/test/DapTest.java";

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
        FileUtil.writeStringToFile(miwen, show(encryptStr) + MIWEN + fileName);
    }


    @Test
    public void readPath2EncryptMail() throws Exception {
        String path = "/Users/**.txt";

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
        String path = "/Users/xianguang/temp/sum-business/总.txt";

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
        FileUtil.writeStringToFile(miwen, show(encryptStr) + MIWEN + fileName);
    }




    @Test
    public void read2Txt() throws IOException {
        String fileName = "总20200204_144910.txt";
        String path = show(encryptStr) + MIWEN + fileName;
        String mingwen = FileUtil.readFile(path).trim();
        String miwen = show(mingwen);
        FileUtil.writeStringToFile(miwen, show(encryptStr) + MINGWEN + fileName);
    }



    @Test
    public void showTxt() throws IOException {
        String path = show(encryptStr) + MINGWEN + "test.txt";
        String info = FileUtil.readFile(path);
        System.out.println(info);
    }

}
