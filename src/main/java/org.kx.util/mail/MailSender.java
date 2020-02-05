package org.kx.util.mail;

/**
 * Created by sunkx on 2017/4/23.
 */
public class MailSender {
    // 邮件发送SMTP主机
    private  String server ;//"smtp.sina.com";
    // 发件人邮箱地址
    private  String sender ;
    // 发件人邮箱用户名
    private  String username ;
    // 发件人邮箱密码
    private  String password ;
    // 发件人显示昵称
    private  String nickname ;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
