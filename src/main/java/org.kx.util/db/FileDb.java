package org.kx.util.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.kx.util.DateUtil;
import org.kx.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/25
 */

public class FileDb {
    private JSONObject jsonObjectCache;


    private FileDb() {
    }

    private static class SingleTonHoler {
        private static FileDb INSTANCE = new FileDb();
    }

    public static FileDb getInstance() {
        return SingleTonHoler.INSTANCE;
    }


    public int insert(String fileName, String key, String value) throws IOException {

        return insert(fileName, key, value, true);
    }

    public int insert(String fileName, String key, String value, boolean persistence) throws IOException {

        String filepath = System.getProperty("user.home") + File.separator + "filedb" + File.separator + fileName;
        File file = new File(filepath);
        createIfNotexsist(file);
        write(file, key, value, persistence);
        backup(fileName, key, value, "insert");
        return 1;
    }

    public int insertWithoutBackUp(String fileName, String key, String value) throws IOException {
        String filepath = System.getProperty("user.home") + File.separator + "filedb" + File.separator + fileName;
        File file = new File(filepath);
        createIfNotexsist(file);
        write(file, key, value, true);
        return 1;
    }


    public int batchInsert(String fileName, HashMap<String, String> kv) throws IOException {

        String filepath = System.getProperty("user.home") + File.separator + "filedb" + File.separator + fileName;
        File file = new File(filepath);
        createIfNotexsist(file);

        Iterator<Map.Entry<String, String>> entries = kv.entrySet().iterator();
        int size = 0;
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            write(file, entry.getKey(), entry.getValue(), false);
            size++;
        }

        JSONObject jsonObject = read2Json(file, false);

        FileUtil.writeStringToFile(JSON.toJSONString(jsonObject), file.getAbsolutePath());


        backup(fileName, "k", "v", "bacthc insert");
        return size;
    }


    public String read(String fileName, String key) throws IOException {

        String filepath = System.getProperty("user.home") + File.separator + "filedb" + File.separator + fileName;
        File file = new File(filepath);
        JSONObject fileContent = read2Json(file, true);
        String value = read(fileContent, key);
        return value;
    }


    public JSONObject readAll(String fileName) throws IOException {

        String filepath = System.getProperty("user.home") + File.separator + "filedb" + File.separator + fileName;
        File file = new File(filepath);
        return read2Json(file, true);
    }


    public int remove(String fileName, String key) throws IOException {

        String filepath = System.getProperty("user.home") + File.separator + "filedb" + File.separator + fileName;
        File file = new File(filepath);
        delete(file, key, true);
        backup(fileName, key, "", "delete");
        return 1;
    }

    public int deleteAll(String fileName) throws IOException {
        String filepath = System.getProperty("user.home") + File.separator + "filedb" + File.separator + fileName;
        File file = new File(filepath);
        jsonObjectCache.remove(file.getName());
        file.delete();
        backup(fileName, "ALL", "ALL", "deleteFile");
        return 1;
    }

    public void backup(String fileName, String key, String value, String action) {
        //1、可以写个日志快照 ,便于db恢复
        //2、可以同步到其他机器
        //3、可以发个 msg

        System.out.println(DateUtil.getDateTimeStr(new Date(), "yyyy-MM-dd HH:mm:ss SSS") + "|" + action + "|" + fileName + "|" + key + "|" + value);

    }


    public void createIfNotexsist(File file) {
        //创建文件
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String read(JSONObject jsonObject, String key) throws IOException {
        return (String) jsonObject.get(key);
    }

    private String readFile(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }
        String content = FileUtil.readFile(file.getAbsolutePath());
        return content;
    }

    private void write(File file, String key, String value, boolean persistence) throws IOException {
        JSONObject jsonObject = read2Json(file, true);
        jsonObject.put(key, value);
        if (jsonObjectCache == null) {
            jsonObjectCache = new JSONObject();
        }
        jsonObjectCache.put(file.getName(), jsonObject);
        if (persistence) {
            FileUtil.writeStringToFile(JSON.toJSONString(jsonObject), file.getAbsolutePath());
        }
    }


    private void delete(File file, String key, boolean persistence) throws IOException {
        JSONObject jsonObject = read2Json(file, true);
        jsonObject.remove(key);
        if (jsonObjectCache == null) {
            jsonObjectCache = new JSONObject();
        }
        jsonObjectCache.put(file.getName(), jsonObject);
        if (persistence) {
            FileUtil.writeStringToFile(JSON.toJSONString(jsonObject), file.getAbsolutePath());
        }
    }


    /**
     * 只读取一次
     * 是否读取文本
     */
    public JSONObject read2Json(File file, boolean caseNillReadFile) throws IOException {

        JSONObject jsonContent = null;
        if (jsonObjectCache != null) {
            jsonContent = (JSONObject) jsonObjectCache.get(file.getName());

        }
        if (jsonContent == null && caseNillReadFile) {
            String fileContent = readFile(file);
            if (StringUtils.isNotBlank(fileContent)) {
                jsonContent = JSONObject.parseObject(fileContent);
                if (jsonObjectCache == null) {
                    jsonObjectCache = new JSONObject();
                }
                jsonObjectCache.put(file.getName(), jsonContent);
            }

        }
        if (jsonContent == null) {
            jsonContent = new JSONObject();
            if (jsonObjectCache == null) {
                jsonObjectCache = new JSONObject();
            }
            jsonObjectCache.put(file.getName(), jsonContent);
        }
        return jsonContent;
    }


    /**
     * 只读取一次
     * 是否读取文本
     */
    public JSONObject read2JsonWithoutCache(String fileName) throws IOException {
        String filepath = System.getProperty("user.home") + File.separator + "filedb" + File.separator + fileName;
        File file = new File(filepath);
        String fileContent = readFile(file);
        if (StringUtils.isNotBlank(fileContent)) {
            JSONObject jsonContent = JSONObject.parseObject(fileContent);
            if (jsonObjectCache == null) {
                jsonObjectCache = new JSONObject();
            }
            jsonObjectCache.put(file.getName(), jsonContent);
            return jsonContent;
        }

        return new JSONObject();
    }


    public static void main(String[] args) throws IOException {
        //  System.out.println(System.getProperty("user.home"));
        // System.out.println(System.getProperty("user.dir"));

        //  FileDb.getInstance().insert("test","32","this is a ois doc.");
        // FileDb.getInstance().remove("test", "32");
        System.out.println(FileDb.getInstance().read("test", "32"));

    }
}
