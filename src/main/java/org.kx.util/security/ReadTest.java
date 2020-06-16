package org.kx.util.security;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kx.util.FileUtil;
import org.kx.util.generate.IdGenerate;

import java.io.*;
import java.util.List;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/6/16
 */

public class ReadTest {




    String trasFerDir =" ";
    String sourceDir = " ";
    String reTDir = " ";


    @Test
    public void tDir() throws Exception {

        List<File> files = FileUtil.filterHidden(sourceDir ,FileUtil.showListFile(new File(sourceDir)));
        String newFIle = IdGenerate.getId()+".txt";
        for(File file :files){
            if (FileUtil.isJava(FileUtil.getSuffix(file.getName()))) {
                 transfer(file.getAbsolutePath(),trasFerDir+newFIle);
            }
        }
    }


    @Test
    public void t1Dir() throws Exception {
        String newFIle = IdGenerate.getId()+".txt";
        transfer("",trasFerDir+newFIle);
    }


    public void transfer(String filePath,String dPath) throws Exception {
        LineAbility lineAbility = new LineAbility();
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
            sb.append(LineAbility.NEWLINE);
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        sb.append("\n");
        MyDict.upgradeDict();
        FileUtil.appedStringToFile(sb.toString(),dPath);
    }



    @Test
    public void retransfer() throws Exception {
        LineAbility lineAbility = new LineAbility();
        String filePath = "";
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
            String newFIle = IdGenerate.getId()+".txt";
            String[] lines =  line.split(LineAbility.NEWLINE);
            for(String line_ :lines){
                if(StringUtils.isBlank(line_)){
                    continue;
                }
                LineInfo lineInfo = new  LineInfo();
                lineInfo.setDictedLine(line_);
                lineAbility.reDictLine(lineInfo) ;
                sb.append(lineInfo.getOriginLine());
                sb.append("\n");
            }
            FileUtil.writeStringToFile(sb.toString(),reTDir+newFIle);
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();


    }
}
