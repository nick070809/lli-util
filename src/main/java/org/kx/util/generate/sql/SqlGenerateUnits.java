package org.kx.util.generate.sql;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/1/15
 */

public interface SqlGenerateUnits {


    public String getDmlTyle();


    public String getOriginSql();


    public String getTableName();
    public boolean isShareTable();
    public String getBizTableName();



    public <T> T getObject(Class<T> instance);

    public <T> T getObjectHtml(Class<T> instance);




    //  public  String getLowerSql();

}
