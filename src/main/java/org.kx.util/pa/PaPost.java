package org.kx.util.pa;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/20
 */

public class PaPost {

//https://www.cnblogs.com/airsen/p/6135238.html
    //https://blog.csdn.net/qicui2835/article/details/80945969

    public static void main(String args[]) {


        Connection connect = Jsoup.connect("https://*home.htm");
        // 带参数开始
        connect.data("expectStartDate", "20200220");
        connect.data("expectEndDate", "20200220");
        connect.data("devDomain", "*");
        // 带参数结束



        try {

            //先获得的是整个页面的html标签页面
            Document doc = connect.post();
            System.out.println(doc.toString());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}