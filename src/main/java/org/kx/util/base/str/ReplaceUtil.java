package org.kx.util.base.str;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.Validator;

import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/3/8
 */

public class ReplaceUtil {

    static String prefix = "&R";

    public  static  String makeNewTxt(String content,String txt,String suffix) {
        String[]   lines = content.split("\n");
        StringBuilder newTxt= new StringBuilder();
        String[]   txtPre = txt.split(prefix);
        for(String line :lines){
            if(StringUtils.isBlank(line)){
                continue;
            }
            List<String> words  = StringUtil.readWords(line) ;
            StringBuilder newTxtDetail= new StringBuilder();

            for(String fixx :txtPre){
                String indexStr = fixx.substring(0,1) ;
                if(Validator.isNum(indexStr)){
                    int index =  Integer.parseInt(indexStr)-1;
                    newTxtDetail.append(words.get(index)).append(fixx.substring(1));
                }else {
                    newTxtDetail.append(fixx) ;
                }
            }
            newTxt.append(newTxtDetail.toString()).append(suffix).append("\n");
        }
        return   newTxt.toString() ;
    }



    public  static  String getTxt(String content,Integer index) {
        String[]   lines = content.split("\n");
        StringBuilder newTxt= new StringBuilder();
        for(String line :lines){
            if(StringUtils.isBlank(line)){
                continue;
            }
            List<String> words  = StringUtil.readWords(line) ;
            newTxt.append(words.get(index-1)).append(",");
        }
        return   newTxt.toString() ;
    }




    @Test
    public  void test(){

        String content = "1 3.1 2.2\n2 6.3 4.2";

        System.out.println(getTxt(content,2));


    }
}
