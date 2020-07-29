package org.kx.util.odps;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.DateUtil;
import org.kx.util.FileUtil;
import org.kx.util.generate.IdGenerate;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/7/29
 */

public abstract class OdpsTable {

    public abstract String getTableName();

    public abstract String getOriginName();

    public abstract String getDs();

    public  String getDaysName(){
        return   "days" ;
    }


    public abstract String[] getClomuns();

    //获取分区
    public  abstract List<String> getPartition() throws ParseException;


    //获取分区每次查询个数
    public  int getQuerySize() {
        return  5;
    };


    //获取分区前语句
    public   String getPartitionPreSql() throws IOException {

        String[] colmuns = getClomuns();
        StringBuilder colmunsBuilder = new StringBuilder();
        for (String strings : colmuns) {
            if (colmunsBuilder.length() == 0) {
                colmunsBuilder.append(strings);
            } else {
                colmunsBuilder.append(",").append(strings);
            }
        }

        String ss= "INSERT OVERWRITE TABLE " + getTableName() + " PARTITION(ds='"+IdGenerate.getId() + "')\n" +
                "SELECT  "+colmunsBuilder.toString()+" " +
                " FROM    "+getOriginName()+"\n" +
                " WHERE ";

        if(StringUtils.isBlank(getDs())){
            return ss +   getDaysName()+" in( ";
        }

        return    ss + getDs()+" and "+ getDaysName() +" in( ";

    }


    //获取分区后语句
    public   String getPartitionSuffixSql() throws IOException {

        return " )";

    }




    public String getCreateSql() {
        String tablenName = getTableName();
        String[] colmuns = getClomuns();
        StringBuilder colmunsBuilder = new StringBuilder();
        for (String strings : colmuns) {
            if (colmunsBuilder.length() == 0) {
                colmunsBuilder.append(strings).append(" string ");
            } else {
                colmunsBuilder.append(",\n").append(strings).append(" string ");
            }
        }
        return "create table if not exists " + tablenName + "\n" +
                "(\n" + colmunsBuilder.toString() +
                "\n) partitioned by (ds STRING)\n" +
                "LIFECYCLE  365 ;";

    }



    public  List<String> getCleanSqls() throws ParseException, IOException {
        List<String> dates =  getPartition();
        List<String> cleanSqls = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        int perSize = getQuerySize();
        int currentSize = 0;


        for (String date : dates) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append(date);
             } else {
                stringBuilder.append(",").append(date);
            }

            if ((currentSize + 1) % perSize == 0) {
                cleanSqls.add(getPartitionPreSql() + " " + stringBuilder.toString() +" " +  getPartitionSuffixSql() + " ;");
                stringBuilder = new StringBuilder();
            }
            currentSize++;
        }

        System.out.println("all size :" + currentSize);
        return  cleanSqls ;
    }


    public   void witreCreateAndCleanSql()  throws Exception {
        String createSql = getCreateSql();
        String filePath = "/Users/xianguang/IdeaProjects/nick070809/lli-util/temp/odps_" + DateUtil.getDateTimeStr(new Date(), "yyyyMMddHHmm") + ".txt";
        FileUtil.writeStringToFile(createSql, filePath);
        getCleanSqls().stream().forEach(e-> {
            try {
                FileUtil.appedStringToFile("\n"+e,filePath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

}
