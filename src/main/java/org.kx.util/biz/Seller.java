package org.kx.util.biz;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/3
 */

@Data
public class Seller implements Serializable {


    private  int age ;
    private  String name ;
    private  String homeAddr ;
    private  Date  birth = new Date() ;

    public Seller(int age, String name, String homeAddr) {
        this.age = age;
        this.name = name;
        this.homeAddr = homeAddr;
    }

}
