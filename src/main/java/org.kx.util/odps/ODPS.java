package org.kx.util.odps;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.FileUtil;
import org.kx.util.Validator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/7/7
 */

public class ODPS {

    Integer clomunSize = 0;
    //处理odps的文件
    @Test
    public void readOdpsFile() throws Exception {
        String filePath = "/Users/xianguang/Downloads/down/dapFlag.log"; //odps.txt
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        StringBuilder resultContent = new StringBuilder();

        while (line != null) { // 如果 line 为空说明读完了
            String parsedLine = parseLine(line);
            if(parsedLine != null){
                //parsedLine = filterLine(parsedLine);
                resultContent.append(parsedLine).append("\n");
            }
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        //System.out.println(resultContent.toString());
        FileUtil.writeStringToFile(resultContent.toString(), "/Users/xianguang/Downloads/down/biz_order_ids.csv");
    }


    //处理odps的文件
    @Test
    public void readLogFile() throws Exception {
        String filePath = "/Users/xianguang/Downloads/down/remote.log";
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        StringBuilder resultContent = new StringBuilder();

        while (line != null) { // 如果 line 为空说明读完了
            String parsedLine = parseLine(line);
            if(parsedLine != null){
                resultContent.append(parsedLine).append("\n");
            }
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        FileUtil.writeStringToFile(resultContent.toString(), "/Users/xianguang/Downloads/down/log.txt");
    }


    //筛选出新的
    @Test
    public void filterNewConfigs() throws Exception {
        String newIds = FileUtil.readFile("/Users/xianguang/Downloads/down/od1.txt");
        String oldIds =  FileUtil.readFile("/Users/xianguang/Downloads/down/odx.txt");

        List<String> nIds = new ArrayList<>();

        if (newIds.contains("\n")) {
            newIds = newIds.replace("\n", "");
        }
        // System.out.println(newIds);
        String[] nm = newIds.split(",");
        for (String id : nm) {
            if (nIds.contains(id)) {
                continue;
            }
            if (oldIds.contains(id)) {
                continue;
            }
            nIds.add(id);
        }
        System.out.println(nIds.size());


        //StringBuilder sbt = new StringBuilder();
        StringBuilder sbt1 = new StringBuilder();
        int size = 0;
        for (String id : nIds) {
            if (size >= 10000) {
                break;
            }
            size++;
            sbt1.append(id).append(",");

        }
        FileUtil.writeStringToFile(sbt1.toString(), "/Users/xianguang/Downloads/down/od.txt");

    }

    private  String filterLine(String line){

        String[] words =  line.split(",");
        return   words[1];
    }
    private  String parseLine(String line) throws UnsupportedEncodingException {
        if(StringUtils.isBlank(line)){
            return null;
        }
        if(line.startsWith("---")){
            return  null;
        }
        line = line.replace("\u001B[K", "");

        if(line.contains("code")){
            line = line.replace(",code", "0code");
        }

        List<String> words  = parseChineseLine(line) ;
        if(clomunSize == 0){
            clomunSize =words.size();
        }
        if(words.size()== 0 || words.size() != clomunSize){
            return null;
        }
        StringBuilder sbt = new StringBuilder();
        for(String word :words){

            if(Validator.isNum(word) && word.length() >5){
                word = "\'"+word ;
            }
            if(sbt.length() >0){
                sbt.append(",").append(word);
            }else {
                sbt.append(word);
            }
        }
        return sbt.toString();
    }


    private  List<String> parseChineseLine(String st) throws UnsupportedEncodingException {
        List<String> stt = new ArrayList();
        if(Validator.containsChinese(st)){
            String[] words = st.split(",");
            StringBuilder stringBuilder = null ;
            for(String word :words){
                if(!Validator.containsChinese(word)){
                    if(stringBuilder != null){
                        stt.add(stringBuilder.toString());
                    }
                    stt.add(word);
                    stringBuilder = null;
                }else {
                    if(stringBuilder == null){
                        stringBuilder = new StringBuilder();
                    }
                    stringBuilder.append(word).append("_");
                }
            }
        }else {
          return   Arrays.asList(st.split(",") );
        }
        return  stt;
    }




}
