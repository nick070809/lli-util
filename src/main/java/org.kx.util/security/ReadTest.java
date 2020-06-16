package org.kx.util.security;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.FileUtil;

import java.io.*;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/6/16
 */

public class ReadTest {


    @Test
    public void transfer() throws Exception {
        LineAbility lineAbility = new LineAbility();
        String filePath = "";
        File file = new File(filePath);
        StringBuffer sb = new StringBuffer();
        InputStream is = new FileInputStream(file);
        String fileChar = FileUtil.getFilecharset(file);
        InputStreamReader ireader = new InputStreamReader(is, fileChar);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行

        while (line != null) { // 如果 line 为空说明读完了
            if(StringUtils.isBlank(line)){
                line = reader.readLine(); // 读取下一行
                continue;
            }
            LineInfo lineInfo = LineInfo.of(line);
            lineAbility.parsingOriginLine(lineInfo) ;
            lineAbility.dictOriginLine(lineInfo) ;
            sb.append(lineInfo.getDictedLine());
            sb.append("\n");
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        //System.out.println(sb.toString());
        MyDict.upgradeDict();
        FileUtil.writeStringToFile(sb.toString(),FileDict.filepath);
    }

    @Test
    public void retransfer() throws Exception {
        LineAbility lineAbility = new LineAbility();
        String filePath = FileDict.filepath;
        StringBuffer sb = new StringBuffer();
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行

        while (line != null) { // 如果 line 为空说明读完了
            if(StringUtils.isBlank(line)){
                line = reader.readLine(); // 读取下一行
                continue;
            }
            LineInfo lineInfo = new  LineInfo();
            lineInfo.setDictedLine(line);
            lineAbility.reDictLine(lineInfo) ;
            sb.append(lineInfo.getOriginLine());
            sb.append("\n");
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        System.out.println(sb.toString());
        //FileUtil.writeStringToFile(sb.toString(),FileDict.targetFilepath);
    }

}
