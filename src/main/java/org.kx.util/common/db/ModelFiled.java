package org.kx.util.common.db;

/**
 * Created by sunkx on 2017/5/17.
 * model属性类型
 */
public class ModelFiled {
    private String  fieldName;
    private String  clomunName;
    private String  javaType;
    private String  jdbcType;
    private String  clomunType;
    private String  clomunChnName;


    public ModelFiled(String fieldName, String clomunName, String javaType, String jdbcType, String clomunType, String clomunChnName) {
        this.fieldName = fieldName;
        this.clomunName = clomunName;
        this.javaType = javaType;
        this.jdbcType = jdbcType;
        this.clomunType = clomunType;
        this.clomunChnName = clomunChnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getClomunName() {
        return clomunName;
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
}
