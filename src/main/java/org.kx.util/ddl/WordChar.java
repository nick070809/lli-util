package org.kx.util.ddl;

import lombok.Data;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2019/12/6
 */

@Data
public class WordChar {
    private  String word ;
    private  Boolean appendBankSpace ;

    public WordChar(String word) {
        this.word = word;
    }
}
