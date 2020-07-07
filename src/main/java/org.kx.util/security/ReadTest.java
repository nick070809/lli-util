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

    LineAbility lineAbility = new LineAbility();

    @Test
    public void tDir() throws Exception {

        List<File> files = FileUtil.filterHidden(sourceDir ,FileUtil.showListFile(new File(sourceDir)));
        files = FileUtil.filteTest(files);
        String newFIle = IdGenerate.getId()+".txt";
        for(File file :files){
            if (FileUtil.isJava(FileUtil.getSuffix(file.getName()))) {
                analyze(file.getAbsolutePath());
            }
        }
        lineAbility.saveLineCount();
        int size = 0 ;
        for(File file :files){
            if (FileUtil.isJava(FileUtil.getSuffix(file.getName()))) {
                if(size % 15 == 0){
                    newFIle = IdGenerate.getId()+".txt";
                }
                transfer(file.getAbsolutePath(),trasFerDir+newFIle);
                size ++ ;
            }
        }
        MyDict.upgradeDict();
    }




    @Test
    public void t1Dir() throws Exception {
        String newFIle = IdGenerate.getId()+".txt";
        String path ="" ;
        analyze(path) ;
        transfer(path,trasFerDir+newFIle);
    }


    public void analyze(String filePath) throws Exception {
        File file = new File(filePath);
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
            lineAbility.standOriginLine2(lineInfo);
            lineAbility.preSaveLineCount(lineInfo.getStandOriLines());
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();

    }


    public void transfer(String filePath,String dPath) throws Exception {
        File file = new File(filePath);
        StringBuffer sb = new StringBuffer();
        InputStream is = new FileInputStream(file);
        String fileChar = FileUtil.getFilecharset(file);
        InputStreamReader ireader = new InputStreamReader(is, fileChar);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行

        String lastLineLastWord = null;
        boolean hasClassName = false ;
        StringBuilder sybs = new StringBuilder();
        while (line != null) { // 如果 line 为空说明读完了
            if(StringUtils.isBlank(line)){
                line = reader.readLine(); // 读取下一行
                continue;
            }

            line = line.replace(Symbol.BIAOGE_.getSymbol(),"");
            LineInfo lineInfo = LineInfo.of(line);
            lineAbility.parsingOriginLine(lineInfo) ;

            if(line.contains("class")){
                hasClassName = true ;
            }

            if(!hasClassName){
                String[] words = lineInfo.getPreDictLine().split(LineAbility.KONGGE);
                Symbol symbol = Symbol.nameOf(words[0]);
                if(symbol != null){
                    line = reader.readLine(); // 读取下一行   class之前的注释去掉
                    continue;
                }
            }

            //如果只有一个标点符号,把合并为一行
            String[] words = lineInfo.getPreDictLine().split(LineAbility.KONGGE);
            if(words.length ==1){
                Symbol symbol = Symbol.nameOf(words[0]);
                if(symbol != null){
                    if(sybs.length() ==0){
                        sybs.append(words[0]) ;
                    }else {
                        sybs.append(LineAbility.KONGGE).append(words[0]) ;
                    }
                    line = reader.readLine(); // 读取下一行
                    continue;
                }
            }/*else {
                String lastWord =  words[words.length-1];
                //
                if(lastWord.equals(Symbol.DAKUOHU_.name()) ||lastWord.equals(Symbol.FENHAO_.name())  ||lastWord.equals(Symbol.DOUHAO_.name()) ){
                    sybs.append(lineInfo.getPreDictLine()) ;
                    line = reader.readLine(); // 读取下一行
                    continue;
                }

            }*/
            //去掉邮件之类的无效注释
            if(line.trim().startsWith("*") && (line.contains("@") || line.contains("author"))){ //去除邮件
                line = reader.readLine(); // 读取下一行
                continue;
            }

            //去掉无效注释
            if(line.contains("TODO")){ //去除TODO
                line = reader.readLine(); // 读取下一行
                continue;
            }
            //去掉日志
            if(line.contains("ogger.info") || line.contains("ogger.warn") || line.contains("ogger.error") ){ //去除Logger
                line = reader.readLine(); // 读取下一行
                continue;
            }

            if(sybs.length() > 0){
                String sybss = sybs.toString();
                String[] wirds = sybss.split(LineAbility.KONGGE);
                boolean isValidMemo = false;
                if((Symbol.nameOf(wirds[0]).equals(Symbol.BEIZHUMEMO_) || Symbol.nameOf(wirds[0]).equals(Symbol.BEIZHUMEMOPRO_))&& Symbol.nameOf(wirds[wirds.length -1]).equals(Symbol.BEIZHUMEMOPROS_)){
                    isValidMemo = true ;
                }
                if(isValidMemo){
                    line = reader.readLine(); // 读取下一行
                    sybs  = new  StringBuilder();
                    continue;
                }


                LineInfo lastLineInfo = LineInfo.of(sybs.toString());
                lineAbility.parsingOriginLine(lastLineInfo) ;
                lineAbility.standOriginLine2(lastLineInfo);
                lineAbility.dicLine(lastLineInfo);
                sb.append(lastLineInfo.getDictedLine());
                sb.append(LineAbility.NEWLINE);
                sybs  = new  StringBuilder();
            }
            lineAbility.standOriginLine2(lineInfo);
            lineAbility.dicLine(lineInfo);
            sb.append(lineInfo.getDictedLine());
            sb.append(LineAbility.NEWLINE);
            line = reader.readLine(); // 读取下一行
        }
        sb.replace(sb.length()-2,sb.length(),"}");
        reader.close();
        is.close();
        sb.append("\n");
        String content = sb.toString();//.replace(LineAbility.KONGGE,"");
        //content = content.replace(Symbol.BIAOGE_.getSymbol(),"");
        //System.out.println(content);
        FileUtil.appedStringToFile(content,dPath);
    }


    @Test
    public void retransferwww() {
        String ll=" ";
        String content = ll.replace(LineAbility.KONGGE,"");
        System.out.println(content);
        content = ll.replace(Symbol.BIAOGE_.getSymbol(),"");
        System.out.println(content);
    }



    @Test
    public void retransfer() throws Exception {
        LineAbility lineAbility = new LineAbility();
        String filePath = trasFerDir+"12250.txt";
        StringBuffer sb = new StringBuffer();
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行

        int f = 0 ;
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
                //System.out.println(line_);

                lineAbility.reDictLine(lineInfo) ;
                System.out.println(lineInfo.getOriginLine());
                sb.append(lineInfo.getOriginLine());
                sb.append("\n");
            }
            FileUtil.writeStringToFile(sb.toString(),reTDir+newFIle);
            line = reader.readLine(); // 读取下一行
            f ++ ;
            if(f >=3){
                break; //只读一行看下
            }
        }
        reader.close();
        is.close();


    }
}
