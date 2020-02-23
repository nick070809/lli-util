package org.kx.util.pa;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/20
 */

public class Pa161 {
    public static void main(String args[]) {

        //这个就是博客中的java反射的url
        final String url = "http://*.html";

        try {

            //先获得的是整个页面的html标签页面
            Document doc = Jsoup.connect(url).get();

            //获取正文标题，因为整片文章只有标题是用h1标签
            Elements btEl = doc.select("title");
            String bt = btEl.text();
            System.out.println("========正文标题======：");
            System.out.println(bt);

            //获取二级标题
            Elements ejbtEls = doc.select("div").select(".center_blank");
            String bt2 = ejbtEls.text();
             System.out.println(bt2);

            //因为整片文章有多个二级标题所以进行拼接
            StringBuilder ejbts = new StringBuilder();
            for (Element el : ejbtEls) {
                ejbts.append(el.text());
                ejbts.append("\n");
            }
            String ejbt = ejbts.toString();
            System.out.println("=======二级标题=========：");
            System.out.println(ejbt);

            //获取时间
            Elements timeEl = doc.select("foot_");
            String time = timeEl.text();
            System.out.println("========发布时间=========：");
            System.out.println("发布时间：" + time);

            //获取阅读数量


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}