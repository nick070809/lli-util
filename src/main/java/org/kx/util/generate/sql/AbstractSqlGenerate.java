package org.kx.util.generate.sql;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.DateUtil;
import org.kx.util.Validator;
import org.kx.util.base.str.StringUtil;
import org.kx.util.generate.FieldName;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/15
 */

public abstract class AbstractSqlGenerate implements SqlGenerateUnits {
    public static String getClumnName(String clum) {
        if (clum.startsWith("(")) {
            clum = clum.substring(1).trim();
        }
        if (clum.startsWith("`")) {
            clum = clum.substring(1).trim();
        }
        if (clum.endsWith("`")) {
            clum = clum.substring(0, clum.length() - 1).trim();
        }
        if (clum.endsWith(")")) {
            clum = clum.substring(0, clum.length() - 1).trim();
        }
        return clum;
    }


    public String[] parasLine(String line, String mask, String sql) {
        String[] t1s = line.split(mask);
        List<String> oi = new ArrayList<>();
        int index = 0;
        for (String word : t1s) {
            if (StringUtils.isNotBlank(word)) {
                String xord = word.trim();
                if (index < 3) {
                    if (xord.contains("(")) {
                        String[] xords = xord.split("\\(");
                        for (String xdd : xords) {
                            String xdd_ = xdd.trim();
                            oi.add(getClumnName(xdd_));
                            index++;
                        }
                    }
                }
                if (index > 0) {
                    if (DbOption.update_.equals(xord) || DbOption.select_.equals(xord) || DbOption.delete_.equals(xord) || DbOption.insert_.equals(xord)) {
                        throw new RuntimeException("sql is error :" + sql);
                    }
                }
                oi.add(getClumnName(xord));
                index++;
            }
        }
        String[] strings = new String[oi.size()];
        oi.toArray(strings);
        return strings;
    }

    public static String parseTableName(String tableName) {
        if (tableName.startsWith("`")) {
            tableName = tableName.substring(1).trim();
        }
        if (tableName.endsWith("`")) {
            tableName = tableName.substring(0, tableName.length() - 1).trim();
        }
        return tableName;
    }

    public static String getClumnValue(String clomunValue) {
        if (clomunValue.startsWith("'")) {
            clomunValue = clomunValue.substring(1);
        }
        if (clomunValue.endsWith("'")) {
            clomunValue = clomunValue.substring(0, clomunValue.length() - 1);
        }
        return clomunValue;
    }

    private boolean isShareTable_(String tableName) {
        //获取最后一位是数字
        String lastChar = getTableName().substring(tableName.length() - 1);
        if (Validator.isNum(lastChar)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isShareTable() {
        String tableName = getTableName();
        return isShareTable_(tableName);
    }

    private String getShareTableName(String tableName, String shareMask) {
        //分片符前面几位为表名
        return tableName.substring(0, tableName.lastIndexOf(shareMask));
    }


    @Override
    public String getBizTableName() {
        String tableName = getTableName();
        boolean isShareTable = isShareTable_(tableName);
        String shareTableName = tableName;
        if (isShareTable) {
            shareTableName = getShareTableName(tableName, "_");
        }
        return shareTableName;
    }


    /**
     * 将map集合中的数据转化为指定对象的同名属性中
     */
    public static <T> T map2Bean(Map<String, String> source, Class<T> instance, boolean isHtml) {
        try {
            T object = instance.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    FieldName fieldName = field.getAnnotation(FieldName.class);
                    if (fieldName != null) {
                        field.set(object, source.get(fieldName.value()));
                    } else {
                        String fieldName_ = field.getName();
                        String aa_bb = StringUtil.Aa_Bb(fieldName_).toLowerCase();
                        if (source.get(aa_bb) != null) {
                            String v = source.get(aa_bb);
                            if (StringUtils.isBlank(v) || "null".equals(v)) {
                                continue;
                            }
                            if (field.getType().getName().equals("java.lang.Byte")) {
                                Byte b = new Byte(v);
                                field.set(object, b);
                            } else if (field.getType().getName().equals("java.lang.Integer")) {
                                field.set(object, Integer.parseInt(v));
                            } else if (field.getType().getName().equals("java.lang.Double")) {
                                field.set(object, Double.parseDouble(v));
                            } else if (field.getType().getName().equals("java.lang.Long")) {
                                field.set(object, Long.parseLong(v));
                            } else if (field.getType().getName().equals("java.util.Date")) {
                                if ("now()".equals(v)) {
                                    Date date = new Date();
                                    field.set(object, date);
                                } else {
                                    Date date = DateUtil.getDate(v, "yyyy-MM-dd HH:mm:ss");
                                    field.set(object, date);
                                }
                            } else if (field.getType().getName().equals("java.lang.Boolean")) {
                                if ("1".equals(v)) {
                                    field.set(object, true);
                                    continue;
                                } else if ("0".equals(v)) {
                                    field.set(object, false);
                                    continue;
                                }
                                Boolean b = Boolean.valueOf(v);
                                field.set(object, b);
                            } else if (field.getType().getName().equals("java.math.BigDecimal")) {
                                BigDecimal bigDecimal = new BigDecimal(v);
                                field.set(object, bigDecimal);
                            } else {
                                if (v.contains(DbChars.huanHang)) {
                                    if (isHtml) {
                                        v = v.replace(DbChars.huanHang, "<br/>");
                                    } else {
                                        v = v.replace(DbChars.huanHang, "\n");
                                    }
                                }
                                field.set(object, v);
                            }
                        }
                    }
                } catch (Exception x) {
                    throw new RuntimeException("field.name " + field.getName(), x);
                }
            }
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getObjectHtml(Class<T> instance) {
        return null;
    }
}
