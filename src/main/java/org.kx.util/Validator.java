package org.kx.util;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.base.BaseErrorCode;
import org.kx.util.base.BaseException;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sunkx
 */
public class Validator {
    public final static String GETPREFIX ="get";
    public final static String REQUIRED ="required";
    public final static String SIZE ="size:";
    public final static String IN ="in:";
    public final static String EMAIL ="email";
    public final static String MOBILE ="mobile";
    public final static String ISNUM ="isNum";
    /**不含有全角**/
    public final static String NOTCONTAINSFULLANGLE ="notContainsFullAngle";
    /**不含有中文**/
    public final static String NOTCONTAINSCHINESE ="notContainsChinese";

    //private static Logger logger = LoggerFactory.getLogger(Validator.class);


    public   static  void validateModel(final Object bean,Map<String, String> checkMap) throws BaseException {
        String err = validate(bean,checkMap);
        if(!err.isEmpty())throw new  BaseException(BaseErrorCode.WrongDataSpecified,err);
    }


    /**
     * sex required|size:1,10|in:male,female|email|mobile|isNum .etc
     */
    public   static  String validate(final Object bean,Map<String, String> checkMap){
        StringBuilder buffer = new  StringBuilder();
        Set<Method> allGetMethod = getAllGetMethod(bean.getClass());
        for (String property: checkMap.keySet()) {
            String getMethod = GETPREFIX +property.substring(0, 1).toUpperCase()+property.substring(1);
            Boolean find = false;
            for(Method m:allGetMethod){
                if(m.getName().equals(getMethod)){
                    check(property,invokeGetMethod(bean,m),checkMap.get(property),buffer);
                    find = true;
                    break;
                }
            }
            if(find == false){
                buffer.append("miss parameter [" + property +"]");
            }
        }
        return buffer.toString();
    }

    public   static  boolean noErrors(final Object bean,Map<String, String> checkMap) throws BaseException {
        String err = validate(bean,checkMap);
        if(err.isEmpty())return true;
        return false;
    }

    private static void check(String property, Object value,String condition,StringBuilder err){
        String[] dsls = StringUtils.split(condition, '|');
        String valueStr =null;
        if(value != null) valueStr = value.toString();

        for(String dsl :dsls){
            if (dsl.equals(REQUIRED)) {
                if(null == value){
                    err.append(property +"为空,");
                }else{
                    if(StringUtils.isEmpty(value.toString()))   err.append(property +"为空,");
                }
            }else if(dsl.startsWith(SIZE)){
                if(value != null) {
                    String col = dsl.substring(SIZE.length());
                    try{
                        if(col.contains(",")){
                            String[] minx = col.split(",");
                            int min = Integer.parseInt(minx[0]);
                            int max = Integer.parseInt(minx[1]);
                            if(valueStr.length() < min)err.append(property +"长度不够,");
                            if(valueStr.length() > max)err.append(property +"长度过长,");
                        }else{
                            int max = Integer.parseInt(col);
                            if(valueStr.length() > max)err.append(property +"长度过长,");
                        }
                    }catch(Exception e1){
                        e1.printStackTrace();
                    }
                }
            }else if(dsl.startsWith(IN)){
                if(value != null) {
                    String col = dsl.substring(IN.length());
                    String[] ins = col.split(",");
                    List<String> range= Arrays.asList(ins);
                    if(!range.contains(valueStr))err.append(property +"不在取值范围内,");
                }
            }else if(dsl.startsWith(EMAIL)){
                if(value != null) {
                    if(!checkEmail(valueStr))err.append(property +"邮件格式不对,");
                }
            }else if(dsl.startsWith(MOBILE)){
                if(value != null) {
                    if(!isMobile(valueStr))err.append(property +"手机号码不对,");
                }
            }else if(dsl.startsWith(ISNUM)){
                if(value != null) {
                    if(!isNum(valueStr))err.append(property +"不全为数字,");
                }
            }else if(dsl.startsWith(NOTCONTAINSFULLANGLE)){
                if(value != null) {
                    if(!notContainsFullAngle(valueStr))err.append(property +"含有全角字符,");
                }
            }
            else if(dsl.startsWith(NOTCONTAINSCHINESE)){
                if(value != null) {
                    if(!notContainsChinese(valueStr))err.append(property +"含有中文字符,");
                }
            }
        }
    }
    /**校验全为数字**/
    public static boolean isNum(String str){
        return str.matches("[0-9]+");
    }
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
    public static Boolean checkEmail(String str) {
        //邮箱正则表达式
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 含有中文
     */
    public static Boolean notContainsChinese(String str) {
        String regex = "[\u4e00-\u9fa5]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean flag = false;
        if (matcher.find()) {
            return  false;
        }
        return true;
    }
    /**
     * 不含有含有全角字符
     */
    public static Boolean notContainsFullAngle(String str) {
        str = str.replaceAll("[\u4e00-\u9fa5]", "");
        char[] chars_test1 = str.toCharArray();
        for (int i = 0; i < chars_test1.length; i++) {
            String temp = String.valueOf(chars_test1[i]);
            // 判断是全角字符
            if (temp.matches("[^\\x00-\\xff]")) {
                return  false;
                //throw new BaseException(ErrorCodeConstants.WrongDataSpecified,"含有全角字符","");
            }
        }
        return  true;
    }

    private static Object invokeGetMethod(Object bean, Method method){
        Object result = null;
        try
        {
            result = method.invoke(bean);
        }
        catch (Exception e) {
            throw new  BaseException(BaseErrorCode.WrongDataSpecified,"获取属性方法错误");
        }
        return result;
    }
    /**
     * pub获取所有get方法
     */
    public static Set<Method> getAllGetMethod(Class clazz)
    {
        Set<Method> method = new HashSet<Method>();
        try {
            Method[] methods = clazz.getMethods();
            for (Method temp : methods) {
                if (temp.getName().startsWith(GETPREFIX))
                    method.add(temp);
            }
        }
        catch (Exception ex)
        {
            throw new  BaseException(BaseErrorCode.WrongDataSpecified,"获取属性方法错误");
        }
        return method;
    }


}
