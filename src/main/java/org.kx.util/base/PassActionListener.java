package org.kx.util.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/9
 */

public class PassActionListener implements ActionListener {


    private JFramePass jFramePass;

    public PassActionListener(JFramePass jFramePass) {
        this.jFramePass = jFramePass;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            final String old_file = "/Users/xianguang/temp/jarCompare/oldIp.log"; //old
            final String new_file = "/Users/xianguang/temp/jarCompare/newIp.log"; //new
            final String srcFile = "10000.txt";
            String libPathCmd = this.jFramePass.getLibPathCmd().getText();

            final String oldIp = this.jFramePass.getOldIp().getText();
            final String targetIp = this.jFramePass.getTargetIp().getText();

            final String libPathCmd_ ="ls "+libPathCmd+" > ~/"+srcFile;

            SSH.name =this.jFramePass.getUserName().getText();
            SSH.pass = new String(this.jFramePass.getUserPass().getPassword()) ;
            System.out.println("你好!"+ SSH.name +" ,你的jar包对比已经开始, 请稍后...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    jFramePass.getLabel().setText("你好!"+SSH.name +" ,你的jar包对比已经开始, 请稍后...");
                }
            }).start();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new SSH(oldIp).SSHexec(libPathCmd_);
                        new SSH(targetIp).SSHexec(libPathCmd_);
                        SFTPUtil.downloadSftpFile(oldIp, srcFile, old_file);
                        SFTPUtil.downloadSftpFile(targetIp, srcFile, new_file);
                        Map<String, String> old_ = reWriteFile(old_file);
                        Map<String, String> new_ = reWriteFile(new_file);
                        String filePath = WriteJarVersion.writeDiff(old_, new_);
                        System.out.println("你好!"+ SSH.name +" ,你的jar包对比已经完成. "+filePath);
                        jFramePass.getLabel().setText("你好!"+ SSH.name +" ,你的jar包对比已经完成. "+filePath);
                    } catch (Exception e1) {
                        jFramePass.getLabel().setText("你好!"+ SSH.name +" ,你的jar包对异常."+e1.getMessage());
                        e1.printStackTrace();

                    }
                }
            }).start();



        } catch (Exception e1) {
            System.out.println("你好!"+ SSH.name +" ,你的jar包对异常.");
            e1.printStackTrace();
        }
    }

    public static Map<String,String > reWriteFile(String filePath) throws Exception {

        Map<String,String > maps = new HashMap<String,String>();
        InputStream is = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("-rw")) {
                line  = line.substring("-rw-rw-r-- 1 admin admin   419592 May 28 19:23 ".length());
            }
            WriteJarVersion.parse(line,maps);
        }
        reader.close();
        is.close();
        return maps ;
    }
}
