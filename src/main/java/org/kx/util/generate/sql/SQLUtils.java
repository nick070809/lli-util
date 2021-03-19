package org.kx.util.generate.sql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/15
 */

public class SQLUtils {

    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<>();
        map.put("stuName", "欧可乐");
        map.put("stuAge", 20);
        map.put("stuSex", "男");
        map.put("Key_stuId", "ASDF");
        map.put("Key_stuSex", "ASDF");
        try {
            System.out.println(getSql("table_name", "insert", map, false, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 动态组装 简单sql语法
     *
     * @param tableName 表名
     * @param operation 操作标识符 select|delete|update ,默认为 select
     * @param mapData   数据的map集合
     * @param useMySQL  true|false , false 为使用动态组装SQL，true为使用自已的sql
     * @param mySql     自已的sql
     *                  注意：update 这里，where xxx = xxx ,的时候，mapData 里的键必须要有 Key_ 前缀（其他的 并不影响到）
     * @return
     * @throws Exception
     */
    public static String getSql(String tableName, String operation, Map<?, ?> mapData, boolean useMySQL, String mySql) throws Exception {
        String sql = null;
        // 使用组装sql的功能
        if (!useMySQL) {
            if (!(tableName != null && !tableName.equals("") && tableName.length() > 0)) {
                throw new Exception(" 参数 tableName 的值为空！");
            } else if (!(mapData != null && !mapData.equals("") && mapData.size() > 0)) {
                throw new Exception(" 参数 mapData 的值为空！");
            }
            // 操作标识 默认为 select
            String operations = "select";
            String condition = " a.* from " + tableName + " a where ";
            if (operation != null && !operation.equals("")) {
                if (operation.equals("update") || operation.equals("UPDATE")) {
                    operations = "update";
                    condition = " " + tableName + " a set ";
                } else if (operation.equals("delete") || operation.equals("DELETE")) {
                    operations = "delete";
                    condition = " from " + tableName + " a where ";
                } else if (operation.equals("insert") || operation.equals("INSERT")) {
                    operations = "insert";
                    condition = " into " + tableName + " (";
                    String link = "";
                    Iterator<?> iterator = mapData.keySet().iterator();
                    while (iterator.hasNext()) {
                        String next = (String) iterator.next();
                        condition += link + next;
                        link = ",";
                    }
                    condition += ") values( ";
                }
            }
            String value = "";
            String link = "";
            String keyValueOperations = " where ";
            Iterator<? extends Map.Entry<?, ?>> iterator = mapData.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<?, ?> next = iterator.next();
                if (next.getValue() instanceof String) {
                    value = "'" + next.getValue() + "'";
                } else {
                    value = "" + next.getValue() + "";
                }
                if (next.getKey().toString().lastIndexOf("Key_") == -1) {
                    if (!operations.equals("insert")) {
                        if (operations.equals("select") || operations.equals("delete")) {
                            condition += link + "a." + next.getKey();
                            condition += "=" + value;
                            link = " and ";
                        } else {
                            condition += link + "a." + next.getKey();
                            condition += "=" + value;
                            link = ",";
                        }
                    } else {
                        condition += link + value;
                        link = ",";
                    }
                } else {
                    continue;
                }
            }

            // 组装 insert sql 的结尾
            if (operations.equals("insert")) {
                condition += ")";
            } else if (operations.equals("update")) { // 组装 update sql 的结尾
                condition += " where ";
                String and = "";
                Iterator<? extends Map.Entry<?, ?>> iterator1 = mapData.entrySet().iterator();
                while (iterator1.hasNext()) {
                    Map.Entry<?, ?> next = iterator1.next();
                    if (next.getValue() instanceof String) {
                        value = "'" + next.getValue() + "'";
                    } else {
                        value = "" + next.getValue() + "";
                    }
                    String key = next.getKey().toString();
                    if (key.lastIndexOf("Key_") != -1) {
                        key = key.substring(key.indexOf("Key_") + 4, key.length());
                        condition += and + "a." + key + "=" + value;
                        and = " and ";
                    }
                }
            }

            sql = operations + condition;
        } else { // 不使用组装sql的功能
            sql = mySql;
        }
        if(sql != null){
            sql = sql +" ;";
        }
        return sql;

    }
}
