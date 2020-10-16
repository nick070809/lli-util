package nick.doc;

import java.io.IOException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/8/11
 */

public class DocUnit extends AbstractCode {


    public DocUnit(MyDocLocationEnum myDocLocationEnum ) {
        super.privateSource = myDocLocationEnum.getPrivateSource();
        super.originSource = myDocLocationEnum.getOriginSource();
    }

    public void securityCodes() throws IOException {
        super.securityCodes();
    }


    public void showCodes() throws IOException {
        super.showCodes();
    }


    public void rebackCodes() throws IOException {
        super.rebackCodes();
    }


}
