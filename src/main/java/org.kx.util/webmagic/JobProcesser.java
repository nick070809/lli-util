package org.kx.util.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;

import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/4
 */

public class JobProcesser  implements PageProcessor {


    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);



    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        page.putField("author", page.getUrl().regex("https://blog.csdn\\.net/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//div[@class='push-event']/strong/a/text()").toString());
        System.out.println();
        List<String> contextList=page.getHtml().xpath("h4[@class='text-truncate']/a/text()").all();
        for(String str:contextList){
            System.out.println(str);
        }
        //System.out.println( page.getHtml().regex("h4[@class='text-truncate']/a/text()").all().toString());
        if (page.getResultItems().get("name") == null) {
            //skip this pagetimeline-on date-point
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));


        // 部分三：从页面发现后续的url地址来抓取
        //page.addTargetRequests(page.getHtml().links().regex("(https://blog.csdn\\.net/[\\w\\-]+/[\\w\\-]+)").all());
    }




    public Site getSite() {
        return site;
    }


    public static void main(String[] args) throws InterruptedException {


        Spider.create(new GithubRepoPageProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("https://blog.csdn.net/hanzl1")
                .addPipeline(new JsonFilePipeline("/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/mingwen/e.txt"))
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
        Thread.sleep(13000L);
    }
}
