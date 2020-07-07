package org.fla.nnd.s1;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.junit.Test;
import org.kx.util.DateUtil;
import org.kx.util.FileUtil;
import org.kx.util.mail.MailSendUtil;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Date;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/2/5
 */

public class CommonTest {
    @Test
    public void soo() throws ParseException {

        Date signedDate = DateUtil.getDate("2019-11-21 23:59:59","yyyy-MM-dd HH:mm:ss") ;
        signedDate = DateUtil.getMonthLastDay(signedDate);
        int month  = Months.monthsBetween(new DateTime(signedDate), new DateTime(DateUtil.getDate("2019-10-26 23:59:59","yyyy-MM-dd HH:mm:ss"))).getMonths() ;

        System.out.println(month);

        BigDecimal ww = BigDecimal.ZERO ;
        BigDecimal wwww = new BigDecimal(123456.789066666666666661);
        Integer runMonth = month;
        if(runMonth >0){
            ww =  wwww.divide(BigDecimal.valueOf(runMonth),6,RoundingMode.HALF_UP) .multiply(BigDecimal.valueOf(12)) ; //gmv折算成年
        }
        System.out.println(ww.toString());

    }
    @Test
    public void soqo() throws ParseException, IOException {
        String  lll = FileUtil.readFile("/Users/xianguang/temp/32.txt");
        String[] lines  = lll.split("\n");
        int li = 0;
        for(String line :lines){
            try{
                li ++ ;
                String[] words  = line.split("\t");

                BigDecimal result = new BigDecimal(words[2]).add(new BigDecimal(words[4]));
                if(result.compareTo(BigDecimal.ZERO) != 0){
                    System.out.println((li +1)+" ___ "+line);
                }
            }catch (Exception ee){
                System.out.println(li +" ___ "+ee);
            }

        }
    }


    @Test
    public void soqos() throws ParseException, IOException {
        String  lll = FileUtil.readFile("/Users/xianguang/temp/32.txt");
        String[] lines  = lll.split("\n");
        int li = 0;
        BigDecimal sss =BigDecimal.ZERO;
        for(String line :lines){
            try{
                li ++ ;
                String[] words  = line.split("\t");
                sss = sss.add(new BigDecimal(words[3]));
            }catch (Exception ee){
                System.out.println(li +" ___ "+ee);
            }

        }
        System.out.println(sss.toString());
    }


    @Test
    public  void sendMail() throws Exception {

        String content = FileUtil.readFile("").trim();
        MailSendUtil.sendCommonMail("King",content);

    }



    @Test
    public  void redd() throws Exception {
        String filePath =  "/Users/xianguang/temp/2559526500090588" ;
        StringBuffer buffer = new StringBuffer();
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            String[] ss = line.split("\t");
            StringBuffer buffer2 = new StringBuffer("INSERT INTO TABLE  xx  PARTITION(ds='ss') select ");
            int index = 0 ;
            for(String word :ss){
                if(StringUtils.isNotBlank(word)){
                    index ++ ;
                    if(index == 3) {
                        buffer2.append("NULL").append(",").append("\'").append(word).append("\'").append(",");
                        continue;
                    }
                    if(index == 4) {
                         continue;
                    }
                    if(index == 5) {
                        continue;
                    }
                    if(index == 6) {
                        buffer2.append("\'201903198481002031\'").append(",");

                    }

                        buffer2.append("\'").append(word).append("\'").append(",");



                }
            }
            String sss = buffer2.toString();
            sss = sss.substring(0,sss.length()-1)+" ;";

            buffer.append(sss); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        System.out.println(buffer.toString());

    }


    @Test
    public void redd2() throws Exception {
        String filePath = "/Users/xianguang/temp/2559526500770874";
        StringBuffer buffer = new StringBuffer();
        InputStream is = new FileInputStream(filePath);
        InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(ireader);
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            String[] ss = line.split("\t");
            StringBuffer buffer2 = new StringBuffer();
            int index = 0;
            for (String word : ss) {
                if (StringUtils.isNotBlank(word)) {
                    index++;
                    if (index == 3) {
                        buffer2.append("\t").append(word.substring(0, word.length() - 2));
                    } else if (index == 1) {
                        buffer2.append(word).append("\t");
                    } else {
                        buffer2.append("\t").append(word);
                    }


                }
            }
            String sss = buffer2.toString();
            buffer.append(sss); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
       // System.out.println(buffer.toString());
        FileUtil.writeStringToFile(buffer.toString(),"/Users/xianguang/temp/25595265007708745");
    }




}
