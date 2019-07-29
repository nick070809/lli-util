package org.kx.util.base;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/9
 */

public class JFramePass {


    private   JTextField userName  =new JTextField();
    private   JPasswordField userPass =new JPasswordField();
    private   JTextField targetIp = new JTextField();
    private   JTextField oldIp  =new JTextField();
    private   JTextField libPathCmd  =new JTextField();
    private   JFrame f;

    private   JLabel label = new JLabel();

    public    JButton  getSubmitFrame() throws InterruptedException {

        this.f = new JFrame("输入域账户和密码");

        JLabel l1 = new JLabel("域账号:");
        l1.setBounds(20, 20, 50, 30);
        userName.setBounds(80, 20, 200, 30);
        f.add(l1);f.add(userName);

        JLabel l2 = new JLabel("密码:");
        l2.setBounds(320, 20, 50, 30);
        userPass.setBounds(380, 20, 200, 30);
        f.add(l2);f.add(userPass);

        JLabel targetIp_ = new JLabel("目标ip:");
        targetIp_.setBounds(20, 50, 50, 30);
        targetIp.setBounds(80, 50, 200, 30);
        f.add(targetIp_);f.add(targetIp);


        JLabel oldIp_ = new JLabel("对比ip:");
        oldIp_.setBounds(320, 50, 50, 30);
        oldIp.setBounds(380, 50, 200, 30);
        f.add(oldIp_);f.add(oldIp);

        JLabel libPathCmd_ = new JLabel("包路径:");
        libPathCmd_.setBounds(20, 80, 50, 30);
        libPathCmd.setBounds(80, 80, 500, 30);
        f.add(libPathCmd_);f.add(libPathCmd);


        label.setBounds(20, 110, 550, 50);
        f.add(label);

        JButton submit   =new JButton("确定");
        submit.setBounds(180, 180, 100, 30);
        f.add(submit);

        JButton concel   =new JButton("取消");
        concel.setBounds(280, 180, 100, 30);
        f.add(concel);

        f.setSize(600, 280);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setVisible(true);

        concel.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                //label.setText("test 3");
                System.exit(-1);
            }
        });
        return submit;
    }



    public JTextField getUserName() {
        return userName;
    }

    public void setUserName(JTextField userName) {
        this.userName = userName;
    }

    public JPasswordField getUserPass() {
        return userPass;
    }

    public void setUserPass(JPasswordField userPass) {
        this.userPass = userPass;
    }

    public JTextField getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(JTextField targetIp) {
        this.targetIp = targetIp;
    }

    public JTextField getOldIp() {
        return oldIp;
    }

    public void setOldIp(JTextField oldIp) {
        this.oldIp = oldIp;
    }

    public JTextField getLibPathCmd() {
        return libPathCmd;
    }

    public void setLibPathCmd(JTextField libPathCmd) {
        this.libPathCmd = libPathCmd;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public JFrame getF() {
        return f;
    }

    public void setF(JFrame f) {
        this.f = f;
    }

    public static void main(String[] args) throws InterruptedException {
        new  JFramePass().getSubmitFrame();
    }
}
