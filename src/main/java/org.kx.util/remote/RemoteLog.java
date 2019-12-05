package org.kx.util.remote;

import org.apache.commons.lang3.StringUtils;
import org.kx.util.FileUtil;
import org.kx.util.base.SFTPUtil;
import org.kx.util.base.SSH;
import org.kx.util.dto.RemoteMechine;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2019/12/5
 */

public class RemoteLog {

    public String getLastPaylog(RemoteMechine remoteMechine) throws Exception {


        String cmd_ = null;

        //不能写200f
        if (StringUtils.isNotBlank(remoteMechine.getKeyWord())) {
            cmd_ = "tail -200 " + remoteMechine.getFilePath() + " |grep " + remoteMechine.getKeyWord() + " > ~/" + remoteMechine.getTargetTempFile();
        } else {
            cmd_ = "tail -200 " + remoteMechine.getFilePath() + " > ~/" + remoteMechine.getTargetTempFile();
        }

        SSH ssh = new SSH(remoteMechine.getTargetIp(), remoteMechine.getUserName(), remoteMechine.getUserPass());
        ssh.SSHexec(cmd_);
        SFTPUtil.downloadSftpFile(ssh, remoteMechine.getTargetIp(), remoteMechine.getTargetTempFile(), remoteMechine.getTargetLocalFile());
        return FileUtil.readFile(remoteMechine.getTargetLocalFile());
    }
}
