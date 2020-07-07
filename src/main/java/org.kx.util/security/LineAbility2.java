package org.kx.util.security;

import java.util.Map;

public  class LineAbility2{
    /**
     *
     * @Description: 是否需要取消确认收入
     * @return
     * @throws Exception
     * @author xin
     */
    public static boolean cancelConfirm(Map<String,String> confirmConf) throws Exception {
        String[] startRanges = confirmConf.get("start").split(",");
        String[] endRanges = confirmConf.get("end").split(",");
        for(int i=0;i<startRanges.length;i++){

        }
        return false;
    }


}
