package org.kx.util.odps;

import nick.doc.AbstractCode;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/7/29
 */

public class SpRuleUnit extends AbstractCode {

    public SpRuleUnit(){
        super.privateSource = "/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/codes/private/SpRule.txt";
        super.originSource = "/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/java/SpRule.java";
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

    @Test
    public void showAllMethod() throws Exception {
        super.showAllMethod();
    }



    @Test
    public void witreCreateAndCleanSql() throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        Method method = getMethod(methodName);
        Object result = method.invoke(getObject());
        System.out.println(result);
    }
}
