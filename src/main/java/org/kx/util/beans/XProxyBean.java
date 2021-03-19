package org.kx.util.beans;

import nick.doc.MdCtrl;
import nick.doc.MdDoc;
import org.kx.util.ClassUtil;
import org.kx.util.base.str.StringUtil;
import org.kx.util.reflect.MyObjectMaker;
import org.kx.util.rsa.RsaCommon;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/22
 */

public class XProxyBean {
    private String beanName;
    private String simpleClassName;
    private String protectedCode;
    private Object bean;

    public XProxyBean(String beanName) {
        this.beanName = beanName;
        this.simpleClassName = StringUtil.UpperFirstWord(beanName);
    }


    public String loadCode() throws ClassNotFoundException {
        List<MdDoc> mdDocList = MdCtrl.getMdCtrl().getDocs();
        for (MdDoc mdDoc : mdDocList) {
            if (mdDoc.isCode()) {
                String name = mdDoc.getName().substring(5);
                if (this.simpleClassName.equals(name)) {
                    this.protectedCode = mdDoc.getOrigContent();
                    return this.protectedCode;
                }
            }
        }
        throw new ClassNotFoundException("class name " + this.simpleClassName + " is not found ");
    }


    public Object loadBean() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        String sourceCode = this.protectedCode.trim();
        String sbizSource = RsaCommon.decryptByPublicKey(sourceCode);
        Object proxyObj = new MyObjectMaker().makeObject(sbizSource);
        this.bean = proxyObj;
        return this.bean;
    }

    public String getBeanName() {
        return beanName;
    }


    public String getSimpleClassName() {
        return simpleClassName;
    }


    public String getProtectedCode() throws ClassNotFoundException {
        if (this.protectedCode == null) {
            return loadCode();
        }
        return this.protectedCode;
    }

    public Object getBean() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (this.bean == null) {
            return loadBean();
        }
        return this.bean;
    }

    public  Object call(String methodName, Object... args) throws Exception{
        Method method = getMethod(methodName);
        Object result = method.invoke(getBean() , args);
        return  result ;
    }


    private Method getMethod(String methodStr) throws Exception {
        Object proxyObj = getBean();
        List<Method> allMethods = ClassUtil.getAllMethod(proxyObj.getClass(), null);
        Method proxyMethod = null;
        for (Method method : allMethods) {
            if (method.getName().equals(methodStr)) {
                proxyMethod = method;
                break;
            }
        }
        if (proxyMethod == null) {
            throw new NoSuchMethodException(proxyObj.getClass().getSimpleName() + "." + methodStr);
        }
        return proxyMethod;
    }

}
