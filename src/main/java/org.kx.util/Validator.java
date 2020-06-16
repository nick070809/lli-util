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
                    if(!containsFullAngle(valueStr))err.append(property +"含有全角字符,");
                }
            }
            else if(dsl.startsWith(NOTCONTAINSCHINESE)){
                if(value != null) {
                    if(containsChinese(valueStr))err.append(property +"含有中文字符,");
                }
            }
        }
    }
    /**校验全为数字**/
    public static boolean isNum(String str){
        return str.matches("[0-9]+");
    }

    // 判断一个字符串是否含有数字
    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }


    /**校验全为字母**/
    public static boolean isChars(String str){
        return str.matches("[a-zA-Z]+");
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
    public static Boolean containsChinese(String str) {
        String regex = "[\u4e00-\u9fa5]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return  true;
        }
        return false;
    }
    /**
     * 不含有含有全角字符
     */
    public static Boolean containsFullAngle(String str) {
        str = str.replaceAll("[\u4e00-\u9fa5]", "");
        char[] chars_test1 = str.toCharArray();
        for (int i = 0; i < chars_test1.length; i++) {
            String temp = String.valueOf(chars_test1[i]);
            // 判断是全角字符
            if (temp.matches("[^\\x00-\\xff]")) {
                return  true;
                //throw new BaseException(ErrorCodeConstants.WrongDataSpecified,"含有全角字符","");
            }
        }
        return  false;
    }

    /**
     * 是否包含中文字符<br>
     * 包含中文标点符号<br>
     *
     * @param str
     * @return
     */
    public static boolean hasChinese(String str) {
        if (str == null) {
            return false;
        }
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否全是中文字符<br>
     * 包含中文标点符号<br>
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (str == null) {
            return false;
        }
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (!isChinese(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是中文字符<br>
     * 包含中文标点符号<br>
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C) {
            return true;
        } else if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D) {
            return true;
        } else if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 是否包含汉字<br>
     * 根据汉字编码范围进行判断<br>
     * CJK统一汉字（不包含中文的，。《》（）“‘'”、！￥等符号）<br>
     *
     * @param str
     * @return
     */
    public static boolean hasChineseByReg(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str).find();
    }

    /**
     * 是否全是汉字<br>
     * 根据汉字编码范围进行判断<br>
     * CJK统一汉字（不包含中文的，。《》（）“‘'”、！￥等符号）<br>
     *
     * @param str
     * @return
     */
    public static boolean isChineseByReg(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str).matches();
    }

    /**
     * 是否包含汉字<br>
     * 根据汉字编码范围进行判断<br>
     * CJK统一汉字（不包含中文的，。《》（）“‘'”、！￥等符号）<br>
     *
     * @param str
     * @return
     */
    public static boolean hasChineseByRange(String str) {
        if (str == null) {
            return false;
        }
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (c >= 0x4E00 && c <= 0x9FBF) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否全是汉字<br>
     * 根据汉字编码范围进行判断<br>
     * CJK统一汉字（不包含中文的，。《》（）“‘'”、！￥等符号）<br>
     *
     * @param str
     * @return
     */
    public static boolean isChineseByRange(String str) {
        if (str == null) {
            return false;
        }
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (c < 0x4E00 || c > 0x9FBF) {
                return false;
            }
        }
        return true;
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
