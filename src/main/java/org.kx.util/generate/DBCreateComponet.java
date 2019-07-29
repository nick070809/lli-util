package org.kx.util.generate;

import com.xian.model.report.StrategyReport;
import org.junit.Test;
import org.kx.util.base.CanNotRun;
import org.kx.util.common.db.CamelNameRule;
import org.kx.util.common.db.DDLCompent;
import org.kx.util.common.db.MakeDMLCompent;
import org.kx.util.common.db.MultipleDataSource;
import org.kx.util.common.db.NameProfix;

import java.sql.SQLException;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/29
 */

public class DBCreateComponet {


     MakeDMLCompent makeDMLCompent = new MakeDMLCompent();
     MultipleDataSource dataSource;
     DDLCompent ddlCompent;

    @Test
    public void createModelService()  {
        try {
            makeDMLCompent.analy(StrategyReport.class,"src/test/java","dal",new CamelNameRule(),new NameProfix("t_",null))
                .makeMapperFile()
                .makeXmlFile()
                .makeControllerFile()
                .makeIServiceFile()
                .makeServiceImplFile()
            ;

        } catch (Exception e) {
            e.printStackTrace();
        }
     }


    @Test
    @CanNotRun
    public void createModelTable() throws SQLException {
        dataSource.setDataSourceKey("dataSource0");
        boolean created1 = ddlCompent.createTable(StrategyReport.class,new CamelNameRule(),new NameProfix("cb_",null),true);
    }
}
