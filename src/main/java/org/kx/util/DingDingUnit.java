package org.kx.util;

import nick.doc.AbstractCode;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/8/11
 */

public class DingDingUnit extends AbstractCode {

    public DingDingUnit(){
        super.privateSource = "/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/codes/private/DingDingAlarmUtils.txt";
        super.originSource = "/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/java/DingDingAlarmUtils.java";
    }


    @Test
    public void defalutSend() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        Method method = getMethod(methodName);
        String content = "this is a test word .";
        Object result = method.invoke(getObject(), "-" + content);
        System.out.println(result);

    }


    @Test
    public void securityCodes() throws IOException {
        super.securityCodes();
    }

    @Test
    public void showCodes() throws IOException {
        super.showCodes();
    }

    @Test
    public void rebackCodes() throws IOException {
        super.rebackCodes();
    }


}
