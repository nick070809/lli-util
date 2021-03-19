package org.kx.util.biz;

import nick.doc.MdCtrl;
import org.kx.util.FileUtil;
import org.kx.util.generate.HtmlGenerate;

import java.math.BigDecimal;
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


    @org.junit.Test
    public  void htmlTest() throws Exception {
        HtmlGenerate htmlGenerate = new HtmlGenerate();
        String html = getHtml(MdCtrl.getMdCtrl().getDocList());

        FileUtil.writeStringToFile(html,"/Users/xianguang/IdeaProjects/nick070809/lli-util/temp/teml.html");

    }


    @org.junit.Test
    public  void html2Test() throws Exception {
        MdCtrl.getMdCtrl().loadself();
    }



    public  String getHtml(String content){
        return  "<html>\n" +
                "    <head>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/> " +
                " </head>\n" +
                "    <body>\n"+content+
                "   </body>\n" +
                "</html>" ;
    }


    @org.junit.Test
    public void  sse() {
        BigDecimal a = new BigDecimal(2);
        BigDecimal b = new BigDecimal(3);
        BigDecimal index = a.multiply(new BigDecimal(100)) .divide(b,0,BigDecimal.ROUND_HALF_UP);


        //a.divide(b, 2, BigDecimal.ROUND_HALF_UP);

        System.out.println(index.longValue());
    }


}
