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

public class WyyAlbum {
    public static void main(String[] args) {

        getAblum();
    }

    public static void getAblum() {

        try {
            String singgers = FileUtil.readFile(MUSICCONTANST.Singger_Path);
            String[] uids = singgers.split(",");
            StringBuilder ablums = new StringBuilder();
            for(String uid : uids){
                String ablum =  getData(uid);
                if(ablum != null){
                    ablums.append(ablum);
                }
            }
            FileUtil.writeStringToFile(ablums.toString(),MUSICCONTANST.Ablum_Path);

            //Arrays.asList(uids).stream().forEach(e ->getData(e));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getData(String uid) {
        String url =  "https://music.163.com/artist/album?id="+uid+"&limit=150&offset=0";

        //System.out.println(url);

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .header("Cookie",  MUSICCONTANST.Cookie)
                    .header("Referer", "https://music.163.com/discover/artist/cat?id=1001&initial=65")
                    .header("Upgrade-Insecure-Requests", "1")
                    .method(Connection.Method.GET)
                    .timeout(200000).get();

            //String singer = doc.title().replace(" - 网易云音乐", "");

            Elements names = doc.select("#m-song-module li a.s-fc0");
            StringBuilder ablum = new StringBuilder();
            for (Element element : names) {

               /* String mess = "{\"singer\":" + "\"" + singer + "\"," +
                        "\"albumName\":" + "\"" + element.text() + "\"," +
                        "\"albumId\":" + "\"" + element.attr("href").
                        replace("/album?id=", "").trim() + "\"}";*/
               // System.out.println(mess);
               // songNames.append(element.text()).append(",");
                ablum.append(element.attr("href").replace("/album?id=", "").trim()).append(",");

                //FileUtils.saveConToFile(mess, "g://singerAlbum.json"); // 自行写存储信息的代码
            }
            return  ablum.toString();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return  null;

    }

}
