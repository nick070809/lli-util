package nick.doc;

import org.junit.Test;

import java.io.IOException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/10/13
 */

public class DocLocationManager {


    @Test
    public void securityAllDocs() throws IOException {
        for (MyDocLocationEnum myDocLocationEnum : MyDocLocationEnum.values()) {
            DocUnit docUnit =  new DocUnit(myDocLocationEnum);
            docUnit.securityCodes();
        }
    }
    @Test
    public void securityCodes() throws IOException {
        MyDocLocationEnum myDocLocationEnum =  MyDocLocationEnum.valueOf("安全配置");
        DocUnit docUnit =  new DocUnit(myDocLocationEnum);
        docUnit.securityCodes();
    }

    @Test
    public void showCodes() throws IOException {
        MyDocLocationEnum myDocLocationEnum =  MyDocLocationEnum.valueOf("安全配置");
        DocUnit docUnit =  new DocUnit(myDocLocationEnum);
        docUnit.showCodes();
    }

    @Test
    public void rebackCodes() throws IOException {
        MyDocLocationEnum myDocLocationEnum =  MyDocLocationEnum.valueOf("日常常用");
        DocUnit docUnit =  new DocUnit(myDocLocationEnum);
        docUnit.rebackCodes();
    }


    public static   String getOriginDoc(String name) throws IOException {
        MyDocLocationEnum myDocLocationEnum =  MyDocLocationEnum.valueOf(name);
        DocUnit docUnit =  new DocUnit(myDocLocationEnum);
        return  docUnit.getOriginDoc();
    }
}
