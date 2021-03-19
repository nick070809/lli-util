package org.kx.util.base;

import org.junit.Test;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/2
 */

public class LongUtil {



    @Test
    public  void getBuyerRoute(){
        Long buyerId=  119979748L;
        System.out.println(buyerId%1000000%4096);
    }


}
