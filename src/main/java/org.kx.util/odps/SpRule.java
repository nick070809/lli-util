package org.kx.util.odps;

import org.junit.Test;
import org.kx.util.DateUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/7/29
 */

public class SpRule extends   OdpsTable {

    String tablenName = "*";
    String[] colmuns = {"*"*", "*", "*", "*", "*", "*", "*", "*"};


    @Override
    public String getTableName() {
        return this.tablenName;
    }

    @Override
    public String getOriginName() {
        return "*";
    }

    @Override
    public String getDs() {
        return " pt =  'history'";
    }

    @Override
    public String getCreateSql() {
        return super.getCreateSql();
    }

    @Override
    public String[] getClomuns() {
        return this.colmuns;
    }

    @Override
    public  int getQuerySize(){
        return 10;
    }

    //获取分区前语句
    public   String getPartitionPreSql() throws IOException{
        return super.getPartitionPreSql();
    }

    //获取分区后语句
    public   String getPartitionSuffixSql() throws IOException {
        return " ) and * not in (0,100)";
    }

    @Override
    public List<String> getPartition() throws ParseException {
        return DateUtil.getMonthReduces(DateUtil.getDate("20200401", "yyyyMMdd"), -1, 5 * 12);
    }


    @Test
    public void witreCreateAndCleanSql() throws Exception {
        super.witreCreateAndCleanSql();
    }
}
