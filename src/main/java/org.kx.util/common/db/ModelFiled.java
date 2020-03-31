package org.kx.util.common.db;

import com.alibaba.fastjson.JSON;
import org.kx.util.DateUtil;
import org.kx.util.base.StringUtil;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunkx on 2017/5/17.
 * model属性类型
 */
public class ModelFiled {
    private String tableName;
    private String  fieldName;
    private String  clomunName;
    private String  javaType;
    private String  jdbcType;
    private String  clomunType;
    private String  clomunChnName;
    private String setMethod;
    private Object value;
    private String getMethod;



    public ModelFiled(String fieldName, String clomunName, String javaType, String jdbcType, String clomunType, String clomunChnName) {
        this.fieldName = fieldName;
        this.clomunName = clomunName;
        this.javaType = javaType;
        this.jdbcType = jdbcType;
        this.clomunType = clomunType;
        this.clomunChnName = clomunChnName;
    }

    public ModelFiled(String fieldName, String javaType, String getMethod, Object value) {
        this.fieldName = fieldName;
        this.getMethod = getMethod;
        this.javaType = javaType;
        this.value = value;
    }



    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getClomunName() {
        if (clomunName != null) {
            return clomunName;
        }
        return StringUtil.generateColumn(fieldName);
    }
    public void setClomunName(String clomunName) {
        this.clomunName = clomunName;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getClomunType() {
        return clomunType;
    }

    public void setClomunType(String clomunType) {
        this.clomunType = clomunType;
    }

    public String getClomunChnName() {
        return clomunChnName;
    }

    public void setClomunChnName(String clomunChnName) {
        this.clomunChnName = clomunChnName;
    }



    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public Object getValue() {
        return value;
    }

    public String getDecorationValue(String own_sign) throws Exception {

        if (getClomunName().equals("gmt_create")) {
            return "now()";
        }
        if (getClomunName().equals("gmt_modified")) {
            return "now()";
        }
        if (getClomunName().equals("own_sign")) {
            /*if(IntegrateUtil.debug){
                logger.error("！！！注意环境变量设置 ！！！");
            }*/
            return own_sign;
        }
        if (getClomunName().equals("gmt_create_nick") || getClomunName().equals("gmt_modified_nick")) {
            return "\'贤广\'";

        }
        if (getClomunName().equals("greate_nick") || getClomunName().equals("modified_nick")) {
            return "\'贤广\'";

        }

        if(getClomunName().equals("biz_type")){
            //todo
            /*if(IntegrateUtil.redo_BizType != null && !IntegrateUtil.redo_BizType.trim().equals("")){
                return "\'" + IntegrateUtil.redo_BizType + "\'";
            }*/

        }


        //gmt_greate_nick
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof String) {
            //判断是否可转换为map
            /*try {
                Map map = FastJsonTool.deserialize((String) value,Map.class) ;
                return map2str(map);
            }catch (Exception e){*/
            String vv = (String) value;

            if (vv.contains("\'")) {
                vv = vv.replace("\'", "\\\'");
            }

            return "\'" + vv + "\'";
            // }
        }
        if (value instanceof Date) {
            return "\'"+DateUtil.getDateTimeStr((Date) value, "yyyy-MM-dd HH:mm:ss") +"\'";
        }


        return objectToMap(value);


    }

    public void setValue(Object value) {
        this.value = value;
    }


    private static String objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }

        String subValue = JSON.toJSONString(map);
        subValue = subValue.replace("\\\"", "\"");

        if (subValue.contains("\'")) {
            subValue = subValue.replace("\'", "\\\'");
        }

        return subValue;
    }


}
