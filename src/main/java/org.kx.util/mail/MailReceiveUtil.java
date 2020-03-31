package org.kx.util.mail;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/5
 */
@Slf4j
public class MailReceiveUtil {

    private static String host="pop3.163.com";


    public static void main(String[] args) throws Exception {
        Properties props=new Properties();
        //设置邮件接收协议为pop3
        props.setProperty("mail.store.protocol", "pop3");
        props.setProperty("mail.pop3.host", host);

        Session session = Session.getInstance(props);
        Store store = session.getStore("pop3");
        //连接要获取数据的邮箱 主机+用户名+密码
        store.connect(host, MailSendUtil.getUserName(), MailSendUtil.getPass());
        Folder folder = store.getFolder("inbox");
        //设置邮件可读可写
        folder.open(Folder.READ_WRITE);

        Message[] messages = folder.getMessages();

        for (int i = 0; i < messages.length; i++) {
            //解析发件人地址
            String address = messages[i].getFrom()[0].toString();
            //解析邮件主题
            String subject = messages[i].getSubject();
            //如果只是纯文本文件情况
            //String content = (String) messages[i].getContent();
            //MIME中包含文本情况
            //getTextMultipart(messages[i]);
            //MIME中包含图片情况
            //getPicMultipart(messages[i]);
            //MIME中包含附件情况
            //getAttachmentMultipart(messages[i]);
            //解析综合数据情况
            getAllMultipart(messages[i]);
        }
        folder.close(true);
        store.close();

    }
    /**
     * 解析综合数据
     * @param part
     * @throws Exception
     */
    private static void getAllMultipart(Part part) throws Exception{
        String contentType = part.getContentType();
        int index = contentType.indexOf("name");
        boolean conName = false;
        if(index!=-1){
            conName=true;
        }
        //判断part类型
        if(part.isMimeType("text/plain") && ! conName) {
            System.out.println((String) part.getContent());
        }else if (part.isMimeType("text/html") && ! conName) {
            System.out.println((String) part.getContent());
        }else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                //递归获取数据
                getAllMultipart(multipart.getBodyPart(i));
                //附件可能是截图或上传的(图片或其他数据)
                if (multipart.getBodyPart(i).getDisposition() != null) {
                    //附件为截图
                    if (multipart.getBodyPart(i).isMimeType("image/*")) {
                        InputStream is = multipart.getBodyPart(i)
                                .getInputStream();
                        String name = multipart.getBodyPart(i).getFileName();
                        String fileName;
                        //截图图片
                        if(name.startsWith("=?")){
                            fileName = name.substring(name.lastIndexOf(".") - 1,name.lastIndexOf("?="));
                        }else{
                            //上传图片
                            fileName = name;
                        }

                        FileOutputStream fos = new FileOutputStream("D:\\"
                                + fileName);
                        int len = 0;
                        byte[] bys = new byte[1024];
                        while ((len = is.read(bys)) != -1) {
                            fos.write(bys,0,len);
                        }
                        fos.close();
                    } else {
                        //其他附件
                        InputStream is = multipart.getBodyPart(i)
                                .getInputStream();
                        String name = multipart.getBodyPart(i).getFileName();
                        FileOutputStream fos = new FileOutputStream("D:\\"
                                + name);
                        int len = 0;
                        byte[] bys = new byte[1024];
                        while ((len = is.read(bys)) != -1) {
                            fos.write(bys,0,len);
                        }
                        fos.close();
                    }
                }
            }
        }else if (part.isMimeType("message/rfc822")) {
            getAllMultipart((Part) part.getContent());
        }
    }

    /**
     * 解析附件内容
     * @param part
     * @throws Exception
     */
    private static void getAttachmentMultipart(Part part) throws Exception{
        if(part.isMimeType("multipart/*")){
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if(bodyPart.getDisposition()!=null){
                    InputStream is = bodyPart.getInputStream();
                    FileOutputStream fos=new FileOutputStream("路径+文件名");
                    int len=0;
                    byte[] bys=new byte[1024];
                    while((len=is.read(bys))!=-1){
                        fos.write(bys, 0, len);
                    }
                    fos.close();
                }
            }
        }

    }
    /**
     * 解析图片内容
     * @param part
     * @throws Exception
     */
    private static void getPicMultipart(Part part) throws Exception{
        if(part.isMimeType("multipart/*")){
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if(bodyPart.isMimeType("image/*")){
                    InputStream is = bodyPart.getInputStream();
                    FileOutputStream fos=new FileOutputStream("路径+文件名");
                    int len=0;
                    byte[] bys=new byte[1024];
                    while((len=is.read(bys))!=-1){
                        fos.write(bys, 0, len);
                    }
                    fos.close();
                }
            }
        }
    }
    /**
     * 解析文本内容
     * @param part
     * @throws Exception
     */
    private static void getTextMultipart(Part part) throws Exception{
        if(part.isMimeType("text/html")){
            String content = (String) part.getContent();
            System.out.println(content);
        }else if(part.isMimeType("text/plain")){
            String content = (String) part.getContent();
            System.out.println(content);
        }
    }



}
