package org.kx.util.base;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/9
 */

public class SSH {

    public  static String pass ;
    public  static String name ;
    private String charset = null;
    private String ip = null;
    private String user = null;
    private String password = null;
    private Connection conn = null;
    private Session session = null;

    public SSH(String ipaddr) {
        this.ip = ipaddr;
        this.user = name;
        this.password = pass;
        this.charset = Charset.defaultCharset().toString();
    }

    public void SSHexec(String[] cmd) throws IOException {
        if (this.StartConn()) {
            for (int i = 0; i < cmd.length; i++) {
                System.out.print(this.exeCmd(cmd[i]));
            }
            this.connClose();
        }
    }



    public void SSHexec(String cmd) throws IOException {
        if (this.StartConn()) {
            this.exeCmd(cmd);
            this.connClose();
        }
    }
    private String exeCmd(String cmd) {
        InputStream input = null;
        String rs = null;
        try {
            session = conn.openSession();
            session.execCommand(cmd);
            input = session.getStdout();
            rs = processStdout(input, charset);
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }
    private  String processStdout(InputStream input ,String charset){
        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        try {
            while (input.read(buf) != -1) {
                sb.append(new String(buf, charset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private boolean StartConn() throws IOException {
        boolean succeed = false;
        try {
            conn = new Connection(ip);
            conn.connect();
            succeed = conn.authenticateWithPassword(user, password);
        } catch (IOException e) {
            e.printStackTrace();
            throw  e ;
        }
        return succeed;
    }
    private  void connClose(){
        conn.close();
    }






}