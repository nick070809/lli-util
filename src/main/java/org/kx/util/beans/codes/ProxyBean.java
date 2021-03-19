package org.kx.util.beans.codes;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/18
 */

public interface ProxyBean {

    public Object getBean() throws Exception;

    public  Object call(String method, Object... args) throws Exception;

    public void showCodes() throws Exception ;

    public void securityCodes() throws Exception ;

    //public String getSourceCode();


}
