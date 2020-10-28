package org.kx.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/10/21
 */

public class FutureSubmit {
    public static int theadNum = 8;

    public static int batchTaskNum = 2000;

    int batach = 0;


    public void batchDeal(Long start, Long end) throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newFixedThreadPool(theadNum);



        for (Long index = start; index < end; index = index + batchTaskNum) {
            Long finalIndex = index;
            List<Long> ids = new ArrayList<Long>() {{
                for (Long i = finalIndex; i < finalIndex + batchTaskNum; i++) {
                    add(i);
                }
            }};

        }

        System.out.println("all task  done");
    }


}
