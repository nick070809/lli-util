package org.kx.util.generate;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.DateUtil;
import org.kx.util.ddl.IDBSQLFormat;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/15
 */

public abstract class SqlGenerateUnit {
    String sqlu =null;
    String sql  =null;
    Object dataObject = null;

    /**
     * 检测是否有中文空格
     */
    public  static void checkChineseBlank(String line){
        if(line.contains(IDBSQLFormat.CN_)){
            line = line.replace(IDBSQLFormat.CN_," ["+IDBSQLFormat.CN_+"] ");
            throw new RuntimeException("bad sql contains chinese blank ; sql is " + line);
        }
    }




    /**
     * 将map集合中的数据转化为指定对象的同名属性中
     */
    public static <T> T map2Bean(Map<String, String> source, Class<T> instance) {
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
                        if(source.get(field.getName()) != null){
                            String v = source.get(field.getName()) ;
                            if(StringUtils.isBlank(v) || "null".equals(v)){
                                continue;
                            }
                            if(field.getType().getName().equals("java.lang.Byte")){
                                Byte b = new Byte(v);
                                field.set(object, b);
                            }else if(field.getType().getName().equals("java.lang.Integer")){
                                field.set(object, Integer.parseInt(v));
                            }else if(field.getType().getName().equals("java.lang.Double")){
                                field.set(object, Double.parseDouble(v));
                            }else if(field.getType().getName().equals("java.lang.Long")){
                                field.set(object, Long.parseLong(v));
                            } else if(field.getType().getName().equals("java.util.Date")){
                                if("now()".equals(v)){
                                    Date date = new Date();
                                    field.set(object, date);
                                }else {
                                    Date date = DateUtil.getDate(v,"yyyy-MM-dd HH:mm:ss");
                                    field.set(object, date);
                                }

                            } else if(field.getType().getName().equals("java.lang.Boolean")){
                                if("1".equals(v)){
                                    field.set(object, true);
                                    continue;
                                }else if("0".equals(v)){
                                    field.set(object, false);
                                    continue;
                                }
                                Boolean b = Boolean.valueOf(v);
                                field.set(object, b);
                            } else if(field.getType().getName().equals("java.math.BigDecimal")){
                                BigDecimal bigDecimal = new BigDecimal(v) ;
                                field.set(object, bigDecimal);
                            } else {
                                field.set(object, v);
                            }
                        }
                    }
                }catch (Exception x){
                    throw new RuntimeException("field.name " +field.getName() ,x);
                }
            }
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
