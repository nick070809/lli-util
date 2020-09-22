package nick.doc;

import org.junit.Test;

import java.io.IOException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/8/11
 */

public class DocUnit extends AbstractCode {

    public DocUnit(){
        super.privateSource = "/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/docs/private/日常常用.txt";
        super.originSource = "/Users/xianguang/IdeaProjects/nick070809/lli-util/src/main/resources/docs/private/日常常用.md";
    }





    @Test
    public void securityCodes() throws IOException {
        super.securityCodes();
    }

    @Test
    public void showCodes() throws IOException {
        super.showCodes();
    }

    @Test
    public void rebackCodes() throws IOException {
        super.rebackCodes();
    }


}
