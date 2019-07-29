package org.kx.util.base;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/9
 */

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;
import org.kx.util.FileUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * SFTP帮助类
 * @author wangbailin
 *
 */
public class SFTPUtil {

    private  static Logger logger = Logger.getLogger(SFTPUtil.class);

    public static void downloadSftpFile(String ftpHost,
                                       String ftpPath,String destFile) throws JSchException {
        Session session = null;
        Channel channel = null;

        JSch jsch = new JSch();
        session = jsch.getSession(SSH.name, ftpHost, 22);
        session.setPassword(SSH.pass);
        session.setTimeout(100000);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        if(!session.isConnected())
            session.connect();

        channel = session.openChannel("sftp");

        if(!channel.isConnected())
            channel.connect();
        ChannelSftp chSftp = (ChannelSftp) channel;


        try {
            InputStream inputStream = chSftp.get("/home/xianguang.skx/"+ftpPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            String line = null;
            StringBuilder buffer = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                buffer.append(line).append("\n");
            }

            FileUtil.writeStringToFile(buffer.toString(),destFile);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("download error.");
        } finally {
            //关闭持久连接  谨慎使用  详细见下面说明
            chSftp.quit();
            channel.disconnect();
            session.disconnect();
        }

    }
    public static void main(String[] args) throws JSchException {


        //   SFTPUtil.downloadSftpFile("11.162.251.91", SSH.name,SSH.pass,22,"/home/xianguang.skx/jar.txt");

    }
}