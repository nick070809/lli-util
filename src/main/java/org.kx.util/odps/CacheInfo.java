package org.kx.util.odps;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/8/4
 */

public class CacheInfo {


    public static List<CacheDetail> parse(String line) {
        JSONArray jsonArray = JSON.parseArray(line);
        List<CacheDetail> list = new ArrayList<>();
        if (jsonArray != null) {

            for (int x = 0; x < jsonArray.size(); x++) {
                CacheDetail cacheDetail = new CacheDetail();
                cacheDetail.setAmount(jsonArray.getJSONObject(x).getString("amount"));
                cacheDetail.setOperType(jsonArray.getJSONObject(x).getString("operType"));
                list.add(cacheDetail);
            }
        }
        System.out.println(list);
        return list;
    }

    @Test
    public void readChacheInfoFile() throws Exception {
        String filePath = "/Users/xianguang/Downloads/down/cacheInfo.txt";
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        List<CacheDetail> list = new ArrayList<>();

        while (line != null) { // 如果 line 为空说明读完了
            if (StringUtils.isNotBlank(line)) {
                list.addAll(parse(line));
            }

            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        BigDecimal amount = BigDecimal.ZERO;
        if (!list.isEmpty()) {
            for (CacheDetail cacheDetail : list) {
                if (cacheDetail.getOperType().equals("unfreeze")) {
                    amount = amount.subtract(new BigDecimal(cacheDetail.getAmount()));
                } else if (cacheDetail.getOperType().equals("freeze")) {
                    amount = amount.add(new BigDecimal(cacheDetail.getAmount()));
                }
            }
        }

        System.out.println(amount.longValue());


    }



}
