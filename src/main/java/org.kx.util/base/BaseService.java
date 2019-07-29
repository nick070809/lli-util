package org.kx.util.base;



import java.sql.SQLException;

/**
 * Created by sunkx on 2017/3/25.
 */
public abstract class BaseService {



    public <T> ResultRich<T> format(T t, Boolean isSuccess, String code, String msg) {
        ResultRich<T> resultRich = new ResultRich<T>(isSuccess);
        resultRich.setModel(t);
        resultRich.setCode(code);
        resultRich.setMsg(msg);
        return resultRich;
    }

    public <T> ResultRich<T> format(T t, ErrorCode err) {
        ResultRich<T> resultRich =  null;
        if(err == null ){
            resultRich = new ResultRich<T>(true);
        }
        else if( err.equals(BaseErrorCode.Success)){
            resultRich = new ResultRich<T>(true);
            resultRich.setCode(err.getRetCode());
            resultRich.setMsg(err.getRetMsg());
        }
        else{
            resultRich = new ResultRich<T>(false);
            resultRich.setCode(err.getRetCode());
            resultRich.setMsg(err.getRetMsg());
        }
        resultRich.setModel(t);
        return resultRich;
    }


    public <T> ResultRich<T> format(T t, ErrorCode err, Object ... params) {
        ResultRich<T> resultRich =  null;
        if(err == null ){
            resultRich = new ResultRich<T>(true);
        }
        else if( err.equals(BaseErrorCode.Success)){
            resultRich = new ResultRich<T>(true);
            resultRich.setCode(err.getRetCode());
            resultRich.setMsg(err.getRetMsg());
        }
        else{
            resultRich = new ResultRich<T>(false);
            resultRich.setCode(err.getRetCode());
            resultRich.setMsg(String.format(err.getRetMsg(), params));
        }
        resultRich.setModel(t);
        return resultRich;
    }

    public <T> ResultRich<T> format(Exception e) {
        ResultRich<T> resultRich  = new ResultRich<T>(false);
        if(e instanceof  BaseException){
            resultRich.setCode( ((BaseException) e).getErrCode());
            resultRich.setMsg(((BaseException) e).getErrMsg());
            return resultRich;
        }else if(e instanceof SQLException){
            resultRich.setCode( BaseErrorCode.SystemError.getRetCode());
            resultRich.setMsg( String.format(BaseErrorCode.SystemError.getRetMsg(), "数据执行异常"));
            return resultRich;
        }else {
            resultRich.setCode( BaseErrorCode.UnknownError.getRetCode());
            resultRich.setMsg(BaseErrorCode.UnknownError.getRetMsg());
            return resultRich;
        }
    }
    

}
