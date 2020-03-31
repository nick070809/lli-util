package org.kx.util.biz;

import org.kx.util.base.StringUtil;
import org.kx.util.common.db.ModelFiled;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2018/9/7
 */

public class MoFiledUtil {





    public static List<ModelFiled> getModelFiled(Object obj) throws Exception {
        Field[] field = obj.getClass().getDeclaredFields();
        List<ModelFiled> modelFileds = new ArrayList<>();

        for (Field f : field) {
            String  fieldName = f.getName();

            if (fieldName.equals("serialVersionUID")){
                continue;
            }
            //过滤常量
             if(StringUtil.firstIsUpper(fieldName)){
                continue;
            }
            String  getMethod =   "get" + StringUtil.generateMethodFiled(fieldName);
            String  fieldType =  filedType(f) ;
            Object  value = getValue(getMethod,obj) ;
            modelFileds.add( new ModelFiled(fieldName,fieldType,getMethod,value) ) ;

        }
        return  modelFileds;
    }



    private static String filedType(Field f){
        String fieldType = f.getGenericType().toString() ;
        fieldType = fieldType.contains(".") ? fieldType.substring(fieldType.lastIndexOf('.') + 1) : fieldType;
        return fieldType.toUpperCase();
    }

    private  static Object getValue(String  method ,Object obj) throws Exception  {
        if(method.equals("getPTradeId")){
            method =  "getpTradeId";
        }
        Method method1  = obj.getClass().getMethod(method, null);
        return method1.invoke(obj,null);
    }

    private  static Object makeObj(List<ModelFiled> list,Class clazz) throws Exception  {




        return null;
    }


}
