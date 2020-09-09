package nick.doc;

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
 * Since 2020/9/9
 */

public abstract class AbstractCode {

    public String privateSource;
    public String originSource;
    private Map<String, Object> objs = new HashMap<>();

    public Object getObject() throws Exception {
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


    public Method getMethod(String methodStr) throws Exception {
        Object proxyObj = getObject();
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


    public void rebackCodes() throws IOException {
        String bizSource = FileUtil.readFile(privateSource);
        bizSource = bizSource.trim();
        String originWord = RsaCommon.decryptByPublicKey(bizSource);
        FileUtil.writeStringToFile(originWord, originSource);
    }

    public void showCodes() throws IOException {
        String bizSource = FileUtil.readFile(privateSource);
        bizSource = bizSource.trim();
        String originWord = RsaCommon.decryptByPublicKey(bizSource);
        System.out.println(originWord);
    }


    public void securityCodes() throws IOException {
        String info = FileUtil.readFile(originSource);
        String securityStr = RsaCommon.encryptByPrivateKey(info);
        FileUtil.writeStringToFile(securityStr, privateSource);
    }


    public void showAllMethod() throws Exception {
        Object proxyObj = getObject();
        List<Method> allMethods = ClassUtil.getAllMethod(proxyObj.getClass(), null);
        for (Method method : allMethods) {
            if("wait".equals(method.getName()) || "equals".equals(method.getName()) || "toString".equals(method.getName()) || "hashCode".equals(method.getName()) ||"getClass".equals(method.getName()) || "notify".equals(method.getName()) || "notifyAll".equals(method.getName()) ){
                continue;
            }
            System.out.println(proxyObj.getClass().getSimpleName() + "." + method.getName());
        }
    }


}
