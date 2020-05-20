package org.high.engine;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/14
 */

public class HelloService {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(getPid());
        while (true){
            TimeUnit.SECONDS.sleep(1);
            hello();
        }
    }

    public static void hello(){
        System.out.println("hello world");
    }

    /**
     * 获取当前运行 JVM PID
     * @return
     */
    private static String getPid() {
        // get name representing the running Java virtual machine.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
        // get pid
        return name.split("@")[0];
    }


}
