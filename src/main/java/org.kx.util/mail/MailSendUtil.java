package org.kx.util.mail;

//import lombok.extern.slf4j.Slf4j;

import org.fla.nnd.s1.Cx;
import org.junit.Test;
import org.kx.util.DateUtil;
import org.kx.util.FileUtil;
import org.kx.util.image.Base64ImageUtil;

import java.util.*;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/5
 */
//@Slf4j
public class MailSendUtil {

    private  static String toName  = "n2pmAAUGxrUz/JrRaoK4MGHPqeJ8KKh6RYUiZG+u1qOd5TbO1pF5/Ol9aU+WJTGtpRlWD5deCAJY/TEcoCpdTCscDu0P5sr9KsmB4gp2EPpbVQJGcAOASYA8U7YagNpNvKyNaoz69tAiYlqIP8p1fERUtnNVuG9rzQEmr4XmLSs=";
    private  static String userName  = "TpURd5GnZIgWMyf0Gi1MROyCD28XaS1knRs71y+x5i6RwWEPmNlvS4MvQ9ZctM9TQ0iJG3soPdsaI/H51Vb2R7bRG98xTHQHRWHGvHx/jv98INjxkWVB5LPoPLH3Q0kmY1IZ+KwTyljviCLPtxpILuK7f2c6vonEyv1OxyccTRk=";
    private  static String passWord  = "OZaqbxGXGsnOfyagDlFV/+xTOxOYICoD5DeCkJPyT5oGCGk0KGeY9EYXq9lFdgPYMOJpCg2shILF/CVvclPwCz1BaB2PxJxk/gBpWO1KLR8wFROtOcbApeZQlp1+FlPHGmqD/j22KTYxg9DRxSVOnpyrq7BimAsRUgNFDEaAhOg=";


    @Test
    public void testMail() throws Exception {
        //Cx.encryptAndShow(toName);
        /*List fileList = new ArrayList();
        fileList.add("/Users/xianguang/temp/12333.png");
        fileList.add("/Users/xianguang/temp/12333.png");

        sendWithPicMail("测试",DateUtil.getDateTimeStr(new Date(),"yyyyMMdd HH:mm:ss"),fileList);*/
        //sendWithFileMail("测试",DateUtil.getDateTimeStr(new Date(),"yyyyMMdd HH:mm:ss"),fileList);




        String picPath = "" ;
        String  base = Base64ImageUtil.GetImageStr(picPath);
        String  exstr = Cx.encrypt(base);
        sendCommonMail(DateUtil.getDateTimeStr(new Date(),"yyyyMMdd"),exstr);

        Thread.sleep(10000);
    }


    @Test
    public void testsendBase64() throws Exception {
        sendBase64("/Users/xianguang/temp/绘图3.txt");
    }

    @Test
    public void testreadBase64() throws Exception {
        readBase64("/Users/xianguang/temp/绘图3.txt");
    }



    public static void sendBase64(String filePath) throws Exception {
        String suffix = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        int lastl =  filePath.lastIndexOf("/") ;
        String dirPath =  filePath.substring(0,lastl+1);
        String fileName =   filePath.substring(lastl+ 1, filePath.length());

        String content = null ;
        if(FileUtil.isPic(suffix)){
            content =  Cx.encrypt(Base64ImageUtil.GetImageStr(filePath));
        }else if(FileUtil.isTxt(suffix)){
            content =  Cx.encrypt(FileUtil.readFile(filePath));
        }
        String exstr =Cx.encrypt(fileName) +"SSSSSDDDDD"+ content;
        String targetPath = "/Users/xianguang/temp/" + DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmmss") + ".txt";
        FileUtil.writeStringToFile(exstr, targetPath);
        List fileList = new ArrayList();
        fileList.add(targetPath);

        sendWithFileMail(suffix.toUpperCase(), suffix.toUpperCase(), fileList);

    }


    public static void readBase64(String filePath) throws Exception {
        String exstr = FileUtil.readFile(filePath);
        String[] fileNameAndContent = exstr.split("SSSSSDDDDD");
        int lastl = filePath.lastIndexOf("/");
        String dirPath = filePath.substring(0, lastl + 1);


        String fileName = Cx.show(fileNameAndContent[0]);
        String content = Cx.show(fileNameAndContent[1]);
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String targetPath = dirPath + fileName;
        if (FileUtil.isPic(suffix)) {
            Base64ImageUtil.GenerateImage(content, targetPath);
        } else if (FileUtil.isTxt(suffix)) {
            FileUtil.writeStringToFile(content, targetPath);
        }

    }




    @Test
    public void testName() throws Exception {
        Cx.encryptAndShow2("898@gmail.com");
    }

    public static String getUserName() throws Exception {
        return Cx.show2(userName);
    }


    public static String getPass() throws Exception {
        return Cx.show2(passWord);
    }


    public static void sendWithPicMail(String subject,String content,List<String> pics)throws Exception {

        Date date = new Date();
        MailTemplate template = MailTemplate.commonPicMail;
        SendMail mail = new SendMail(getUserName(), getPass()); //发件人信息

        mail.setPros(getConf()); //顺序不能变
        mail.initMessage();


        mail.setRecipient(Cx.show2(toName));
        mail.setSubject(subject);
        mail.setDate(date);
        mail.setFrom("LLI-"+DateUtil.getDateTimeStr(date,"yyyyMMdd"));


        Map map  = new HashMap();
        map.put("content",content);
        String body = MailTemplateFactory.generateHtmlFromFtl(template.getPath(), map);

        mail.addContent(body, "text/html; charset=UTF-8");
        mail.addPics(pics);
        mail.sendMessage();
    }


    public static void sendWithFileMail(String subject,String content,List<String> fileList)throws Exception {

        Date date = new Date();
        MailTemplate template = MailTemplate.commonMail;
        SendMail mail = new SendMail(getUserName(), getPass()); //发件人信息

        mail.setPros(getConf()); //顺序不能变
        mail.initMessage();


        mail.setRecipient(Cx.show2(toName));
        mail.setSubject(subject);
        mail.setDate(date);
        mail.setFrom("LLI-"+DateUtil.getDateTimeStr(date,"yyyyMMdd"));


        Map map  = new HashMap();
        map.put("content",content);
        String body = MailTemplateFactory.generateHtmlFromFtl(template.getPath(), map);

        mail.addContent(body, "text/html; charset=UTF-8");
        mail.addFiles(fileList);
        mail.sendMessage();
    }




    public static void sendCommonMail(String subject,String content)throws Exception {
        Date date = new Date();
        MailTemplate template = MailTemplate.commonMail;
        SendMail mail = new SendMail(getUserName(), getPass()); //发件人信息

        mail.setPros(getConf()); //顺序不能变
        mail.initMessage();


        mail.setRecipient(Cx.show2(toName));
        mail.setSubject(subject);
        mail.setDate(date);
        mail.setFrom("LLI-"+DateUtil.getDateTimeStr(date,"yyyyMMdd"));


        Map map  = new HashMap();
        map.put("content",content);
        String body = MailTemplateFactory.generateHtmlFromFtl(template.getPath(), map);
        mail.addContent(body, "text/html; charset=UTF-8");
        mail.sendMessage();
    }


    private static Map getConf(){
        Map<String, String> conf = new HashMap<String, String>();
        conf.put("mail.smtp.host", "smtp.qq.com");
        conf.put("mail.smtp.auth", "true");
        conf.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        conf.put("mail.smtp.port", "465");
        conf.put("mail.smtp.socketFactory.port", "465");
        return conf;
    }


}
