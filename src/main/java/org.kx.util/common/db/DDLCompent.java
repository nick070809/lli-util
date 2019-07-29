package org.kx.util.common.db;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by sunkx on 2017/5/17.
 * 数据定义语言(DDL)，例如：CREATE、DROP、ALTER等语句
 * mysql http://www.jb51.net/article/55853.htm
 * 安装mariadb  http://www.cnblogs.com/carlo-jie/p/6104135.html
 */

public class DDLCompent extends DBCompent {

    private MultipleDataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(DDLCompent.class);
    private boolean exsited = false;
     /**
     * 建表
     */
     public boolean createTable(ModelWapper modelWapper, Connection conn, boolean drop) {
         Statement stmt = null;
         String tableName =null;
         List<ModelFiled> modelFileds = null;
         DbType dbType = null;
         try {
             if(conn == null)conn = dataSource.getConnection();
             dbType = modelWapper.getDbType();
             tableName = modelWapper.getTableName();
             modelFileds = modelWapper.getModelFileds();
             //检查表存在
             String exsitSql = null;
             if (dbType == null) {
                 logger.error("not found db type {}", conn.getMetaData().getDriverName());
                 conn.close();
                 return false;
             }

             if (dbType.equals(DbType.MYSQL)) {
                 exsitSql = "select count(1) exsited from information_schema.TABLES where TABLE_NAME = '" + tableName + "' ";

             } else if (dbType.equals(DbType.ORACLE)) {
                 exsitSql = "select count(1) exsited from all_tables where TABLE_NAME = '" + tableName + "' ";

             } else {
                 logger.error("not found db type {}", conn.getMetaData().getDriverName());
                 conn.close();
                 return false;
             }
             if (exsitSql == null) {
                 logger.error("not found general exsitSql {}", conn.getMetaData().getDriverName());
                 conn.close();
                 return false;
             }
             if (modelFileds == null) {
                 logger.error("not found general modelFileds {}", JSON.toJSONString(modelWapper.getClazz()));
                 conn.close();
                 return false;
             }
             stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(exsitSql);
             if (rs.next()) {
                 int xx = rs.getInt(1);
                 if (xx != 0) {
                     logger.error("table {} has exsited !", tableName);
                     if(drop){
                         logger.warn("table {} will be drop !!!", tableName);
                         String dropStr = "drop table "+tableName ;
                         stmt.executeQuery(dropStr);
                     }else{
                         conn.close();
                         exsited = true;
                     }
                     // return false;
                 }
             } else {
                 System.out.println("数据库 用户名或密码错误！");
             }

         } catch (Exception e) {
             logger.error("query table exsit occurs ex {}", e);
             return false;
         }

         StringBuilder sb = new StringBuilder();

         // int doo = 0;
         for (ModelFiled filed : modelFileds) {
           /* if (doo != 0) {
                sb.append(", ").append(filed.getClomunName()).append(" ").append(filed.getClomunType());
                doo++;
            } else {*/
             sb.append(filed.getClomunName()).append(" ").append(filed.getClomunType()).append(", ");
            /*    doo++;
            }*/
         }
         StringBuilder sb2 = new StringBuilder();
         sb2.append("CREATE TABLE ").append(tableName).append(" ( ");
         sb2.append(sb);
         //TODO 主键  primary key (id,name)
         if (dbType.equals(DbType.MYSQL)) {
             sb2.append("  primary key ("+modelWapper.getIdName()+")");

         } else if (dbType.equals(DbType.ORACLE)) {
             sb2.append("  constraint pk_"+tableName+" primary key("+modelWapper.getIdName()+")");

         } else {
             logger.error("not found db type {}");
             return false;
         }
         sb2.append(" )");
         String createSql = sb2.toString() ;
         logger.info("DDL -->" + createSql);
        // if(true) return false;
         //创建表
         try {
             if (conn == null || conn.isClosed())
                 conn = dataSource.getConnection();
             if (stmt == null) stmt = conn.createStatement();
            //
             StringBuilder chineseName = new StringBuilder();
             try{
                 for (ModelFiled filed : modelFileds) {
                     if(filed.getClomunChnName() != null){
                         chineseName.append(" comment on column  ").append(tableName) .append(".").append(filed.getClomunName()).append(" is '").append(filed.getClomunChnName()).append("';");
                         String comment  = chineseName.toString();
                         System.out.println( comment); //自己去执行
                         chineseName.delete( 0, chineseName.length() );
                     }
                 }
             }catch (Exception e){e.printStackTrace();}
             if(exsited) return false;
             stmt.executeUpdate(createSql);
             return true;
         } catch (Exception e) {
             logger.error("create table occurs ex {}", e);
         } finally {
             try {
                 if (stmt != null) stmt.close();
                 if (conn != null || !conn.isClosed())
                     conn.close();
             } catch (SQLException e) {
                 logger.error("create table occurs ex {}", e);
             }
         }
         return false;
     }
    public   boolean createTable(Class model, NameRule nameRule, NameProfix nameProfix, boolean drop) throws SQLException {
        Connection conn  = dataSource.getConnection();
        logger.error("use conn -->"+ conn.getMetaData().getUserName());
        ModelWapper  modelWapper = getModelWapper( getDataBaseType(conn),model,nameRule,nameProfix);
        return createTable(  modelWapper, conn, drop) ;
    }



}

