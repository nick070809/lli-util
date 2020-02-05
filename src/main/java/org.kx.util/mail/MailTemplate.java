package org.kx.util.mail;

/**
 * Created by sunkx on 2017/4/23.
 */
public enum MailTemplate {
    commonMail("commonMail.html"),
    comfirmRegister("comfirmRegister.html"),
    noticeRegister("noticeRegister.html"),
    forgotPas("forgotPas.html"),

    ;
    private String path;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    MailTemplate( String path) {
        this.path = path;
    }
}
