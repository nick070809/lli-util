package org.kx.util;

import org.junit.Test;
import org.kx.util.reflect.MyObjectMaker;
import org.kx.util.rsa.RsaCommon;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/8/11
 */

public class DingDingUtils {

    private static String privateSource = "/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/codes/private/DingDingAlarmUtils.txt";
    private static String originSource = "/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/java/DingDingAlarmUtils.java";

    @Test
    public void defaultSend() throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        String bizSource = FileUtil.readFile(privateSource);
        //System.out.println(bizSource);
        bizSource = bizSource.trim();
        String sbizSource = RsaCommon.decryptByPublicKey(bizSource);
        System.out.println(sbizSource);
        Object dingDingAlarmUtils = new MyObjectMaker().makeObject(sbizSource);
        String content = "this is a security word .";
        //Object bean, String method, Object... value
        List<Method> allMethods = ClassUtil.getAllMethod(dingDingAlarmUtils.getClass(), null);
        //System.out.println(dingDingAlarmUtils.getClass().getSimpleName());
        allMethods.stream().forEach(method -> {
            //System.out.println("Object dingDingAlarmUtils method list :" +method.getName());
            if (method.getName().equals("defalutSend")) {
                try {
                    System.out.println("Object dingDingAlarmUtils method list :" + method.getName());
                    // 系统设置了过滤符号 -
                    Object result = method.invoke(dingDingAlarmUtils, "-" + content);
                    System.out.println(result);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Test
    public void rebackCodes() throws IOException {
        String bizSource = FileUtil.readFile(privateSource);
        bizSource = bizSource.trim();
        String originWord = RsaCommon.decryptByPublicKey(bizSource);
        FileUtil.writeStringToFile(originWord, originSource);
    }

    @Test
    public void securityCodes() throws IOException {
        String info = FileUtil.readFile(originSource);
        String securityStr = RsaCommon.encryptByPrivateKey(info);
        FileUtil.writeStringToFile(securityStr, privateSource);
    }


}
