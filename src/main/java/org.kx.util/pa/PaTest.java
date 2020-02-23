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

public class PaTest {

    public static void main(String args[]){

        //这个就是博客中的java反射的url
        final String url=  "https://www.cnblogs.com/qdhxhz/p/9230805.html";

        try {
            //先获得的是整个页面的html标签页面
            Document doc = Jsoup.connect(url).get();

            //获取正文标题，因为整片文章只有标题是用h1标签
            Elements btEl = doc.select("H1");
            String  bt=btEl.text();
            System.out.println("========正文标题======：");
            System.out.println(bt);

            //获取二级标题
            Elements ejbtEls = doc.select("H2");
            //因为整片文章有多个二级标题所以进行拼接
            StringBuilder  ejbts=new  StringBuilder();
            for(Element el :ejbtEls) {
                ejbts.append(el.text());
                ejbts.append("\n");
            }
            String ejbt=ejbts.toString();
            System.out.println("=======二级标题=========：");
            System.out.println(ejbt);

            //获取时间
            Elements timeEl = doc.select("#post-date");
            String  time=timeEl.text();
            System.out.println("========发布时间=========：");
            System.out.println("发布时间：" + time);

            //获取阅读数量
            Elements readEl = doc.select("#post_view_count");
            String  read=readEl.text();
            System.out.println("========阅读数量=========：");
            System.out.println("阅读数量：" + read);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
