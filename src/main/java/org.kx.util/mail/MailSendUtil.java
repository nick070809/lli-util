package org.kx.util.mail;

import lombok.extern.slf4j.Slf4j;
import org.fla.nnd.s1.Cx;
import org.junit.Test;
import org.kx.util.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/5
 */
@Slf4j
public class MailSendUtil {
     private  static String toName  = "OP4QP8FnUG3cFbsncYSxiZTPq7ioe1YJJNkCeBwwgYfglrgcSRldxWToOpnIketvN8YvX89oN4k7U03LMFId02facDzcBJypjo3dUH70JHHXvrkaJ2uYjrT6nuV48N8f9WT80OYZIWn78Rz87HQBgQsuer+9NnuhU/Frl6OT5z8=";
     private  static String userName  = "cGpk4oywZGoqWlFdEPfU3NRkkXZ39ns9fhhe2tkg58qCHJfYVNCHLuqHDq0kpV/nIma/1TE3UQzQKVd9/B2yuOH64bQpJ7EgswV4BarXxeMcXkcAVo2RezKj8sA4n2EyB2fdnXTmquQQje+Zov3hxmC58sfEqI3MmUoRW2q4LP0=";
     private  static String passWord  = "GbLgsjlEGrDj0qGxzvvydOwmLNWr1t1bH3jPEAAXPc54ZhptsGqA9kcQUHMLr8zssv3hI7uitjNoYv1lK8Aq2IXA9lNeg239WCo77v+Z6p3JFLzzzUh0EhCet4zt3Q76UHWBc3+rjogQsMG7n0XYUVhD57DJ7Ixba6uKbW9XPtY=";


    @Test
    public void testMail() throws Exception {
        //Cx.encryptAndShow(toName);
        sendCommonMail("测试",DateUtil.getDateTimeStr(new Date(),"yyyyMMdd HH:mm:ss"));
        Thread.sleep(10000);
    }

    public static String getUserName() throws Exception {
        return Cx.show(userName);
    }


    public static String getPass() throws Exception {
        return Cx.show(passWord);
    }



    public static void sendCommonMail(String subject,String content)throws Exception {
        Date date = new Date();
        MailTemplate template = MailTemplate.commonMail;
        SendMail mail = new SendMail(getUserName(), getPass()); //发件人信息

        mail.setPros(getConf()); //顺序不能变
        mail.initMessage();


        mail.setRecipient(Cx.show(toName));
        mail.setSubject(subject);
        mail.setDate(date);
        mail.setFrom("LLI-"+DateUtil.getDateTimeStr(date,"yyyyMMdd"));


        Map map  = new HashMap();
        map.put("content",content);
        String body = MailTemplateFactory.generateHtmlFromFtl(template.getPath(), map);
        mail.setContent(body, "text/html; charset=UTF-8");
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
