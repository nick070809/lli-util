package org.kx.util.biz;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/6
 */

@Data
public class SettleRequestInfo {

    private String operType;
    private JSONObject detailParams ;
    private JSONObject requestParams ;

    public SettleRequestInfo(String operType, JSONObject detailParams) {
        this.operType = operType;
        this.detailParams = detailParams;
    }
}
