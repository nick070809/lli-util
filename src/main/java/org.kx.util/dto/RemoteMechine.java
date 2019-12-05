package org.kx.util.dto;

import lombok.Data;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2019/12/5
 */

@Data
public class RemoteMechine {
    private  String  userName ;
    private  String  userPass ;
    private  String  targetIp ="11.159.179.82";
    private  String  oldIp ="11.167.97.7";
    private  String  libPath ;
    private  String  targetTempFile ="10000.txt";
    //local info
    private String oldLocalFile = "~/temp/oldIp.log"; //old
    private String targetLocalFile = "~/temp/newIp.log"; //new




}
