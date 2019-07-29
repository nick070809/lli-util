package org.kx.util.base;

/**
 * Created by sunkx on 2017/8/11.
 * 统一错误码
 */
public enum BaseErrorCode implements  ErrorCode{

    Success("000000", "交易成功/Success"),
    UnknownError("999999", "系统错误/UnknownError"),
    SystemError("999998", "系统错误{%s}"),
    WrongDataSpecified("999997", "参数校验失败{%s}"),

    ;

    private String retMsg;

    private String retCode;

    private BaseErrorCode(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
}
