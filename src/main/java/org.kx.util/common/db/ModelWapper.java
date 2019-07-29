package org.kx.util.common.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.kx.util.common.db.ClomunDataType.*;

/**
 * Created by sunkx on 2017/5/17.
 * model包装类
 */
public class ModelWapper {
    private String idName;  //主键名称
    private String second;  //非主键属性名称

    private String tableName;
    private List<ModelFiled> modelFileds;

    private Class clazz;
    private NameRule nameRule;
    private NameProfix profix;
    private DbType dbType;

    public  String filedsToStirng(){
        StringBuffer sbf = new StringBuffer();
        for(ModelFiled f:modelFileds){
            sbf.append(f.getClomunName()+",");
        }
        String str = sbf.toString();
        return str.endsWith(",")?str.substring(0, str.length()-1):str;
    }



    public ModelWapper(Class clazz, NameRule nameRule, NameProfix profix, DbType dbType) {
        this.clazz = clazz;
        this.nameRule = nameRule;
        this.profix = profix;
        this.dbType = dbType;
    }

    public String getTableName() {
        if (this.tableName == null) {
            String clazzName = clazz.getSimpleName();
            if (clazzName.endsWith("DTO")) clazzName = clazzName.substring(0, clazzName.length() - 3);
            if (clazzName.endsWith("TO")) clazzName = clazzName.substring(0, clazzName.length() - 2);
            if (clazzName.endsWith("QO")) clazzName = clazzName.substring(0, clazzName.length() - 3);
            if (clazzName.endsWith("POJO")) clazzName = clazzName.substring(0, clazzName.length() - 4);
            if (clazzName.endsWith("VO")) clazzName = clazzName.substring(0, clazzName.length() - 2);
            if (profix.getTableProfix() != null) {
                this.tableName = (profix.getTableProfix() + nameRule.getName(clazzName)).toLowerCase();
            } else {
                this.tableName = nameRule.getName(clazzName).toLowerCase();
            }
        }
        return this.tableName;
    }

    public List<ModelFiled> getModelFileds() {
        if (this.modelFileds == null) {
            Field[] field = this.clazz.getDeclaredFields();
            modelFileds = new ArrayList<>();
            for (Field f : field) {
                if (!f.getName().equals("serialVersionUID")) {
                    ModelFiled fileddd = getModelFiled(dbType, f);
                    this.modelFileds.add(fileddd);
                }
            }
        }
        return this.modelFileds;
    }

    public String getIdName() {
        if (this.idName == null) {
            getModelFileds();
        }
        return idName;
    }

    private ModelFiled getModelFiled(DbType dbType, Field f) {
        String jdbcType = null;
        String clomunType = null;
        String clomunName = null;
        String fieldType = f.getGenericType().toString();
        fieldType = fieldType.contains(".") ? fieldType.substring(fieldType.lastIndexOf('.') + 1) : fieldType;
        String sfieldType = fieldType.toUpperCase();
        switch (sfieldType) {
            case DOUBLEStr:
                jdbcType = DOUBLEStr;
                if (dbType.equals(DbType.MYSQL)) {
                    clomunType = DoubleSTR + "(17,2)";
                } else if (dbType.equals(DbType.ORACLE)) {
                    clomunType = NUMBERSTR + "(17,2)";
                } else if (dbType.equals(DbType.SQLSERVER)) {
                    //TODO
                } else {
                    //TODO
                }
                break;
            case FLOATStr:
                jdbcType = FLOATStr;
                if (dbType.equals(DbType.MYSQL)) {
                    clomunType = DoubleSTR + "(8,2)";
                } else if (dbType.equals(DbType.ORACLE)) {
                    clomunType = NUMBERSTR + "(8,2)";
                } else if (dbType.equals(DbType.SQLSERVER)) {
                    //TODO
                } else {
                    //TODO
                }
                break;
            case "DATE":
                jdbcType = TIMESTAMPStr;
                if (dbType.equals(DbType.MYSQL)) {
                    clomunType = DateTimeStr;
                } else if (dbType.equals(DbType.ORACLE)) {
                    clomunType = DATEStr;
                } else if (dbType.equals(DbType.SQLSERVER)) {
                    //TODO
                } else {
                    //TODO
                }
                break;

            case "STRING":
                jdbcType = VARCHARStr;
                if (dbType.equals(DbType.MYSQL)) {
                    clomunType = TextStr;
                } else if (dbType.equals(DbType.ORACLE)) {
                    clomunType = VARCHAR2Str;
                } else if (dbType.equals(DbType.SQLSERVER)) {
                    //TODO
                } else {
                    //TODO
                }
                break;
            case "BIGDECIMAL":
                jdbcType = DECIMALStr;
                if (dbType.equals(DbType.MYSQL)) {
                    clomunType = DecimalStr + "(25,2)";
                } else if (dbType.equals(DbType.ORACLE)) {
                    clomunType = NUMBERSTR + "(25,2)";
                } else if (dbType.equals(DbType.SQLSERVER)) {
                    //TODO
                } else {
                    //TODO
                }
                break;
            case "CLOB":
                jdbcType = VARCHARStr;
                clomunType = NUMBERSTR + "(25)";
                if (dbType.equals(DbType.MYSQL)) {
                    clomunType = LongTextStr;
                } else if (dbType.equals(DbType.ORACLE)) {
                    clomunType = ClobStr;
                } else if (dbType.equals(DbType.SQLSERVER)) {
                    //TODO
                } else {
                    //TODO
                }
                break;
            default:
                jdbcType = INTEGERStr;

                if (dbType.equals(DbType.MYSQL)) {
                    clomunType = BigintSTR;
                } else if (dbType.equals(DbType.ORACLE)) {
                    clomunType = NUMBERSTR + "(19)";
                } else if (dbType.equals(DbType.SQLSERVER)) {
                    //TODO
                } else {
                    //TODO
                }
                break;
        }
        if (profix.getFiledProfix() != null) {
            clomunName = profix.getFiledProfix() + nameRule.getName(f.getName()).toLowerCase();
        } else {
            clomunName = nameRule.getName(f.getName()).toLowerCase();
        }
        if (this.idName == null) {
            //this.idName = clomunName;
            this.idName = f.getName();
        }else  if(second == null){
            this.second = f.getName();
        }

         ChineseName chineseName = f.getAnnotation(ChineseName.class);
        if(chineseName != null){
            //System.out.println(chineseName);
            return new ModelFiled(f.getName(), clomunName, fieldType, jdbcType, clomunType, chineseName.value());
        }
        return new ModelFiled(f.getName(), clomunName, fieldType, jdbcType, clomunType,null);
    }

    public DbType getDbType() {
        return dbType;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class getClazz() {
        return clazz;
    }

    public NameRule getNameRule() {
        return nameRule;
    }

    public NameProfix getProfix() {
        return profix;
    }

    public String getSecond() {
        return second;
    }
}
