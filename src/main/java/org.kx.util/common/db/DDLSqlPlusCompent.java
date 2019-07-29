package org.kx.util.common.db;

import com.alibaba.fastjson.JSON;
import org.kx.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by sunkx on 2017/6/27.
 * 使用sqlplus执行建表语句
 */

public class DDLSqlPlusCompent extends DBCompent{
    @Autowired
    private MultipleDataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(DDLCompent.class);
    private boolean exsited = false;
    /**
     * 建表
     */
    public boolean createTable(Class model, NameRule nameRule, NameProfix nameProfix, boolean drop) {
        Statement stmt = null;
        Connection conn = null;
        String tableName =null;
        List<ModelFiled> modelFileds = null;
        DbType dbType = null;
        ModelWapper modelWapper =null;
        try {
            conn = dataSource.getConnection();
            modelWapper = getModelWapper( getDataBaseType(conn),model,nameRule,nameProfix);
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
                logger.error("not found general modelFileds {}", JSON.toJSONString(model));
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

        for (ModelFiled filed : modelFileds) {
            sb.append(filed.getClomunName()).append(" ").append(filed.getClomunType()).append(", ");
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
        if(exsited) return false;
        //创建表
        try {
            if (conn == null || conn.isClosed())
                conn = dataSource.getConnection();
            if (stmt == null) stmt = conn.createStatement();
            stmt.executeUpdate(createSql);
            FileUtil.writeStringToFile("","E:/test.sql");
            StringBuilder chineseName = new StringBuilder();
            try{
                for (ModelFiled filed : modelFileds) {
                    if(filed.getClomunChnName() != null){
                        chineseName.append("\n comment on column  ").append(tableName) .append(".").append(filed.getClomunName()).append(" is '").append(filed.getClomunChnName()).append("';");
                        String comment  = chineseName.toString();
                       // logger.info("comment SQL -->" + comment);
                        FileUtil.appedStringToFile(new String(comment.getBytes(),"UTF-8"),"E:/test.sql");
                        chineseName.delete( 0, chineseName.length() );
                    }
                }
                System.out.println("writed to file");
                // TODO
                new PLSQL().exeSqlplus("cbpaydev", "cbpaydev", "SERVERTAF", "test.sql", "E:/","E://test.log");
                System.out.println("running  the file");
                String dooo = FileUtil.readFile("E://test.log");
                System.out.println(dooo);
            }catch (Exception e){e.printStackTrace();}
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





}
