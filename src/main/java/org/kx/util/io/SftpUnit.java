package org.kx.util.io;

import com.jcraft.jsch.JSchException;
import nick.doc.MdDoc;
import org.junit.Test;
import org.kx.util.base.SFTPUtil;
import org.kx.util.base.SSH;

import java.io.IOException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/23
 */

public class SftpUnit {

    @Test
    // Auth fail 申请下个人登陆权限
    public void downLoad() throws JSchException {
        SSH ssh = new SSH("100.83.221.101");
        SFTPUtil.downloadSftpFile(ssh, "/home/admin/opdata/ids.txt", "/Users/xianguang/Downloads/down/ids.txt");
    }


    @Test
    public void test() throws JSchException, IOException, ClassNotFoundException {
        //  downLoad() ;
       /* Object obj = IoUtil.readObjectFromFile("/Users/xianguang/Downloads/down/order.txt");
        System.out.println(obj.getClass());*/
        MdDoc ssh =   new MdDoc("1","2") ;

        IoUtil.writeOrderToFile(ssh, "/Users/xianguang/Downloads/down/orderw.txt");
        Object obj = IoUtil.readObjectFromFile("/Users/xianguang/Downloads/down/orderw.txt");
        System.out.println(obj.getClass());


    }

}
