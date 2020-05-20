package org.kx.util.music;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kx.util.FileUtil;

import java.io.IOException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/20
 */

public class WyySinger {
    public static void main(String[] args) {
        getSinger();
    }

    public static void getSinger() {
        //https://music.163.com/#/discover/artist/cat?id=1001&initial=-1 ,热门男歌手
        String url = "https://music.163.com/#/discover/artist/cat?id=1001&initial=-1";
        getData(url);

    }

    public static void getData(String url) {
        System.out.println(url);

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")

                    .header("Cookie", MUSICCONTANST.Cookie)
                    .header("Referer", "https://music.163.com/")
                    .header("Upgrade-Insecure-Requests", "1")
                    .method(Connection.Method.GET)
                    .timeout(200000).get(); // 设置请求头等信息，模拟人工访问，超时时间可自行设置

            //System.out.println(doc);
            Elements names = doc.select("#m-artist-box li a.s-fc0");

            StringBuilder ids = new StringBuilder();
            for (Element element : names) {

               /* String mess = "{\"name\":" + "\"" + element.text() + "\"," +
                        "\"uid\":" + "\"" + element.attr("href").
                        replace("/artist?id=", "").trim() + "\"}";
                System.out.println(mess);*/
                ids.append(element.attr("href").replace("/artist?id=", "").trim()).append(",");
                //FileUtil.appedStringToFile(mess,MUSICCONTANST.Singger_Path);
            }
            FileUtil.writeStringToFile(ids.toString(), MUSICCONTANST.Singger_Path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
