package org.kx.util.mail;

/**
 * create by sunkx on 2018/1/13
 */
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SendMail {
    private String username = null;
    private String password = null;
    private Authenticator auth = null;
    private MimeMessage mimeMessage =null;
    private Properties pros = null;
    private Multipart multipart = new MimeMultipart();


    /**
     * 初始化账号密码并验证
     * 创建MimeMessage对象
     * 发送邮件必须的步骤:1
     * @param username
     * @param password
     */
    public SendMail(String username,String password){
        this.username = username;
        this.password = password;
    }
    /**
     * 初始化MimeMessage对象
     * 发送邮件必须的步骤:3
     */
    public void initMessage(){
        this.auth = new Email_Autherticator();
        Session session = Session.getDefaultInstance(pros,auth);
        session.setDebug(false); //设置获取 debug 信息
        mimeMessage = new MimeMessage(session);
    }
    /**
     * 设置email系统参数
     * 接收一个map集合key为string类型，值为String
     * 发送邮件必须的步骤:2
     * @param map
     */
    public void setPros(Map<String,String> map){
        pros = new Properties();
        for(Map.Entry<String,String> entry:map.entrySet()){
            pros.setProperty(entry.getKey(), entry.getValue());
        }
    }
    /**
     * 验证账号密码
     * 发送邮件必须的步骤
     * @author Administrator
     *
     */
    public class Email_Autherticator extends Authenticator
    {
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(username, password);
        }
    }
    /**
     * 设置发送邮件的基本参数(去除繁琐的邮件设置)
     * @param sub 设置邮件主题
     * @param text 设置邮件文本内容
     * @param rec 设置邮件接收人
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void setDefaultMessagePros(String sub,String text,String rec) throws MessagingException, UnsupportedEncodingException{
        mimeMessage.setSubject(sub);
        mimeMessage.setText(text);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(rec));
        mimeMessage.setSentDate(new Date());
        mimeMessage.setFrom(new InternetAddress(username,username));
    }
    /**
     * 设置主题
     * @param subject
     * @throws MessagingException
     */
    public void  setSubject(String subject) throws MessagingException{
        mimeMessage.setSubject(subject);
    }
    /**
     * 设置日期
     * @param date
     * @throws MessagingException
     */
    public void  setDate(Date date) throws MessagingException{
        mimeMessage.setSentDate(new Date());
    }
    /**
     * 设置邮件文本内容
     * @param text
     * @throws MessagingException
     */
    public void setText(String text) throws MessagingException{
        mimeMessage.setText(text);
    }
    /**
     * 设置邮件头部
     * @param arg0
     * @param arg1
     * @throws MessagingException
     */
    public void setHeader(String arg0,String arg1) throws MessagingException{
        mimeMessage.setHeader(arg0, arg1);
    }
    /**
     * 设置邮件接收人地址 <单人发送>
     * @param recipient
     * @throws MessagingException
     */
    public void setRecipient(String recipient) throws MessagingException{
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
    }
    /**
     * 设置邮件接收人地址 <多人发送>
     * @throws MessagingException
     * @throws AddressException
     */
    public String setRecipients(List<String> recs) throws AddressException, MessagingException{
        if(recs.isEmpty()){
            return "接收人地址为空!";
        }
        for(String str:recs){
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(str));
        }
        return "加入接收人地址成功!";
    }
    /**
     * 设置邮件接收人地址 <多人发送>
     * @throws MessagingException
     * @throws AddressException
     */
    @SuppressWarnings("static-access")
    public String setRecipients(StringBuffer sb) throws AddressException, MessagingException{
        if(sb==null||"".equals(sb)){
            return "字符串数据为空!";
        }
        Address []address = new InternetAddress().parse(sb.toString());
        mimeMessage.addRecipients(Message.RecipientType.TO, address);
        return "收件人加入成功";
    }
    /**
     * 设置邮件发送人的名字
     * @param from
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public void setFrom(String from) throws UnsupportedEncodingException, MessagingException{
        mimeMessage.setFrom(new InternetAddress(username,from));
    }





    public void addContent(String msg,String type) throws MessagingException{
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(msg, type);
        multipart.addBodyPart(messageBodyPart);
    }

    public void addFile(String filePath) throws MessagingException, UnsupportedEncodingException {
        File file = new File(filePath);
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(MimeUtility.encodeText(file.getName()));
        multipart.addBodyPart(messageBodyPart);
    }


    public void addFiles(List<String> fileList) throws MessagingException, UnsupportedEncodingException {
        for(String filePath:fileList){
            addFile(filePath);
        }
        mimeMessage.setContent(multipart);
    }


    /**
     * 先添加内容
     * 添加附件
     * 本地保存邮件信息
     * 发送
     * @throws MessagingException
     */
    public void saveMultipart() throws MessagingException {
        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();
    }

    /**
     * 发送邮件<单人发送>
     * return 是否发送成功
     * @throws MessagingException
     */
    public String sendMessage() throws MessagingException{
        saveMultipart();
        Transport.send(mimeMessage);
        return "success";
    }

}
