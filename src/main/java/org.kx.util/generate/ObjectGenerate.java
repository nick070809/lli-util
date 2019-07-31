package org.kx.util.generate;

import com.alibaba.fastjson.JSON;
import org.kx.util.ClassUtil;
import org.kx.util.DateUtil;
import org.kx.util.base.StringUtil;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by sunkx on 2017/5/11.
 */
public class ObjectGenerate {
    //TODO
    public static String makeNewObj(Class clazz) {
        String name = clazz.getSimpleName();
        String oname = StringUtil.LowerFirstWord(name);
        StringBuilder sb = new StringBuilder(name + " " + oname + "  = new " + name + "();" + "\r\n");
        List<Method> list = ClassUtil.getAllSetMethod(clazz);
        long i = System.currentTimeMillis();
        int y = 23+new Random().nextInt(100);
        for (Method method : list) {
            Class type = method.getParameterTypes()[0];
            try {
                if (type.equals(String.class)) {
                    if(method.getName().endsWith("Currency")){
                        sb.append(oname + "." + method.getName() + "(\"CNY\")");
                    }else if(method.getName().endsWith("Country")){
                        sb.append(oname + "." + method.getName() + "(\"CN\")");
                    }else if(method.getName().endsWith("Version")){
                        sb.append(oname + "." + method.getName() + "(\"1\")");
                    }else if(method.getName().endsWith("OwnSign")){
                        sb.append(oname + "." + method.getName() + "(\"1\")");
                    }else{
                        sb.append(oname + "." + method.getName() + "(\"" + i + "\")");
                    }
                } else if (type.equals(Integer.class)) {
                    if(method.getName().endsWith("Version")){
                        sb.append(oname + "." + method.getName() + "(1)");
                    }else{
                        sb.append(oname + "." + method.getName() + "(" + y + ")");
                    }
                } else if (type.equals(Long.class)) {
                    sb.append(oname + "." + method.getName() + "(" + i + "L)");
                } else if (type.equals(Double.class)) {
                    sb.append(oname + "." + method.getName() + "(" + i + ".0)");
                } else if (type.equals(Long.TYPE)) {
                    if(method.getName().endsWith("Type") || method.getName().endsWith("Status")){
                        sb.append(oname + "." + method.getName() + "(1)");
                    }else {
                        sb.append(oname + "." + method.getName() + "(" + i + "L)");
                    }

                } else if (type.equals(Byte.TYPE)) {
                    sb.append("Byte "+method.getName()+" = 1;");
                    sb.append(oname + "." + method.getName() + "("+method.getName()+")");
                } else if (type.equals(Integer.TYPE)) {
                    if(method.getName().endsWith("type") || method.getName().endsWith("status")){
                        sb.append(oname + "." + method.getName() + "(1)");
                    }else {
                        sb.append(oname + "." + method.getName() + "(" +y + ")");
                    }
                } else if (type.equals(Short.TYPE)) {
                    sb.append(oname + "." + method.getName() + "(((short))" + y + ")");
                } else if (type.equals(Float.TYPE)) {
                    sb.append(oname + "." + method.getName() + "(" + y + ".0)");
                } else if (type.equals(Date.class)) {
                    sb.append(oname + "." + method.getName() + "(new Date())");
                } else if (type.equals(Timestamp.class)) {
                    sb.append(oname + "." + method.getName() + "(new Date())");
                } else if (type.equals(Double.TYPE)) {
                    sb.append(oname + "." + method.getName() + "(" + y + ".0)");
                } else if (type.equals(Boolean.TYPE)) {
                    sb.append(oname + "." + method.getName() + "(false)");
                } else if (type.equals(Character.TYPE)) {
                    sb.append(oname + "." + method.getName() + "('c')");
                } else if (type.equals(BigDecimal.class)) {
                    sb.append(oname + "." + method.getName() + "(new BigDecimal("+y+"))");
                }else if (type.equals(Currency.class)) {
                    sb.append(oname + "." + method.getName() + "(Currency.getInstance(\"CNY\"))");
                }else if (type.equals(Enum.class)) {
                    sb.append(oname + "." + method.getName() + "("+type.getSimpleName()+"");
                }else if(type.getName().equals("java.lang.Byte")){
                    sb.append("Byte "+method.getName()+" = 1;\r\n");
                    sb.append(oname + "." + method.getName() + "("+method.getName()+")");
                }
                else {
                    sb.append(oname + "." + method.getName() + "(new " + type.getSimpleName() + "())");
                }
                i++;
                sb.append(";\r\n");
            } catch (Exception ex) {

            }
        }
        sb.append("return  " + oname + ";");
        System.out.println(sb.toString());

        return sb.toString();
    }

    //TODO
    public static Object initValue(Class clazz) throws IllegalAccessException, InstantiationException {
        Object obj = clazz.newInstance();
        List<Method> list = ClassUtil.getAllSetMethod(clazz);
        int i = 1;
        for (Method method : list) {
            Class type = method.getParameterTypes()[0];
            try {
                if (type.equals(String.class)) {

                    if(method.getName().endsWith("Currency")){
                        method.invoke(obj,  "CNY");
                    }else if(method.getName().endsWith("Country")){
                        method.invoke(obj,  "CN");
                    }else if(method.getName().endsWith("Version")){
                        method.invoke(obj,  "0");
                    }else if(method.getName().endsWith("OwnSign")){
                        method.invoke(obj,  "1");
                    }else if(method.getName().endsWith("ExtendInfo")){
                        HashMap vv = new HashMap();
                        vv.put("testKey","testValue");

                        method.invoke(obj,  JSON.toJSONString(vv));
                    }else{
                        method.invoke(obj, i + "");
                    }
                } else if (type.equals(Integer.class)) {
                    method.invoke(obj, i);
                } else if (type.equals(Long.class)) {
                    method.invoke(obj, (long) i);
                } else if (type.equals(Double.class)) {
                    method.invoke(obj, i + 0.2);
                } else if (type.equals(Long.TYPE)) {
                    method.invoke(obj, (long) i);
                } else if (type.equals(Integer.TYPE)) {
                    method.invoke(obj, i);
                } else if (type.equals(Short.TYPE)) {
                    method.invoke(obj, (short) i);
                } else if (type.equals(Float.TYPE)) {
                    method.invoke(obj, i + 0.2);
                } else if (type.equals(Date.class)) {
                    method.invoke(obj, new Date());
                } else if (type.equals(Timestamp.class)) {
                    method.invoke(obj, new Date());
                } else if (type.equals(Double.TYPE)) {
                    method.invoke(obj, i + 0.2);
                } else if (type.equals(Boolean.TYPE)) {
                    method.invoke(obj, false);
                } else if (type.equals(Character.TYPE)) {
                    method.invoke(obj, "c");
                } else if (type.equals(BigDecimal.class)) {
                    method.invoke(obj, new BigDecimal(5));
                }else if (type.equals(ArrayList.class)) {
                    method.invoke(obj, new ArrayList(5));
                }else if (type.equals(Map.class)) {
                    method.invoke(obj, new HashMap(5));
                }
                i++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return obj;
    }


    public static String makeParm(Class type) {
        if (type.equals(String.class)) {
            return "\""+DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm")+"\"";
        } else if (type.equals(Integer.class)) {
            return "" + Integer.parseInt(DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm"));
        } else if (type.equals(Long.class)) {
            return "" + Long.parseLong(DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm"));
        } else if (type.equals(Double.class)) {
            return "" + Integer.parseInt(DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm")) + 0.1;
        } else if (type.equals(Long.TYPE)) {
            return "" + Long.parseLong(DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm"));
        } else if (type.equals(Integer.TYPE)) {
            return "" + Integer.parseInt(DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm"));
        } else if (type.equals(Short.TYPE)) {
            return "1";
        } else if (type.equals(Float.TYPE)) {
            return "" + Integer.parseInt(DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm")) + 0.2;
        } else if (type.equals(Date.class)) {
            return "new Date()";
        } else if (type.equals(Timestamp.class)) {
            return "new Date()";
        } else if (type.equals(Double.TYPE)) {
            return "" + Integer.parseInt(DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm")) + 0.2;
        } else if (type.equals(Boolean.TYPE)) {
            return "false";
        } else if (type.equals(Character.TYPE)) {
            return "c";
        } else if (type.equals(BigDecimal.class)) {
            return "" + Integer.parseInt(DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm")) + 0.2;
        }
        return "" + Integer.parseInt(DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm"));
    }


    public static void main(String[] args) {

    }
}


