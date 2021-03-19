package org.kx.util.session;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.kx.util.DateUtil;
import org.kx.util.db.FileDb;
import org.kx.util.rsa.DESUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/16
 */

public class ISessionIpml implements ISession {


    @Override
    public Set<SessionDTO> read(String domain) throws IOException, ParseException {
        JSONObject content = FileDb.getInstance().readAll(domain);
        Set<SessionDTO> sessionDTOS = SessionDTO.parseJson(content);
        if (sessionDTOS.size() >= 15) {
            reSize(domain, sessionDTOS);
            JSONObject ncontent = FileDb.getInstance().readAll(domain);
            sessionDTOS = SessionDTO.parseJson(ncontent);

        }
        for (SessionDTO sessionDTO : sessionDTOS) {
            sessionDTO.setContent(DESUtil.decrypt(sessionDTO.getContent()));
        }
        return sessionDTOS;
    }

    @Override
    public Date save(String domain, String words) throws IOException {
        String nw = DESUtil.encrypt(words);
        FileDb.getInstance().insertWithoutBackUp(domain, DateUtil.getDateTimeStr(new Date(), SessionDTO.f), nw);
        return null;
    }

    private synchronized void reSize(String domain, Set<SessionDTO> sessionDTOS) throws IOException {
        HashMap<String, String> ss = new HashMap<>();
        int index = 0;
        for (SessionDTO sessionDTO : sessionDTOS) {
            ss.put(DateUtil.getDateTimeStr(sessionDTO.getDate(), SessionDTO.f), sessionDTO.getContent());
            if (index >= 15/2) {
                break;
            }
            index++;
        }
        FileDb.getInstance().deleteAll(domain);
        FileDb.getInstance().batchInsert(domain, ss);
        return;
    }

    @Test
    public void test() throws IOException, ParseException, InterruptedException {
        String domain = "001.txt";
        for (int s = 1; s < 20; s++) {
            save(domain, "hello," + s);
            Thread.sleep(100L);
            if (s > 15) {
                read(domain);
            }
        }

        System.out.println(JSON.toJSONString(read(domain)));
        ;
    }
}
