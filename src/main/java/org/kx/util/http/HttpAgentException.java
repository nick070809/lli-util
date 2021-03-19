package org.kx.util.http;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/3/3
 */

public class HttpAgentException  extends Exception{

    private static final long serialVersionUID = 4052213654872400925L;

    public HttpAgentException() {
    }

    public HttpAgentException(String e) {
        super(e);
    }

    public HttpAgentException(Exception e) {
        super(e);
    }

    public HttpAgentException(String s, Exception e) {
        super(s, e);
    }


}
