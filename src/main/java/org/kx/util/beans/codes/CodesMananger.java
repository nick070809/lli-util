package org.kx.util.beans.codes;

import org.junit.Test;
import org.kx.util.base.str.StringUtil;

import java.util.HashMap;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/18
 */

public class CodesMananger<T> {
    public static final String BEAN_NAME_SUFFIX = "Proxy";
    public static final String BEAN_PATH = "org.kx.util.beans.codes";

    public static CodesMananger CodesMananger = new CodesMananger();

    private HashMap<String, Object> beans = new HashMap<>();

    public static CodesMananger getInstance() {
        return CodesMananger;
    }


    public void loadProxyBean(String beanName) throws Exception {
        String proxyBeanName = StringUtil.UpperFirstWord(beanName) + BEAN_NAME_SUFFIX;
        Class clazz = Class.forName(BEAN_PATH + "." + proxyBeanName);
        if (ProxyBean.class.isAssignableFrom(clazz)) {
            Object proxyObj = clazz.newInstance();
            ProxyBean proxyBean = (ProxyBean) proxyObj;
            beans.put(beanName,proxyBean);
            return;
        }
        throw new ClassNotFoundException("class name " + proxyBeanName + " is not found ");
    }


    public <T> Object getProxyBean(String beanName) throws Exception {
        Object bean =  beans.get(beanName) ;
        if (bean == null ) {
            loadProxyBean( beanName);
        }
        bean =  beans.get(beanName) ;
        return  bean;
    }


    public  Object call(String beanName ,String methodName, Object... args) throws Exception {
        ProxyBean proxyBean = (ProxyBean) CodesMananger.getInstance().getProxyBean(beanName);
        return proxyBean.call(methodName,args);
    }


    /**
     *  .. test code ..
     */

    //String beanName = "appUtil";
    String beanName = "scUtil";

    @Test
    public void securityCodes() throws Exception {
        ProxyBean proxyBean = (ProxyBean) CodesMananger.getInstance().getProxyBean(beanName);
        proxyBean.securityCodes();
    }



    @Test
    public void showCodes() throws Exception {
        ProxyBean proxyBean = (ProxyBean) CodesMananger.getInstance().getProxyBean(beanName);
        proxyBean.showCodes();
    }


    @Test
    public void call() throws Exception {
        ProxyBean proxyBean = (ProxyBean) CodesMananger.getInstance().getProxyBean(beanName);
        Object obj = proxyBean.call("test","skx");
        if(obj != null){
            System.out.printf(obj.toString());
        }
    }


}
