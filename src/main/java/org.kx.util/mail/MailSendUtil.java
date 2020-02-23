package org.kx.util.mail;

import lombok.extern.slf4j.Slf4j;
import org.fla.nnd.s1.Cx;
import org.junit.Test;
import org.kx.util.DateUtil;

import java.util.*;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/5
 */
@Slf4j
public class MailSendUtil {

    private  static String toName  = "AgejvKAfSUjxT7UrHQGDA5/X3UzQ/ShMCzETN2eG8GvP+tgafRK8GcoKaO/aSylTUyW1dz8fWOFrJaFzDr6hM6gjRtmcYsjuzd0WaLnMyFzr4rB+0bGmVfviJb3miDJkiPx5KzRW/bEMrD2JkZU1dDqCYVPc7+VlKRnJi9vZA1g=";
    private  static String userName  = "d5IdcGtQDklCyhDSNIH8bIWuDXsToODQFf8YAzviQcfEuOKStcDKp9pnDtVrEdsVBEcGW6YveIZsIWGV/vPpEGum+NbBCKnfiZmW2i1PQgQiYkr4T38YmY8KtlVrMnBd4BqnbTfguYtILTNepZMFWNihsRtjsDXHlp3ywIDteNk=";
    private  static String passWord  = "NJl7GqWdPfYYyYv+5PwtiaoJ0oGQEbUjvfsYVKP48AqUoJiNoB/8TO7MOdJlytGXfLX0SXb63PubGF8TD6KclR7yxA7ZdalVQ+naL3GrBKruMkQ4inUfYYM8+RSOzAarj1MoFTxz5gjUJWTtX4HgbqWK+zI+Yl7iVUamMcCW9u0=";

    @Test
    public void testMail() throws Exception {
        //Cx.encryptAndShow(toName);
        List<String> fileList = new ArrayList<>();
        sendWithFileMail("测试",DateUtil.getDateTimeStr(new Date(),"yyyyMMdd HH:mm:ss"),fileList);
       // sendCommonMail("测试",DateUtil.getDateTimeStr(new Date(),"yyyyMMdd HH:mm:ss"));
        Thread.sleep(10000);
    }

    public static String getUserName() throws Exception {
        return Cx.show2(userName);
    }


    public static String getPass() throws Exception {
        return Cx.show2(passWord);
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
