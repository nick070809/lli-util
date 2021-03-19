package org.kx.util.base.str;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.FileUtil;
import org.kx.util.biz.SettleRequestInfo;
import org.kx.util.config.SysConf;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 富文本工具
 * Created by sunkx on 2017/5/11.
 */
public class RishStringUtil {
    public static String compareSettleClause(String s1, String s2) throws IOException {
        String[] keys = {SysConf.op,SysConf.ef};
        return CompareUtil.compareSettleRequestInfo(CompareUtil.parseClause(StringUtil.toJSONArray(s1),keys), CompareUtil.parseClause(StringUtil.toJSONArray(s2),keys));
    }

    public static String compareBizconf(String s1, String s2) throws IOException {
        String[] keys = {SysConf.mc};
        List<SettleRequestInfo> t1 =CompareUtil.parseClause(parseBizTable(s1, SysConf.bizTitels),keys) ;
        List<SettleRequestInfo> t2 =CompareUtil.parseClause(parseBizTable(s2, SysConf.bizTitels),keys) ;
        return CompareUtil.compareSettleRequestInfo(t1,t2);
    }

    public  static  JSONArray parseBizTable(String content_,String title_){
        String[] columns = content_.split("\t");
        List<String> cl = new ArrayList<>();
        int clSize = 0;
        //换行符号处理
        for(int i =0;i<columns.length;i++){
            if( (columns[i].startsWith("cn\n") ||columns[i].startsWith("CN\n") ) && columns[i].length() >3){
                cl.add( columns[i].substring(0,2)) ;
                String ex =  columns[i].substring(3).trim();
                if(StringUtils.isNotBlank(ex)){
                    cl.add(ex) ;
                    clSize ++ ;
                }
            }else {
                cl.add(columns[i]) ;

            }
            clSize ++ ;
        }
        String[] titles = title_.split("\t");
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        array.add(jsonObject);
        /*System.out.println("titles.length "+titles.length);
        System.out.println("clSize "+clSize);*/
       /* for(int i =110;i<clSize;i++){
            System.out.println(cl.get(i));
        }
        System.out.println("=========");*/
        int t = 0;
        for(int i =0;i<clSize;i++){
            if(t == titles.length && clSize>titles.length ){
                t = 0;
                jsonObject = new JSONObject();
                array.add(jsonObject);
            }

            String title =  titles[t];
            t ++ ;
            if( ! ("id".equals(title)  || "gmt_create".equals(title)||
                    "gmt_modified".equals(title)|| "gmt_greate_nick".equals(title)|| "gmt_modified_nick".equals(title)
                    || "country".equals(title)|| "end_time".equals(title)|| "start_time".equals(title)
                    || "extend_info".equals(title) || "db_remark".equals(title)|| "biz_config_id".equals(title)
                    || "module_desc".equals(title)|| "own_sign".equals(title)
            )){
                jsonObject.put(title,cl.get(i));
            }


        }
       // System.out.println(array.toJSONString());
        return array ;

    }



    /**
     * 将文本文件中的内容读入到buffer中
     */
    public static void readToBuffer(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        StringBuilder sbt = new StringBuilder() ;
        while (line != null) { // 如果 line 为空说明读完了
            String word = line ;
            if(StringUtils.isBlank(word)){
                line = reader.readLine(); // 读取下一行
                continue;
            }
            sbt.append(line).append(",");
            line = reader.readLine(); // 读取下一行
            if(sbt.length() >= 4000){
                String s = "curl '="+sbt.toString()+"'" ;
                FileUtil.appedStringToFile(s+"\n\n","/Users/xianguang/IdeaProjects/nick070809/lli-util/xd.txt");
                sbt = new StringBuilder() ;
            }
        }
        String s = "curl '="+sbt.toString()+"'" ;
        FileUtil.appedStringToFile(s+"\n\n","/Users/xianguang/IdeaProjects/nick070809/lli-util/xd.txt");


        reader.close();
        is.close();
    }



    @Test
    public void testCase() throws IOException {
        readToBuffer("/Users/xianguang/IdeaProjects/nick070809/lli-util/xds.txt");
    }
}
