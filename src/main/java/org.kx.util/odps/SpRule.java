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

    String tablenName = "s_hj_settle_sp_rule";
    String[] colmuns = {"parent_id", "biz_order_id", "execute_id", "biz_type", "settle_fee", "out_account", "execute_status", "available", "seller_id"};


    @Override
    public String getTableName() {
        return this.tablenName;
    }

    @Override
    public String getOriginName() {
        return "huijin.s_hj_execute_rule";
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
        return " ) and biz_type not in (0,100)";
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
