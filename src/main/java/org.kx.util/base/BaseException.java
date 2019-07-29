package org.kx.util.base;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errCode;
    private String errMsg;

    /**
     *
     */
    public BaseException() {
        super();
    }

    /**
     *
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     *
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     */
    public BaseException(String errCode, String errMsg) {
        super();
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BaseException(ErrorCode errorCode){
        super();
        this.errCode = errorCode.getRetCode();
        this.errMsg = errorCode.getRetMsg();
    }
    public BaseException(ErrorCode errorCode, Object ... params){
        super();
        this.errCode = errorCode.getRetCode();
        this.errMsg = String.format(errorCode.getRetMsg(), params);

    }

    /**
     *
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     */
    public String getErrCode() {
        return errCode;
    }

    /**
     */
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    /**
     */
    public String getErrMsg() {
        return errMsg;
    }

    /**
     */
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    /**
     */
    public String toString() {
        return "[errCode:" + errCode + "],[errMsg:" + errMsg + "]";
    }

}
