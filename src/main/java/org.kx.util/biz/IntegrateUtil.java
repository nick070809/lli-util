package org.kx.util.biz;

import org.kx.util.DateUtil;
import org.kx.util.base.StringUtil;
import org.kx.util.common.db.ModelFiled;

import java.io.IOException;
import java.util.*;


/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2018/9/7
 */

public class IntegrateUtil {
    private static String tableProfix = "hj_";
    private static String primeKey = "id";
    private   int size = 0;
    private   BizConfigStrategy bizConfigStrategy;
    private   Map repeatKey = new HashMap();
    public    Map<String, Map<SqlLevel, List<String>>> sqlcons = null;



    //=============================

    //不能多，只能少
    public static void checkClomun(String clomun, String table, List<String> clomuns) throws Exception {
        //List<String> clomuns = TableClomn.getClomuns(table);
        for (String clomun0 : clomuns) {
            if (clomun0.equals(clomun)) {
                return;
            }
        }
        throw new RuntimeException("not find clomun " + clomun + " in table " + table);
    }

    public String getInsertSql(Object obj) throws Exception {
        return getInsertSql(obj, null);
    }


    public String getInsertRepeatSql(String table, Long id, Object obj) throws Exception {
        String key = table + "_" + id;
        if (repeatKey.get(key) != null) {
            return null;
        } else {
            repeatKey.put(key, 1);
        }
        return getInsertSql(obj, null);
    }


    public String getInsertSql(Object obj, Map<String, Object> map) throws Exception {
        List<ModelFiled> fileds = MoFiledUtil.getModelFiled(obj);
        if (fileds == null || fileds.isEmpty()) {
            return null;
        }
        String tableName = getTableName(obj);
        StringBuilder insert1 = new StringBuilder();
        StringBuilder values1 = new StringBuilder();
        boolean start = false;
        for (ModelFiled filed : fileds) {
            filed.setTableName(tableName);
            if (isFiltered(filed)) {
                continue;
            }

            if (filed.getValue() != null && !filed.getClomunName().equals(primeKey)) {
                String clomunName = filed.getClomunName();

                // checkClomun(clomunName,tableName);
                if (start) {
                    insert1.append(", ").append(clomunName);
                    values1.append(", ").append(filed.getDecorationValue(bizConfigStrategy.own_sign));
                } else {
                    insert1.append(clomunName);
                    values1.append(filed.getDecorationValue(bizConfigStrategy.own_sign));
                    start = true;
                }
            }
        }

        //对象没有的数据,数据库/业务 要求的数据 ；
        if (map != null && !map.isEmpty()) {
            for (String instr : map.keySet()) {
                //checkClomun(instr,tableName);
                insert1.append(", ").append(instr);
                values1.append(", ");
                Object value = map.get(instr);
                if (value instanceof Number) {
                    values1.append(value);
                } else if (value instanceof Boolean) {
                    values1.append(value);
                } else if (value instanceof String) {
                    values1.append("\'" + value + "\'");
                } else if (value instanceof Date) {
                    values1.append("\'" + DateUtil.getDateTimeStr((Date) value, "yyyy-MM-dd HH:mm:ss") + "\'");
                } else {
                    values1.append(value.toString());
                }

            }
        }
        String sss = "insert into " + tableName + "(" + insert1.toString() + ") \r\n values   (\r\n" + values1.toString() + ") ;";
        return sss;
    }


    public static String getTableName(Object obj) {
        return getTableName2(obj.getClass());
    }


    public static String getTableName2(Class clazz) {
        String objName = clazz.getSimpleName();
        if (objName.endsWith("ATO") || objName.endsWith("MTO") || objName.endsWith("DTO")) {
            objName = objName.substring(0, objName.length() - 3);
        } else if (objName.endsWith("TO") || objName.endsWith("DO")) {
            objName = objName.substring(0, objName.length() - 2);
        }

        return tableProfix + StringUtil.generateColumn(objName);
    }


    public boolean isFiltered(ModelFiled filed) {

        try {
            if (bizConfigStrategy.getFilterLines() != null && !bizConfigStrategy.getFilterLines().isEmpty()) {
                for (ModelFiled filedq : bizConfigStrategy.getFilterLines()) {
                    if (filedq.getTableName().equals(filed.getTableName())) {
                        //过滤值
                        Object value = filed.getValue();
                        if (filedq.getClomunName() != null && filedq.getValue() != null && value != null
                                && filedq.getClomunName().equals(filed.getClomunName()) && filedq.getValue().equals(value)) {
                            return true;
                        }
                        //过滤所用值
                        if (filedq.getClomunName() != null && filedq.getValue() == null
                                && filedq.getClomunName().equals(filed.getClomunName())) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }


        return false;
    }


   /* public static boolean isFilteredTableBiz(String tableName ,String bizType,BizConfigStrategy bizConfigStrategy){

        if(bizConfigStrategy.getReduceLine() != null  && !bizConfigStrategy.getReduceLine().isEmpty()) {
            for (ModelFiled filedq : bizConfigStrategy.getReduceLine()) {

                if(filedq.getTableName().equals(tableName) ){
                    //过滤业务类型
                    if(filedq.getClomunName() != null && filedq.getValue() != null && filedq.getClomunName().equals("biz_type") &&  filedq.getValue().equals(bizType)){
                        return true;
                    }
                    //过滤表
                    if(filedq.getClomunName() == null){
                        return true;
                    }
                }

            }
        }
        return  false ;
    }*/


    public   boolean outBiztype(String bizType, BizConfigStrategy bizConfigStrategy) {
        if (bizConfigStrategy.getOutbizTypes() != null) {
            for (String biz : bizConfigStrategy.getOutbizTypes()) {
                if (bizType.equals(biz)) {
                    return true;
                }
            }
        }
        return false;
    }


    public   void putInfo(String bizType, SqlLevel sqlLevel, String info) {
        if (info == null) {
            return;
        }
        /*if(IntegrateUtil.outBiztype(bizType)){ ？
            return;
        }*/
        if (BizConfigStrategy.ignoreDeleteLog && info.startsWith("delete")) {
            return;
        }
        if (BizConfigStrategy.ignoreSelectLog && info.startsWith("select")) {
            return;
        }
        if (BizConfigStrategy.ignoreInsertLog && info.startsWith("insert")) {
            return;
        }
        if (BizConfigStrategy.ignoreOtherLog && info.startsWith("_00_")) {
            return;
        }
        if (info.startsWith("insert")) {
            size = size + 1;
        }
        if (size > 300) {
            throw new RuntimeException("size is more than 300");
        }
        if (sqlcons == null) {
            sqlcons = new HashMap<>();
        }
        Map<SqlLevel, List<String>> bizSqls = sqlcons.get(bizType);
        if (bizSqls == null) {
            bizSqls = new HashMap<>();
            sqlcons.put(bizType, bizSqls);
        }
        List<String> levelsqls = bizSqls.get(sqlLevel);
        if (levelsqls == null) {
            levelsqls = new ArrayList<String>();
            bizSqls.put(sqlLevel, levelsqls);
        }
        levelsqls.add("\r\n" + info);

    }


    public String getSql() throws IOException {
        if (sqlcons == null) {
            return "not found a info ";
        }
        StringBuilder sbt = null;
        String total = "\r\nINSERT SQL TOTAL SIZE : " + size;
        String start = "\n当前环境是 : " + bizConfigStrategy.own_sign;
        sbt = new StringBuilder(start);
        sbt.append(total);
        sbt.append("\r\n需求链接 : " + bizConfigStrategy.getRequestUrl());
        for (Map.Entry<String, Map<SqlLevel, List<String>>> entry : sqlcons.entrySet()) {
            String biz = entry.getKey();
            Map<SqlLevel, List<String>> bizSqls = entry.getValue();
            if (bizSqls == null) {
                return  "bizType " + biz + " not found a info ";
            } else {
                if (bizSqls.isEmpty()) {
                    continue;
                }
                String title = "\r\n\r\n=====================================\r\nbiz :" + biz + "" +
                        "\r\n=====================================";

                sbt.append(title);


                for (SqlLevel sqlLevel : SqlLevel.values()) {
                    List<String> infos = bizSqls.get(sqlLevel);
                    if (infos != null && !infos.isEmpty()) {
                        if (!sqlLevel.equals(SqlLevel.First)) {
                            String level = "\r\n\r\n-------------------------------- level " + sqlLevel.name() + " --------------------------------";
                            sbt.append("\r\n\r\n");

                        }
                        for (String sql : infos) {
                            sbt.append(sql);
                            sbt.append("\r\n");
                        }
                    }
                }
            }
        }

        return sbt.toString();
    }


    public BizConfigStrategy getBizConfigStrategy() {
        return bizConfigStrategy;
    }

    public void setBizConfigStrategy(BizConfigStrategy bizConfigStrategy) {
        this.bizConfigStrategy = bizConfigStrategy;
    }

}
