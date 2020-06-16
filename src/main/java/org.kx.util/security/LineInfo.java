package org.kx.util.security;

import lombok.Data;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/6/16
 */
@Data
public class LineInfo {

    private  String originLine ;

    private  String preDictLine ;

    private  String dictedLine ;

    private  boolean hasNewWord ;


    public  static LineInfo of(String originLine){
        LineInfo lineInfo = new LineInfo();
        lineInfo.setOriginLine(originLine);
        return lineInfo;
    }

}
