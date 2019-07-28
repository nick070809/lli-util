package org.kx.util.mail;

import java.io.Serializable;

/**
 * Created by sunkx on 2017/4/23.
 */
public class SysMail implements Serializable {
    private String receivers;  //收件人  以分号隔开
    private String copyto;  //抄送 以分号隔开
    private String subject;
    private String mailBody;
    private String filePaths; //文件列表
    private int state;

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getCopyto() {
        return copyto;
    }

    public void setCopyto(String copyto) {
        this.copyto = copyto;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public String getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String filePaths) {
        this.filePaths = filePaths;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
