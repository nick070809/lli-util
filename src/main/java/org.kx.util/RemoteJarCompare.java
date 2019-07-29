package org.kx.util;

import org.kx.util.base.JFramePass;
import org.kx.util.base.PassActionListener;

import javax.swing.*;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/29
 */

public class RemoteJarCompare {


    public  void compare() throws InterruptedException {
        JFramePass jFramePass = new JFramePass();

        String userName = "xianguang.skx";
        String oldIp = "11.162.251.91";
        String newIp = "100.88.238.120";
        String defaultPath ="/home/admin/settle-center/target/settle-center/BOOT-INF/lib";


        jFramePass.getOldIp().setText(oldIp);
        jFramePass.getTargetIp().setText(newIp);
        jFramePass.getUserName().setText(userName);
        jFramePass.getLibPathCmd().setText(defaultPath);

        JButton submit =  jFramePass.getSubmitFrame();
        submit.addActionListener(new PassActionListener(jFramePass));
    }


}
