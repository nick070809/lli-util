package org.kx.util.beans.codes;

import org.kx.util.ClassUtil;
import org.kx.util.FileUtil;
import org.kx.util.reflect.MyObjectMaker;
import org.kx.util.rsa.RsaCommon;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/18
 */

public abstract class AbstractXBean  implements ProxyBean {

    public String privateSource;
    public String originSource;
    private Map<String, Object> objs = new HashMap<>();


    public Object getBean() throws Exception {
        if (objs.get(this.getClass().getSimpleName()) != null) {
            return objs.get(this.getClass().getSimpleName());
        }

        String bizSource = FileUtil.readFile(privateSource);
        bizSource = bizSource.trim();
        String sbizSource = RsaCommon.decryptByPublicKey(bizSource);
        Object proxyObj = new MyObjectMaker().makeObject(sbizSource);
        objs.put(this.getClass().getSimpleName(), proxyObj);
        return proxyObj;
    }

    @Override
    public Object call(String methodName, Object... args) throws Exception {
        Method method = getMethod(methodName);
        Object result = method.invoke(getBean() , args);
        return  result ;
    }


    public void securityCodes() throws IOException {
        String info = FileUtil.readFile(originSource);
        String securityStr = RsaCommon.encryptByPrivateKey(info);
        FileUtil.writeStringToFile(securityStr, privateSource);
    }
    public Method getMethod(String methodStr) throws Exception {
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


    public void showCodes() throws Exception {
        String bizSource = FileUtil.readFile(privateSource);
        bizSource = bizSource.trim();
        String originWord = RsaCommon.decryptByPublicKey(bizSource);
        System.out.println(originWord);
    }

}
