package org.kx.util;

import com.lianlianpay.lli.common.BaseErrorCode;
import com.lianlianpay.lli.common.BaseException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunkx on 2017/4/17.
 */
public class Validator {

    public static void notBlank(Object t, String message) {
        if(t ==null)throw new BaseException(BaseErrorCode.WrongDataSpecified, message);
        if(t instanceof  String ){
            if (StringUtils.isBlank((String) t)) throw new BaseException(BaseErrorCode.WrongDataSpecified, message);
        }
    }

    public static boolean isNum(String str) {
        return str.matches("[0-9]+");
    }

    public static void notEmail(String str, String message) {
        if (StringUtils.isBlank(str)) throw new BaseException(BaseErrorCode.WrongDataSpecified, "email is null");
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(str);
        if(!matcher.matches())
            throw new BaseException(BaseErrorCode.WrongDataSpecified, message);
    }
}
