package org.kx.util.biz;

import org.kx.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/3
 */

public class Test {


    @org.junit.Test
    public  void test() throws Exception {

        Seller seller0 = new Seller(12,"tee","good");
        Seller seller1 = new Seller(13,"gee","bad");
        List<Seller> sellerList = new ArrayList<Seller>(){{
            add(seller0);
            add(seller1);
        }};

        IntegrateUtilEnhance integrateUtil =  new IntegrateUtilEnhance();


        String html = integrateUtil.getTableClomun(sellerList,new ArrayList<String>(){{
            add("age");
            add("name");
            add("home_addr");
        }},null) ;

        FileUtil.writeStringToFile(html,"/Users/xianguang/IdeaProjects/nick070809/lli-util/temp/teml.html");


    }
}
