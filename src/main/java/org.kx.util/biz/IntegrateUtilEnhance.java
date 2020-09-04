package org.kx.util.biz;

import org.kx.util.base.StringUtil;
import org.kx.util.common.db.ModelFiled;

import java.util.List;


/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2018/9/7
 */

public class IntegrateUtilEnhance<T> {
   // private   int size = 0;
    public     StringBuilder sbt = new StringBuilder();


    /**
     * @param objs        对象列表
     * @param showClomuns 需要展示的列表
     * @return
     * @throws Exception
     */
    public String getTableClomun(List<T> objs, List<String> showClomuns,List<String> jsonClomuns) throws Exception {

        if (objs == null || objs.isEmpty() || showClomuns == null || showClomuns.isEmpty()) {
            return "";
        }
        StringBuilder title = new StringBuilder();
        StringBuilder data = new StringBuilder();

        boolean addTitle = false;
        for (Object obj : objs) {
            List<ModelFiled> fileds = MoFiledUtil.getModelFiled(obj);
            if (fileds == null || fileds.isEmpty()) {
                continue;
            }
            data.append("<tr>");
            for (ModelFiled filed : fileds) {

                if (!showClomuns.contains(filed.getClomunName())) {
                    continue;
                }


                if (!addTitle) {
                    title.append("<th>");
                    title.append(filed.getClomunName());
                    title.append("</th>");
                }

                data.append("<td>");
                if(jsonClomuns != null && jsonClomuns.contains(filed.getClomunName())){
                    data.append(StringUtil.buildJosnHtml(filed.getHtmlValue()));
                }else {
                    data.append(filed.getHtmlValue());
                }

                data.append("</td>");

            }
            if (!addTitle) {
                addTitle = true;
            }
            data.append("</tr>");
        }

        return new StringBuilder("<table>").append("<tr>").append(title).append("</tr>").append(data).append("</table>").toString();

    }


    public   void putLine(String info) {
        if (info == null) {
            return;
        }
        if (BizConfigStrategy.ignoreDeleteLog && info.startsWith("delete")) {
            return;
        }
        if (BizConfigStrategy.ignoreSelectLog && info.startsWith("select")) {
            return;
        }
        if (BizConfigStrategy.ignoreInsertLog && info.startsWith("insert")) {
            return;
        }
        if (BizConfigStrategy.ignoreOtherLog && info.startsWith("_00_")) {
            return;
        }
        sbt.append("\n<p>" + info +"</p>");

    }

    public  String getHtml(){
            return  "<html>\n" +
                    "    <head>\n" +
                    "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/> " +
                    " </head>\n" +
                    "    <body>"+getContent()+
                    "   </body>\n" +
                    "</html>" ;
    }

    public  String getContent(){
        return   sbt.toString() ;
    }

}
