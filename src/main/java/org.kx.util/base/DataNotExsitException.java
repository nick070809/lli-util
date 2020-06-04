package org.kx.util.base;

public class DataNotExsitException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errCode;
    private String errMsg;

    /**
     *
     */
    public DataNotExsitException() {
        super();
    }

    /**
     *
     */
    public DataNotExsitException(String message) {
        super(message);
    }

    /**
     *
     */
    public DataNotExsitException(Throwable cause) {
        super(cause);
    }

    /**
     */
    public DataNotExsitException(String errCode, String errMsg) {
        super();
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public DataNotExsitException(ErrorCode errorCode){
        super();
        this.errCode = errorCode.getRetCode();
        this.errMsg = errorCode.getRetMsg();
    }
    public DataNotExsitException(ErrorCode errorCode, Object ... params){
        super();
        this.errCode = errorCode.getRetCode();
        this.errMsg = String.format(errorCode.getRetMsg(), params);

    }

    /**
     *
     */
    public DataNotExsitException(String message, Throwable cause) {
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
