package org.kx.util;

import org.junit.Test;
import org.kx.util.beans.XProxyBean;
import org.kx.util.rsa.RsaCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/18
 */

public class LocalBeanMananger<T> {

    public static LocalBeanMananger localBeanMananger = new LocalBeanMananger();

    List<XProxyBean> xProxyBeanList = new ArrayList<>();

    public static LocalBeanMananger getInstance() {
        return localBeanMananger;
    }


    public void loadProxyBean(String beanName) throws Exception {
        for(XProxyBean xProxyBean :xProxyBeanList){
            if(xProxyBean.getBeanName().equals(beanName)){
                  return;
            }
        }
        XProxyBean xProxyBean = new XProxyBean(beanName);
        xProxyBean.loadCode();
        xProxyBean.loadBean();
        xProxyBeanList.add(xProxyBean);
    }


    public <T> Object getProxyBean(String beanName) throws Exception {
        for(XProxyBean xProxyBean :xProxyBeanList){
            if(xProxyBean.getBeanName().equals(beanName)){
                return xProxyBean;
            }
        }
        loadProxyBean( beanName);
        for(XProxyBean xProxyBean :xProxyBeanList){
            if(xProxyBean.getBeanName().equals(beanName)){
                return xProxyBean;
            }
        }
        throw new ClassNotFoundException("bean name " + beanName + " is not found ");
    }


    public  Object call(String beanName ,String methodName, Object... args) throws Exception {
        XProxyBean proxyBean = (XProxyBean) LocalBeanMananger.getInstance().getProxyBean(beanName);
        return proxyBean.call(methodName,args);
    }


    /**
     *  .. test code ..
     */

    String beanName = "appUtil";
    @Test
    public void showCodes() throws Exception {
        XProxyBean proxyBean = (XProxyBean) LocalBeanMananger.getInstance().getProxyBean(beanName);
        String sourceCode = proxyBean.getProtectedCode().trim();
        String sbizSource = RsaCommon.decryptByPublicKey(sourceCode);
        System.out.println(sbizSource);
    }


    @Test
    public void call() throws Exception {
        XProxyBean proxyBean = (XProxyBean) LocalBeanMananger.getInstance().getProxyBean(beanName);
        Object obj = proxyBean.call("test","skx");
        if(obj != null){
            System.out.printf(obj.toString());
        }
    }


}
