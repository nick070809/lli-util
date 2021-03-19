package org.kx.util.generate.sql;

import com.alibaba.fastjson.JSON;
import com.xian.model.report.StrategyReport;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/15
 */

public class SqlTest {


    @Test
    public void mockBizInsetSql() throws Exception {
        String originSql = "INSERT INTO `lli_bill_0065` (`id`,`user_id`,`user_nick`,`trade_id`,`status`) VALUES (12323,18065,'18065',220946070218922233,1);";
        Map<String, String> newKvs = new HashMap<>();
        newKvs.put("user_Id", "18087");
        newKvs.put("trade_id", "18089");
        newKvs.put("status", "2");
        InsertSqlGenerateUnit.mockBizSql(originSql, newKvs);
    }


    @Test
    public void getMap() {
        String originSql = "insert into `lli_bill_0065` (`id`,`out_biz_id`,`org_code`,`trade_id`) values (2323,18065,'18065',220946070218922233);";
        SqlGenerateUnits sqlGenerateUnits = SqlGenerateUtil.initSqlGenerateUnits(originSql);
        StrategyReport strategyReport = sqlGenerateUnits.getObject(StrategyReport.class);
        System.out.println(JSON.toJSONString(strategyReport));
    }


    @Test
    public void getEx() {
        String originSql = "delete  from xx_bb where biz_type ='dddf'; \n\n\ninsert into `lli_bill_0065` (`id`,`out_biz_id`,`org_code`,`trade_id`) values (2323,18065,'18065',220946070218922233);";
        SqlGenerateUnits sqlGenerateUnits = SqlGenerateUtil.initSqlGenerateUnits(originSql);
        StrategyReport strategyReport = sqlGenerateUnits.getObject(StrategyReport.class);
        System.out.println(JSON.toJSONString(strategyReport));
    }

}
