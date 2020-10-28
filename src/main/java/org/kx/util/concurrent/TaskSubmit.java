package org.kx.util.concurrent;

import org.kx.util.base.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/10/21
 */

public class TaskSubmit {


    public static int theadNum = 8;

    public static int batchTaskNum = 20000;

    int batach = 0;

    //97242078001
    //97240892001L
    public static void main(String[] args) throws InterruptedException {
        Long  start = 97242178001L ;
        new TaskSubmit().batchDeal(start, start+20000L);
    }


    public Integer getNextNo(int currentNo) {
        int nextNo = currentNo + 1;
        if (nextNo >= theadNum) {
            nextNo = 0;
        }
        return nextNo;

    }

    public void batchDeal(Long start, Long end) throws InterruptedException {
        List<RTask> rTasks = new ArrayList<>();
        List<Thread> rxTasks = new ArrayList<>();
        for (int index = 0; index < theadNum; index++) {
            RTask rTask = new RTask(index);
            rTasks.add(rTask);
            Thread thread = new Thread(rTask);
            rxTasks.add(thread);
            thread.start();
        }

        int no_ = 0;
        for (Long index = start; index < end; index = index + batchTaskNum) {
            Long finalIndex = index;
            List<Long> ids = new ArrayList<Long>() {{
                for (Long i = finalIndex; i < finalIndex + batchTaskNum; i++) {
                    add(i);
                }
            }};

            boolean submit = false;
            int times = 0;
            while (!submit) {
                times++;
                for (RTask rTask : rTasks) {
                    if ( (rTask.getNo() == getNextNo(no_))&& rTask.submitTask(ids)) {
                        System.out.println("submited " + rTask.getNo() + " ==> " + (finalIndex + batchTaskNum));
                        submit = true;
                        batach++;
                        break;
                    }
                    no_ = rTask.getNo();
                }
                if (times >= theadNum) {
                    Thread.sleep(times* 50L + 100L);
                }

            }
        }

        System.out.println("all task  done");


    }

    static class RTask implements Runnable {

        private Integer No;

        public RTask(Integer No) {
            this.No = No;
        }

        private List<Long> ids;


        private volatile int signal = 0;


        @Override
        public void run() {

            while (true) {

                if (signal == 1) {
                    doTask(ids);
                    signal = 0;
                }
            }
        }

        public synchronized boolean submitTask(List<Long> ids) {
            if (this.signal == 1) {
                //System.out.println(No + " submitTask false "+ System.currentTimeMillis());
                return false;
            }
            this.ids = ids;
            this.signal = 1;
            return true;
        }


        private void doTask(List<Long> ids) {
           // ids.stream().forEach(e -> {
                System.out.println("delete form xx_xxx where id in ("+ StringUtil.toIds(ids) +")");
           // });
        }

        public Integer getNo() {
            return No;
        }


    }


}
