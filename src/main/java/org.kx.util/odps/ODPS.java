package org.kx.util.odps;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.FileUtil;
import org.kx.util.Validator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
        String filePath = "/Users/xianguang/Downloads/down/odps.txt";
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
        FileUtil.writeStringToFile(resultContent.toString(), "/Users/xianguang/Downloads/down/item2.csv");
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


    private  String parseLine(String line){
        if(StringUtils.isBlank(line)){
            return null;
        }
        if(line.startsWith("---")){
            return  null;
        }
        line = line.replace("\u001B[K", "");

        String[] words = line.split(",");
        if(clomunSize == 0){
            clomunSize =words.length;
        }
        if(words.length== 0 || words.length != clomunSize){
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

}
