package org.kx.util.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.kx.util.FileUtil;
import org.kx.util.Validator;
import org.kx.util.db.FileDb;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/6/15
 */

public class MyDict {

    static HashMap<Character, Integer> kindex = new HashMap<>();
    static HashMap<String, String> vindex = new HashMap<>();

    static String  dictName ="dictM.txt";

    static String filepath = System.getProperty("user.home") + File.separator + "filedb" + File.separator + dictName;
    static File file = new File(filepath);


    public static   Integer getIndex(Character character){
        Integer index = kindex.get(character);
        if(index == null){
            return  0;
        }
        return index;
    }


    public static   Integer getNextIndex(Character character){
        Integer index = kindex.get(character);
        if(index == null){
            return  0;
        }
        Integer newIndex =  index+1;
        kindex.put(character ,newIndex);
        return newIndex;
    }

    public static Character getKey(String word){
        if(Validator.isNum(word)){
            return  null;
        }
        return  word.charAt(0);

    }
    public static   Integer getExsitIndex(String word){
        return Integer.valueOf(word.substring(1));
    }



    public  static  String putIndexNotPersistence(String word) throws IOException {
        return putIndex(word,false);
    }


    public  static  String getIndex(String word) throws IOException {
         return  FileDb.getInstance().read(dictName, word);
    }


    public  static  String putIndex(String word, boolean persistence) throws IOException {
        if(Validator.isNum(word)){
            return  null;
        }
        String index =  FileDb.getInstance().read(dictName, word);
        if(index != null){
            return  index ;
        }
        Character character =  word.charAt(0);
        Integer exsitIndex = kindex.get(character);
        if(exsitIndex == null){
            String idex = character+""+0 ;
            kindex.put(character,0);
            FileDb.getInstance().insert(dictName, word,idex,false);
            return idex;
        }
        String idex = character+""+(exsitIndex +1) ;
        kindex.put(character, exsitIndex +1);
        FileDb.getInstance().insert(dictName,word, idex,false);
        if(persistence){
            JSONObject jsonObject = FileDb.getInstance().read2Json(file, true);
            FileUtil.writeStringToFile(JSON.toJSONString(jsonObject), file.getAbsolutePath());
        }
        return idex;
    }



    public  static  String getWordByIndex(String index){
        return vindex.get(index);
    }


    public  static  void putIndex(Character character,Integer index){
        Integer exsitIndex = kindex.get(character);
        if(exsitIndex == null){
            kindex.put(character,index);
            return;
        }
        if(exsitIndex <  index){
            kindex.put(character,index);
            return;
        }
    }

    static{ //加载
        FileDb.getInstance().createIfNotexsist(file);
        try {
            JSONObject jsonObject = FileDb.getInstance().read2Json(file, true);
            Iterator<Map.Entry<String, Object>> entries = jsonObject.entrySet().iterator();
            int size = 0;
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                Character key = getKey((String) entry.getValue());
                if(key == null){
                    continue;
                }
                Integer index = getExsitIndex((String) entry.getValue());
                putIndex(key,index);
                vindex.put(entry.getValue().toString(),entry.getKey());
                size++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  static void  upgradeDict() throws IOException {
        JSONObject jsonObject = FileDb.getInstance().read2Json(file, true);
        FileUtil.writeStringToFile(JSON.toJSONString(jsonObject), file.getAbsolutePath());
    }

    public static void main(String[] args) throws IOException {
        String name ="SUNKAIXIANG";
        MyDict.putIndex(name,true);
        System.out.println(getIndex(name.charAt(0)));

    }

    //构建词典
    @Test
    public void generateDict() throws Exception {
        String filePath = "/Users/xianguang/filedb/wordCount.csv";
        File file = new File(filePath);
        InputStream is = new FileInputStream(file);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        BufferedReader reader = new BufferedReader(ireader);

        HashMap<String, Integer> readResult = new HashMap<>();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            String[] kv = line.split(",");
            if (kv.length == 2) {
                String key  = kv[0];
                if(Validator.isNum(key)){
                    continue;
                }
               // MyDict.putIndex(kv[0]);
                Integer count = Integer.parseInt(kv[1]);
                int ecount = readResult.get(kv[0])== null?0: readResult.get(kv[0]);
                if(count != null && count> ecount){
                    readResult.put(kv[0],count);
                }else {
                    readResult.put(kv[0],1);
                }
            }
        }
        reader.close();
        is.close();

        LinkedHashMap<String, Integer> result = readResult.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));




        Iterator<Map.Entry<String, Integer>> entries = result.entrySet().iterator();

       /* String pretty = JSON.toJSONString(entries, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);


        System.out.println(pretty);*/
        int size = 0;
        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            MyDict.putIndexNotPersistence(entry.getKey());
            size++;
        }
        System.out.println("MyDict size : " + size);

        JSONObject jsonObject = FileDb.getInstance().read2Json(MyDict.file, false);
        FileUtil.writeStringToFile(JSON.toJSONString(jsonObject), MyDict.filepath);
    }

}
