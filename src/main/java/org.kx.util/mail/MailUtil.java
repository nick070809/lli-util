package org.kx.util.mail;

import com.lianlianpay.lli.common.BaseErrorCode;
import com.lianlianpay.lli.common.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

/**
 * create by sunkx on 2018/1/13
 */
public class MailUtil {
    MailSender mailSender = new MailSender();
    private Logger logger = LoggerFactory.getLogger(MailUtil.class);
    private static Properties props; // 系统属性
    @PostConstruct
    public void init(){
        props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", mailSender.getServer());
        //props.setProperty("mail.transport.protocol", protocol);
    }

    public  void sendMail(MailTemplate template , Map map, String receivers, String subject, String copyTo){
        try{
            SysMail mail = new SysMail();
            String body = MailTemplateFactory.generateHtmlFromFtl(template.getPath(), map);
            //  logger.error(body);
            mail.setMailBody(body);
            mail.setReceivers(receivers);
            mail.setSubject(subject);
            if(StringUtils.isNotBlank(copyTo))mail.setCopyto(copyTo);
            mail.setFilePaths(null);
            send(mail);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("发送邮件失败",e);
            throw new BaseException(BaseErrorCode.SystemError,"发送邮件失败"+e.getMessage());
        }

    }


    private void send(SysMail mail) throws AddressException, MessagingException, UnsupportedEncodingException {
        Session session =Session.getDefaultInstance(props, null);
        session.setDebug(true);
        MimeMessage mimeMsg =new MimeMessage(session);
        Multipart mp =new MimeMultipart();
        mimeMsg.setSubject(MimeUtility.encodeText(mail.getSubject(), "utf-8", "B"));
        BodyPart bp = new MimeBodyPart();
        bp.setContent("" + mail.getMailBody(), "text/html;charset=utf-8");
        mp.addBodyPart(bp);

        mimeMsg.setFrom(new InternetAddress(mailSender.getNickname() + " <" + mailSender.getSender() + ">"));
        mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getReceivers()));
        if(StringUtils.isNotBlank(mail.getCopyto()))
            mimeMsg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(mail.getCopyto()));
        if(StringUtils.isNotBlank(mail.getFilePaths())){
            String[] str = mail.getFilePaths().split(";");
            for(String path:str){
                BodyPart bfp = new MimeBodyPart();
                FileDataSource fileds = new FileDataSource(path);
                bfp.setDataHandler(new DataHandler(fileds));
                bfp.setFileName(fileds.getName());
                mp.addBodyPart(bfp);
            }
        }
        mimeMsg.setContent(mp);
        mimeMsg.saveChanges();

        Transport transport = session.getTransport("smtp");
        transport.connect((String) props.get("mail.smtp.host"), mailSender.getUsername(),
                mailSender.getPassword());
        transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO));
        if(StringUtils.isNotBlank(mail.getCopyto()))
            transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.CC));
        transport.close();
    }
}
