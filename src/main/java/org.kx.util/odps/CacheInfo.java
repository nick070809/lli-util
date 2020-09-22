package org.kx.util.odps;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.FileUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/8/4
 */

public class CacheInfo {


    @Test
    public void readChacheString() throws Exception {
        System.out.println(readChache(FileUtil.readFile("/Users/xianguang/Downloads/down/cacheInfo.txt")));
    }



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
        return list;
    }


    public static String readChache(String content) throws Exception {

        String[] lines = content.split("\n");
        List<CacheDetail> list = new ArrayList<>();
        StringBuilder sbt = new StringBuilder();
        for (String line : lines) {
            if (StringUtils.isNotBlank(line)) {
                List<CacheDetail> transaction = parse(line);
                sbt.append(transaction).append("</r>\n");
                list.addAll(transaction);
            }
        }
        BigDecimal freezeAll = BigDecimal.ZERO;
        BigDecimal unfreezeAll = BigDecimal.ZERO;
        BigDecimal freezeNow = BigDecimal.ZERO;
        if (!list.isEmpty()) {
            for (CacheDetail cacheDetail : list) {

                if (cacheDetail.getOperType().equals("unfreeze")) {
                    freezeNow = freezeNow.subtract(new BigDecimal(cacheDetail.getAmount()));
                    unfreezeAll = unfreezeAll.add(new BigDecimal(cacheDetail.getAmount()));
                } else if (cacheDetail.getOperType().equals("freeze")) {
                    freezeNow = freezeNow.add(new BigDecimal(cacheDetail.getAmount()));
                    freezeAll = freezeAll.add(new BigDecimal(cacheDetail.getAmount()));
                }
            }
        }
        sbt.append("</r>\nfreezeAll: " + freezeAll.longValue()).append(", ").append("unfreezeAll: " + unfreezeAll.longValue()).append(", ").append("freezeNow: " + freezeNow.longValue());
        return sbt.toString();
    }




}
