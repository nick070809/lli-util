package org.kx.util.generate;

import com.xian.model.report.PrivateClass;
import org.kx.util.base.CanNotRun;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/30
 */

public class IllegalReadObj<T> {

    public static <T>Object getPrivateProperty(Object bean, String property) throws Exception {
        Class<?> clazz = bean.getClass();
        Field[] fs = clazz.getDeclaredFields();// 获取PrivateClass所有属性
        for (int i = 0; i < fs.length; i++) {
            if (property.equals(fs[i].getName())) {
                fs[i].setAccessible(true);// 将目标属性设置为可以访问
                return fs[i].get(bean);
                // fs[i].set(pc, "null");//将属性值重新赋值
            }
        }

        return null;
    }

    @CanNotRun
    public static Object invokeSetNullMethod(Object bean, String method, Class type) throws Exception {
        Class<?> clazz = bean.getClass();

        Method m = clazz.getDeclaredMethod(method, type);
        m.setAccessible(true);
        return m.invoke(bean,"");

    }

    public static Object invokePrivateMethod(Object bean, String method, Object... value) throws Exception {

        Class<?> clazz = bean.getClass();
        if (value == null || value.length == 0) {
            //判断是set还是get bug 不能设置为null
            Method m = clazz.getDeclaredMethod(method);
            m.setAccessible(true);
            return m.invoke(bean);

        }
        int size = value.length;
        if (size > 5) {
            throw new RuntimeException("not surport so much parameters " + size);
        }
        if (size == 1) {
            Method m = clazz.getDeclaredMethod(method, value[0].getClass());
            m.setAccessible(true);
            return m.invoke(bean, value[0]);
        } else if (size == 2) {
            Method m = clazz.getDeclaredMethod(method, value[0].getClass(), value[1].getClass());
            m.setAccessible(true);
            return m.invoke(bean, value[0], value[1]);
        } else if (size == 3) {
            Method m = clazz.getDeclaredMethod(method, value[0].getClass(), value[1].getClass(), value[2].getClass());
            m.setAccessible(true);
            return m.invoke(bean, value[0], value[1], value[2]);
        } else if (size == 4) {
            Method m = clazz.getDeclaredMethod(method, value[0].getClass(), value[1].getClass(), value[2].getClass(),
                value[3].getClass());
            m.setAccessible(true);
            return m.invoke(bean, value[0], value[1], value[2], value[3]);
        } else if (size == 5) {
            Method m = clazz.getDeclaredMethod(method, value[0].getClass(), value[1].getClass(), value[2].getClass(),
                value[3].getClass(), value[4].getClass());
            m.setAccessible(true);
            return m.invoke(bean, value[0], value[1], value[2], value[3], value[4]);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        //https://blog.csdn.net/gretchen_grundler/article/details/79550530
       /* Object object =  new  IllegalReadObj().getPrivateProperty(new PrivateClass(),"id");
        System.out.println(object);*/
        PrivateClass dd = new PrivateClass();
       // System.out.println(IllegalReadObj.invokeSetNullMethod(dd, "seturl", String.class));
        System.out.println(IllegalReadObj.invokePrivateMethod(dd, "seturl", "3"));
        //System.out.println(IllegalReadObj.invokePrivateMethod(dd, "getUrl", "3",32));

    }
}
