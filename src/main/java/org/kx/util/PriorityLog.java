package org.kx.util;

import org.kx.util.biz.SqlLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/6
 */

public class PriorityLog {
    public Map<String, Map<SqlLevel, List<String>>> logLines = null;


    public void putInfo(String bizType, SqlLevel sqlLevel, String info) {
        if (info == null) {
            return;
        }
        if (logLines == null) {
            logLines = new HashMap<>();
        }
        Map<SqlLevel, List<String>> bizSqls = logLines.get(bizType);
        if (bizSqls == null) {
            bizSqls = new HashMap<>();
            logLines.put(bizType, bizSqls);
        }
        List<String> levelsqls = bizSqls.get(sqlLevel);
        if (levelsqls == null) {
            levelsqls = new ArrayList<String>();
            bizSqls.put(sqlLevel, levelsqls);
        }
        levelsqls.add(info);

    }


    public List<String> getLogs()  {

        List<String> infos = new ArrayList<>();
        if(logLines == null){
            return  infos;
        }

        for (Map.Entry<String, Map<SqlLevel, List<String>>> entry : logLines.entrySet()) {
            Map<SqlLevel, List<String>> bizSqls = entry.getValue();
            if (bizSqls != null) {
                if (bizSqls.isEmpty()) {
                    continue;
                }
                for (SqlLevel sqlLevel : SqlLevel.values()) {
                    List<String> logLines = bizSqls.get(sqlLevel);
                    if (logLines != null && !logLines.isEmpty()) {
                        for (String sql : logLines) {
                            infos.add(sql);
                        }
                    }
                }
            }
        }

        return infos;
    }


}
