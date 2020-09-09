package org.kx.util.generate;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.biz.MoFiledUtil;
import org.kx.util.common.db.ModelFiled;

import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/8
 */

public class HtmlGenerate<T> {


    public String generateHerfList(List<T> objs,  String showClomuns,String urlClomuns) throws Exception {
        if (objs == null || objs.isEmpty() || StringUtils.isBlank(showClomuns) || StringUtils.isBlank(urlClomuns)   )  {
            return "";
        }

        StringBuilder data = new StringBuilder();
        for (Object obj : objs) {
            List<ModelFiled> fileds = MoFiledUtil.getModelFiled(obj);
            if (fileds == null || fileds.isEmpty()) {
                continue;
            }

            String  addUrl = null;
            String addTitle = null;
             for (ModelFiled filed : fileds) {

                if ( urlClomuns != null &&   urlClomuns.equals(filed.getClomunName())) {

                    addUrl = filed.getHtmlValue();
                }

                if (showClomuns.equals(filed.getClomunName())) {
                    addTitle = filed.getHtmlValue();
                }

                if(addUrl != null && addTitle != null){
                    continue;
                }
                // <a href="pages/util/updateOrderAttri.html">删除订单属性</a>&nbsp;&nbsp;&nbsp;
                // <a href="javascript:0" id="b01" type="button" onclick="sendRequest();"> 》》》</a>
                 //console.log  sendRequest
            }
            data.append("<p><a href=\"javascript:0\"  onclick=\"sendRequest('" + addUrl +"');\">" + addTitle +"</a></p>\n");
        }
        return data.toString();
    }




}
