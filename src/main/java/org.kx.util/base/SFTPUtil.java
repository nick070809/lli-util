package org.kx.util.base;

/**
 * Description ： Created by  xianguang.skx Since 2019/7/9
 */

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;
import org.kx.util.FileUtil;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

/**
 * SFTP帮助类
 * @author wangbailin
 *
 */
public class SFTPUtil {

    private  static Logger logger = Logger.getLogger(SFTPUtil.class);

    static String PATHSEPARATOR = "/";


    public static void downloadSftpFile(SSH ssh ,String ftpPath,String destFile) throws JSchException {

        Session session = null;
        Channel channel = null;

        JSch jsch = new JSch();
        session = jsch.getSession(ssh.getUser(), ssh.getIp(), 22);
        session.setPassword(ssh.getPassword());
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

            File file = new File(destFile);
            //创建文件
            if(!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }



            //InputStream inputStream = chSftp.get("/home/xianguang.skx/"+ftpPath);

            InputStream inputStream = chSftp.get(ftpPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            String line = null;
            StringBuilder buffer = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                buffer.append(line).append("\n");
            }

            FileUtil.writeStringToFile(buffer.toString(),destFile);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("create file error.");
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


    public static void downloadSftpDir(  String ftpHost,
                                       String ftpPath,   String destPath) throws JSchException {

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
            recursiveFolderDownload(ftpPath,destPath, chSftp) ;
         }  catch (Exception e) {
            e.printStackTrace();
            logger.info("download error.");
        } finally {
            //关闭持久连接  谨慎使用  详细见下面说明
            chSftp.quit();
            channel.disconnect();
            session.disconnect();
        }

    }


    private static void recursiveFolderDownload(String sourcePath, String destinationPath,ChannelSftp channelSftp) throws SftpException {
        Vector<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(sourcePath); // Let list of folder content

        //Iterate through list of folder content
        for (ChannelSftp.LsEntry item : fileAndFolderList) {

            if (!item.getAttrs().isDir()) { // Check if it is a file (not a directory).
                if (!(new File(destinationPath + PATHSEPARATOR + item.getFilename())).exists()
                    || (item.getAttrs().getMTime() > Long
                    .valueOf(new File(destinationPath + PATHSEPARATOR + item.getFilename()).lastModified()
                        / (long) 1000)
                    .intValue())) { // Download only if changed later.

                    new File(destinationPath + PATHSEPARATOR + item.getFilename());
                    channelSftp.get(sourcePath + PATHSEPARATOR + item.getFilename(),
                        destinationPath + PATHSEPARATOR + item.getFilename()); // Download file from source (source filename, destination filename).
                }
            } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) {
                new File(destinationPath + PATHSEPARATOR + item.getFilename()).mkdirs(); // Empty folder copy.
                recursiveFolderDownload(sourcePath + PATHSEPARATOR + item.getFilename(),
                    destinationPath + PATHSEPARATOR + item.getFilename(),channelSftp); // Enter found folder on server to read its contents and create locally.
            }
        }
    }


    public static void uploadSftpFile(SSH ssh ,String ftpHost ,String remoteFilepath, String localFilePath) throws JSchException, SftpException  {
        Session session = null;

        JSch jsch = new JSch();
        session = jsch.getSession(ssh.getUser(), ftpHost, 22);
        session.setPassword(ssh.getPassword());
        session.setTimeout(100000);
        uploadSftpFile(remoteFilepath, localFilePath, session);
    }



    private static void uploadSftpFile(String remoteFilepath, String localFilePath, Session session) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp chSftp = (ChannelSftp) channel;

        try {
            chSftp.put(localFilePath, remoteFilepath);
        } finally {
            chSftp.quit();
            channel.disconnect();
            session.disconnect();
        }
    }



    public static void main(String[] args) throws JSchException, SftpException {



    }
}