package org.kx.util.biz;


import lombok.Data;
import org.kx.util.common.db.ModelFiled;

import java.util.ArrayList;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2018/9/8
 */

@Data
public class BizConfigStrategy {


    public final static Long userId = -1L;        //防止合同 过多
    public final static String contractNo = null;  //防止合同 过多
    public final static Integer version = null;    //防止合同实列 过多


    public final static String own_sign = "1";
    public final static boolean debug = true;
    public final static String redo_BizType = "";


    public static boolean ignoreSelectLog = false;
    public static boolean ignoreDeleteLog = false;
    public static boolean ignoreInsertLog = true;
    public static boolean ignoreFilterLog = true;
    public static boolean ignoreOtherLog = true;


    //过滤的列/表
    public List<ModelFiled> filterLines = new ArrayList();
    //？
    public String[] outbizTypes;
    //业务类型
    public String[] bizTypes;
    public String requestUrl = "";


}