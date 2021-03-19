package com.xian.db.proxy;

import lombok.Data;

import java.io.Serializable;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/12/7
 */

@Data
public class XUser  implements Serializable {

    private  Long id ;
    private  String name ;
    private  String xno;


    public XUser(Long id, String name, String xno) {
        this.id = id;
        this.name = name;
        this.xno = xno;
    }
}
