package org.kx.util.session;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.kx.util.DateUtil;

import java.text.ParseException;
import java.util.*;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/16
 */

@Data
public class SessionDTO {
    private Date date ;
    private  String content ;

    public final static  String f ="yyyy-MM-dd HH:mm:ss SSS";

    public SessionDTO(String dateStr, String content) throws ParseException {
        this.date = DateUtil.getDate(dateStr,f);
        this.content = content;
    }

    public static Set<SessionDTO> parseJson(JSONObject content) throws ParseException {
        Set<SessionDTO> list = new TreeSet<SessionDTO>(new Comparator<SessionDTO>() {
            @Override
            public int compare(SessionDTO o1, SessionDTO o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        Iterator<Map.Entry<String, Object>> entries = content.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            String ors =  (String) entry.getValue() ;
            list.add(new SessionDTO(entry.getKey(), ors));
        }
        return  list;
    }

}
