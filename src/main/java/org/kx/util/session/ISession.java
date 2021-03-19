package org.kx.util.session;

import java.util.Date;
import java.util.Set;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/16
 */

public interface ISession {

    public Set<SessionDTO> read(String domain) throws Exception;

    public Date save(String domain,String words) throws Exception;
}
