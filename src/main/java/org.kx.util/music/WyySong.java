package org.kx.util.music;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/20
 */


import com.alibaba.fastjson.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.kx.util.FileUtil;

/**
 * describe:根据专辑Id获取专辑对应歌曲所有信息
 *
 * @author wfd
 * @date 2019/08/27
 */
public class WyySong {

    public static void main(String[] args) {
        getSong();
    }

    public static void getSong() {
        try {

            String singgers = FileUtil.readFile(MUSICCONTANST.Ablum_Path);
            String[] ablums = singgers.split(",");
             StringBuilder songs = new StringBuilder();
            for(String ablum : ablums){
                String songName =  getData(ablum);
                if(ablum != null){
                    songs.append(songName);
                }
            }
            FileUtil.writeStringToFile(songs.toString(),MUSICCONTANST.Song_Path);
           // Arrays.asList(uids).stream().forEach(e ->getData(e));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getData(String albumId) {

        String url = "https://music.163.com/album?id=" + albumId;
       // System.out.println(url);

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .header("Cookie", MUSICCONTANST.Cookie)
                    .header("Referer", "https://music.163.com/discover/artist/cat?id=1001&initial=65")
                    .header("Upgrade-Insecure-Requests", "1")
                    .method(Connection.Method.GET)
                    .timeout(200000).get();

            Elements names = doc.select("#song-list-pre-data"); // 歌名
            Elements singer = doc.select(".intr:eq(1) span a"); // 歌手
            Elements album = doc.select(".tit .f-ff2"); // 专辑名


            JSONArray jsonArray = JSONArray.parseArray(names.text());
            StringBuilder song = new StringBuilder();

            for (int i = 0; i < jsonArray.size(); i++) {
                String songName = jsonArray.getJSONObject(i).getString("name").replace("\"", "").replace("\\", "");
                String songId = jsonArray.getJSONObject(i).getString("id");
                String songStatus = jsonArray.getJSONObject(i).getString("status");
                String mess = "{" +
                        "\"songName\":\"" + songName + "\"," +
                        "\"songId\":\"" + songId + "\"," +
                        "\"songStatus\":\"" + songStatus + "\"," +
                        "\"singer\":\"" + singer.text() + "\"," +
                        "\"singerId\":\"" + singer.attr("href").
                        replace("/artist?id=", "") + "\"," +
                        "\"album\":\"" + album.text().replace("\"", "").replace("\\", "") + "\"," +
                        "\"albumId\":\"" + albumId + "\"" +
                        "}";
                System.out.println(mess);
               // FileUtil.writeStringToFile(mess.toString(),MUSICCONTANST.Song_Path);
                song.append(songName).append(",");

            }
            return song.toString();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;

    }

}