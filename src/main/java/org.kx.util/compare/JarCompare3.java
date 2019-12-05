package org.kx.util.compare;

import org.kx.util.base.LocalCacheManager;
import org.kx.util.base.SFTPUtil;
import org.kx.util.base.SSH;
import org.kx.util.base.WriteJarVersion;
import org.kx.util.dto.RemoteMechine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Description ： beta jar冲突比较 Created by  xianguang.skx Since 2018/8/31
 */

public class JarCompare3 {



    public void actionPerformed(RemoteMechine remoteMechine) throws Exception {

        String libPathCmd_ ="ls "+remoteMechine.getLibPath()+" > ~/"+remoteMechine.getTargetTempFile();

        SSH ssh1 = new SSH(remoteMechine.getOldIp(),remoteMechine.getUserName(),remoteMechine.getUserPass());
        ssh1.SSHexec(libPathCmd_);

        SSH ssh2 = new SSH(remoteMechine.getTargetIp(),remoteMechine.getUserName(),remoteMechine.getUserPass());
        ssh2.SSHexec(libPathCmd_);

        SFTPUtil.downloadSftpFile(ssh1,remoteMechine.getOldIp(), remoteMechine.getTargetTempFile(), remoteMechine.getOldLocalFile());
        SFTPUtil.downloadSftpFile(ssh2,remoteMechine.getTargetIp(), remoteMechine.getTargetTempFile(), remoteMechine.getTargetLocalFile());



        Map<String, String> old_ =  reWriteFile(remoteMechine.getOldLocalFile());
        Map<String, String> new_ =  reWriteFile(remoteMechine.getTargetLocalFile());

        String dffContent = WriteJarVersion.getDiffContent(old_, new_);
        System.out.println(dffContent);

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

    public static void main(String[] args) throws Exception {
        RemoteMechine remoteMechine = new RemoteMechine();
        String x = (String) LocalCacheManager.getInstance().getCache("x");
        remoteMechine.setUserPass(x);
        new  JarCompare3().actionPerformed(remoteMechine);
    }

}
