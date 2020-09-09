package org.kx.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkx on 2017/3/27.
 */
public class ClassUtil {
    public static Integer  invokeGetMethod(Object obj,String property) {
        if(property == null )return 0;
        String s  =property.charAt(0)+"";
        property = "get"+s.toUpperCase() +   property.substring(1);
        Class clazz = obj.getClass();
        Method method;
        try {
            method = clazz.getDeclaredMethod(property);
            int hd = method.invoke(obj, null).hashCode();
            return  hd;
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e) {
        }
        return 0;
    }
    public static List<Method> getAllSetMethod(Class clazz){
        return getAllMethod(clazz,"set");
    }
    public static List<Method> getAllMethod(Class clazz, String prefix)
    {
        List method = new ArrayList();
        try {
            Method[] methods = clazz.getMethods();
            for (Method temp : methods) {
                if(prefix == null){
                    method.add(temp);
                }else if (temp.getName().startsWith(prefix)){
                    method.add(temp);
                }

            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
        return method;
    }

    public static Field[] getModelFileds(Class clazz) {
        return clazz.getDeclaredFields();
    }
    /**
     * 根据属性获得set方法
     * @param clazz
     * @return
     */


}
